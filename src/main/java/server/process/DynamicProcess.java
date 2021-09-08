package server.process;

import java.net.MalformedURLException;
import java.net.URL;


import java.net.URLClassLoader;
import java.util.Map;

import server.entity.ServletContext;
import server.process.base.Processor;
import server.servlet.base.Servlet;
import server.servlet.base.ServletRequest;
import server.servlet.base.ServletResponse;
import server.config.Constants;

import javax.servlet.http.HttpServletRequest;

public class DynamicProcess implements Processor {
    public void process(ServletRequest request, ServletResponse response) throws Exception {
        String uri = ((HttpServletRequest) request).getRequestURI();// 资源地址
        URL[] urls = new URL[1];
        urls[0] = new URL("file", null, Constants.SYSTEM_PATH);// 项目路径
        URLClassLoader ucl = new URLClassLoader(urls);// 类加载器扫描urls数组中的路径
        Class clazz = ucl.loadClass(getClassName(uri));// 加载指定的类
        String name = clazz.getName();
        Servlet servlet = ServletContext.getInstance().getServlets().get(name);
        // 第一次访问
        if (servlet == null) {
            Object o = clazz.newInstance();
            if (o != null && o instanceof Servlet) {
                // 实例化调用
                servlet = (Servlet) o;
                servlet.init();
                // 存入ServletContext
                ServletContext.getInstance().addServlet(name, servlet);
            }
        }
        servlet.service(request, response);
    }

    /**
     * 获取请求中的类名
     */
    private String getClassName(String uri) {
        return (uri.substring(uri.indexOf("/") + 1, uri.lastIndexOf(".")).replace("/", "."));
    }

}
