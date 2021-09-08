package server.entity;

import java.util.Map;

import server.servlet.base.Servlet;

public interface Context {
	
	public Map<String , Servlet> getServlets();
	
	public void addServlet(String name,Servlet servlet);
}
