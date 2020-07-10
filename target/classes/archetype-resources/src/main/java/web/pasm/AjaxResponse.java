package ${package}.web.pasm;


import com.ultrapower.pasm.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Properties;

/**
 * description:
 *
 * @author Andy
 * @version 1.0
 * @date 05/30/2019 16:41
 */
public class AjaxResponse extends HttpServletResponseWrapper {

	public static final String CONST_CAS_GATEWAY = "_const_cas_gateway_";
	private Properties prop = PropertiesUtil.loadProperties("security.properties");
	private String casServerLoginUrl;
	private boolean renew;
	private boolean gateway;
	private String excludePath;
	private HttpServletRequest request;

	public AjaxResponse(HttpServletRequest request, HttpServletResponse response) {
		super(response);
		this.casServerLoginUrl = this.prop.getProperty("iam.server.url", "http://127.0.0.1/ucas");
		this.renew = false;
		this.gateway = false;
		this.excludePath = this.prop.getProperty("iam.intercept.all.exceptives", "123.jsp");
		this.request = request;
	}


	@Override
	public void sendRedirect(String location) throws IOException {
		String decodeLocation = URLDecoder.decode(location, "utf-8");
		if (location.startsWith(this.casServerLoginUrl)) {
			/*String ipAndPort = this.getRealIpPortFromRequestHeader();
			String[] casUrls = decodeLocation.split("\\?");
			String casUrlPre = casUrls[0];
			String changeUrl = "";
			String casUrlPost = casUrls[1];
			String serviceUrlPost = "";
			if (casUrlPost.contains(";")) {
				String[] serviceUrls = casUrlPost.split(";");
				serviceUrlPost = ";" + serviceUrls[1];
			}
			changeUrl = ipAndPort + this.request.getServletPath();
			String redirectUrl = URLEncoder.encode(casUrlPre + "?" + "service=" + changeUrl + serviceUrlPost, "utf-8");*/

			/*//pasm登录重定向
			this.getResponse().setCharacterEncoding("utf-8");
			this.getResponse().setContentType("application/json; charset=utf-8");*/
			HttpServletResponse response = (HttpServletResponse) this.getResponse();
			response.sendRedirect(location);
		} else {
			super.sendRedirect(location);
		}
	}

	private String getRealIpPortFromRequestHeader() {
		return this.request.getHeader("host");
	}

		/*private String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();

	}*/
}
