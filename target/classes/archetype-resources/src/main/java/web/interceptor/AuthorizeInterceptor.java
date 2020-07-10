package ${package}.web.interceptor;


import com.ultrapower.ioss.pasmclient.annotation.DataAuthorize;
import com.ultrapower.ioss.pasmclient.annotation.PageAuthorize;
import com.ultrapower.ioss.pasmclient.annotation.UnAuthorize;
import com.ultrapower.ioss.pasmclient.manager.UserManager;
import com.ultrapower.ioss.pasmclient.model.RegionType;
import com.ultrapower.ioss.pasmclient.model.User;
import ${package}.web.pasm.Config;
import ${package}.web.pasm.MenuEntity;
import ${package}.web.pasm.MenuManager;
import ${package}.web.pasm.UnPageAuthorize;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by chj
 * Update by whx
 */
public class AuthorizeInterceptor extends HandlerInterceptorAdapter {

	private Config config;
	private String unAuthor = "unAuthor";
	private Logger log = LoggerFactory.getLogger(AuthorizeInterceptor.class);


	public AuthorizeInterceptor(Config config) {
		this.config = config;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		HandlerMethod method = (HandlerMethod) handler;
		User user = UserManager.getInstance().getUser();

		PageAuthorize pageAuthorize;
		DataAuthorize dataAuthorize;
		UnPageAuthorize unPageAuthorize;

		UnAuthorize unAuthorize = method.getMethodAnnotation(UnAuthorize.class);
		//用户未登陆+不需要验证
		if ((user == null) && (unAuthorize == null)) {
			String queryString = request.getQueryString();
			String webRoot = request.getContextPath();
			String url = request.getServletPath();

			if (StringUtils.equals(request.getHeader("X-Requested-With"), "XMLHttpRequest")
					|| StringUtils.contains(request.getHeader("Accept"), "application/json")) {
				response.setHeader("SESSION_STATUS", "TIMEOUT");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return false;
			}
			if (!("/logout".equals(url) || "/login".equals(url) || url.startsWith("/login/") || "/error".equals(url))) {
				if (queryString != null && queryString.length() > 0) {
					url += "?" + queryString;
				}
				response.sendRedirect(webRoot + "/login?service=" + java.net.URLEncoder.encode(url, "UTF-8"));
				return false;
			}
			return true;
		}

		if (unAuthorize == null) {
			if (user.isAdministrator()) {
				log.debug("管理员用户,具备访问权限");
				return true;
			}
			boolean hasPermission = true;
			unPageAuthorize = method.getMethodAnnotation(UnPageAuthorize.class);

			if (unPageAuthorize == null) {
				pageAuthorize = method.getMethodAnnotation(PageAuthorize.class);
				//说明需要页面访问权限
				if (pageAuthorize != null) {
					hasPermission = user.hasPageAuthor(pageAuthorize.value());
				}
			}

			dataAuthorize = method.getMethodAnnotation(DataAuthorize.class);
			//具备页面访问权限且需要对数据权限进行判定
			if (hasPermission && dataAuthorize != null) {
				hasPermission = false;
				if (user.getAreaList() != null) {
					for (RegionType type : dataAuthorize.value()) {
						if (type == RegionType.Province && user.getAreaList().containsKey(String.valueOf(config.getProvince()))) {
							hasPermission = true;
							break;
						} else if (type == RegionType.Region && user.getAreaList().containsKey(String.valueOf(config.getRegion()))) {
							hasPermission = true;
							break;
						} else if (type == RegionType.District && user.getAreaList().containsKey(String.valueOf(config.getDistrict()))) {
							hasPermission = true;
							break;
						}
					}
				}
				log.debug("用户{}具有数据访问权限:{}", user.getUserName(), hasPermission);
			}

			if (!hasPermission) {
				//不具有权限
				log.warn("用户{}不具有访问{}的权限", user.getUserName(), method.getMethod().getName());
				request.getRequestDispatcher(unAuthor).forward(request, response);
				return false;
			}
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView == null) {
			return;
		}

		boolean isRedirectView = modelAndView.getView() instanceof RedirectView;
		if (isRedirectView) {
			return;
		}
		HandlerMethod method = (HandlerMethod) handler;
		UnAuthorize unAuthorize = method.getMethodAnnotation(UnAuthorize.class);
		UnPageAuthorize unPageAuthorize = method.getMethodAnnotation(UnPageAuthorize.class);

		if (!(StringUtils.equals(request.getHeader("X-Requested-With"), "XMLHttpRequest")
				|| StringUtils.contains(request.getHeader("Accept"), "application/json")
				|| method.getMethodAnnotation(ResponseBody.class) != null
				|| unAuthorize != null || unPageAuthorize != null)) {
			String url = request.getServletPath();
			String queryStr = request.getQueryString();
			if (queryStr != null && !queryStr.isEmpty()) {
				url += "?" + queryStr;
			}

			if (!"errorHtml".equals(method.getMethod().getName())) {
				PageAuthorize pageAuthorize = method.getMethodAnnotation(PageAuthorize.class);
				MenuEntity menuCfg = getMenuCfg(MenuManager.Instance.getMenuList(), url);
				String menuCfgId = menuCfg != null ? menuCfg.getId() : null;

				if (!hasPermission(pageAuthorize, modelAndView, menuCfgId)) {
					if (modelAndView.getViewName() != null &&
							!"login".equals(modelAndView.getViewName()) &&
							!modelAndView.getViewName().contains("redirect")) {
						modelAndView.setViewName(unAuthor);
					}
				} else {
					//写通过验证后的逻辑
				}
			}
		}
	}

	private MenuEntity getMenuCfg(List<MenuEntity> data, String url) {
		MenuEntity menuEntity = null;
		for (MenuEntity config : data) {
			if (config.getHref() != null && config.getHref().length() > 0) {
				if (config.getHref().equals(url)) {
					return config;
				} else if (url.startsWith(config.getHref())) {
					menuEntity = config;
				}
			}

			if (config.getChilden() != null && config.getChilden().size() != 0) {
				MenuEntity finded = getMenuCfg(config.getChilden(), url);
				if (finded != null && finded.getHref().equals(url)) {
					return finded;
				}

				if (finded != null && finded.getText().length() > 0) {
					menuEntity = finded;
				}
			}
		}

		return menuEntity;
	}

	private boolean hasPermission(PageAuthorize pageAuthorize, ModelAndView modelAndView, String menuId) {
		String viewName = modelAndView.getViewName();
		assert viewName != null;
		if ("login".equals(viewName) || "error".equals(viewName)
				|| viewName.startsWith("redirect:/login")) {
			return true;
		}
		User user = UserManager.getInstance().getUser();
		if (user.isAdministrator()) {
			return true;
		}
		if (pageAuthorize != null && user.hasPageAuthor(pageAuthorize.value())) {
			return true;
		}
		return user.hasMenuAuthor(menuId);
	}
}
