package ${package}.web.filter;


import com.ultrapower.ioss.pasmclient.manager.UserManager;
import ${package}.web.pasm.Config;
import ${package}.web.pasm.MenuEntity;
import ${package}.web.pasm.MenuManager;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2018/6/12/012.
 */
public class DefaultRequestFilter implements Filter {

	private Config config;

	public DefaultRequestFilter(Config config) {
		this.config = config;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		if (request.getServletPath().isEmpty() || "/".equals(request.getServletPath())) {
			String redirect = "unAuthor";
			String url = getMenuCfgId(MenuManager.Instance.getMenuList());
			if (StringUtils.isEmpty(url)) {
				redirect = config.getProperty("lm.defaultPage", redirect);
			} else {
				redirect = url;
			}

			if (!StringUtils.isEmpty(redirect) && !"/".equals(redirect)) {
				response.sendRedirect(request.getContextPath() + redirect);
			}
		} else {
			filterChain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {

	}

	private String getMenuCfgId(List<MenuEntity> menuEntities) {
		if (menuEntities == null || UserManager.getInstance().getUser() == null) {
			return null;
		}

		for (MenuEntity menuCfg : menuEntities) {
			if (!menuCfg.isVisible()) {
				continue;
			}
			if (!StringUtils.isEmpty(menuCfg.getHref())) {
				boolean author = UserManager.getInstance().getUser().hasMenuAuthor(menuCfg.getId());
				if (author) {
					return menuCfg.getHref();
				}
			}

			String url = getMenuCfgId(menuCfg.getChilden());
			if (url != null) {
				return url;
			}
		}

		return null;
	}
}
