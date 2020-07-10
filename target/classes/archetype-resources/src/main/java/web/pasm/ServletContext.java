package ${package}.web.pasm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletContext {
    private static final ThreadLocal<ServletContext> contextHolder = new ThreadLocal<>();
    private HttpServletRequest request;
    private HttpServletResponse response;

    public static void set(HttpServletRequest request, HttpServletResponse response) {
        ServletContext context = new ServletContext(request, response);
        contextHolder.set(context);
    }

    public static ServletContext getCurrent() {
        return contextHolder.get();
    }

    public ServletContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
