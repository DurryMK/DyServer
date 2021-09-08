package server.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import server.servlet.base.Servlet;

public class ServletContext implements Context {

	private ConcurrentHashMap<String, Servlet> servlets = new ConcurrentHashMap<String, Servlet>();

	private static ServletContext servletContext;

	public static synchronized ServletContext getInstance() {
		if (servletContext == null)
			servletContext = new ServletContext();
		return servletContext;
	}

	public ConcurrentHashMap<String, Servlet> getServlets() {
		return this.servlets;
	}

	public void addServlet(String name,Servlet servlet) {
		this.servlets.put(name, servlet);
	}

}
