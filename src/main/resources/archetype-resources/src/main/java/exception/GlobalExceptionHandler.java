package ${package}.exception;

import com.alibaba.fastjson.JSON;
import ${package}.common.HttpStatusEnum;
import ${package}.common.ViewResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * description: 全局异常处理
 *
 * @author Andy
 * @version 1.0
 * @date 07/11/2020 01:15
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	private static final String REQ_PARAMS = "request-params";

	@ExceptionHandler(value = Exception.class)
	public ViewResult defaultErrorHandler(Exception e) {
		LOGGER.error("未处理的异常{}\n", e.getMessage(), e);
		ViewResult vr = ViewResult.instance();
		vr.code(HttpStatusEnum.INTERNAL_SERVER_ERROR.code())
				.msg(HttpStatusEnum.INTERNAL_SERVER_ERROR.reasonPhraseCN())
				.data(null);
		return vr;
	}

	@ExceptionHandler(ServletException.class)
	public ViewResult controllerExceptionHandler(HttpServletRequest req, Exception e) {
		Map<String, String> getRequestParams = getRequestParams(req);
		LOGGER.error("---ControllerException Handler---Host {} invokes url {} params: {} \nERROR: {}",
				req.getRemoteHost(), req.getRequestURL(), JSON.toJSONString(getRequestParams), e.getMessage(), e);
		ViewResult vr = ViewResult.instance();
		vr.code(HttpStatusEnum.BAD_REQUEST.code()).msg(HttpStatusEnum.BAD_REQUEST.reasonPhraseCN()).data(null);
		return vr;

	}

	@ExceptionHandler(value = ServiceException.class)
	public ViewResult defaultServiceExceptionHandler(ServiceException se) {
		LOGGER.error("业务层发生异常{}\n", se.getMessage(), se);
		ViewResult vr = ViewResult.instance();
		vr.code(HttpStatusEnum.INTERNAL_SERVER_ERROR.code())
				.msg(HttpStatusEnum.INTERNAL_SERVER_ERROR.reasonPhraseCN())
				.data(null);
		return vr;
	}

	/**
	 * 读取request中传过来的字符串
	 * 有些调用方不知道用什么方式调用，可能是【application/x-www-form-urlencoded】、【text/plain】、【application/json】
	 *
	 */
	private Map<String, String> getRequestParams(HttpServletRequest request) {
		String type = request.getContentType();
		Map<String, String> receiveMap = new HashMap<>();
		if ("application/x-www-form-urlencoded".equals(type)) {
			Enumeration<String> enu = request.getParameterNames();
			while (enu.hasMoreElements()) {
				String key = String.valueOf(enu.nextElement());
				String value = request.getParameter(key);
				receiveMap.put(key, value);
			}
			//else是text/plain、application/json这两种情况
		} else {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				receiveMap.put(REQ_PARAMS, sb.toString());
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return receiveMap;
	}

}
