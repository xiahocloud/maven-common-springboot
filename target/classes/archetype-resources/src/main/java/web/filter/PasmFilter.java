package ${package}.web.filter;

import com.ultrapower.ioss.pasmclient.manager.UserManager;
import ${package}.web.pasm.AjaxResponse;
import ${package}.web.pasm.MenuManager;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description:
 *
 * @author Andy
 * @version 1.0
 * @date 05/30/2019 16:55
 */
public class PasmFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		filterChain.doFilter(servletRequest, new AjaxResponse(request, response));
	}

}
