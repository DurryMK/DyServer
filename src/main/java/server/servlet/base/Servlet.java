package server.servlet.base;

/**
 * 定义servlet的生命周期
 */
public interface Servlet {

    void init();

    void destroy();

    void service(ServletRequest request, ServletResponse response);
}
