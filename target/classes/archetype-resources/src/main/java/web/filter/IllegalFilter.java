package ${package}.web.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by chj
 */
public class IllegalFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;

		String queryString = request.getQueryString();
		String urlStr = request.getRequestURI().toString();
		if (!StringUtils.isEmpty(queryString)) {
			urlStr += "?" + queryString;
		}

		String decodeUrl = URLDecoder.decode(urlStr);
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		${package}.web.pasm.ServletContext.set(request, response);

		if (decodeUrl.contains("<") || decodeUrl.contains(">") || urlStr.contains("%df")) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else if (StringUtils.equals(request.getHeader("X-Requested-With"), "XMLHttpRequest")
				|| StringUtils.contains(request.getHeader("Accept"), "application/json")) {
			String urlPrefix = request.getScheme() //当前链接使用的协议
					+ "://" + request.getServerName()//服务器地址
					+ ":" + request.getServerPort();
			if (!request.getHeader("Referer").startsWith(urlPrefix)) {
				response.setHeader("SESSION_STATUS", "TIMEOUT");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			} else {
				filterChain.doFilter(request, response);
			}
		} else {

			filterChain.doFilter(request, response);
		}

	}

	@Override
	public void destroy() {

	}
}
