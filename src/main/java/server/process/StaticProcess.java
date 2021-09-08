package server.process;

import server.process.base.Processor;
import server.servlet.base.ServletRequest;
import server.servlet.base.ServletResponse;
import server.servlet.JeHttpServletResponse;

public class StaticProcess implements Processor {
	public void process(ServletRequest request, ServletResponse response) {
		((JeHttpServletResponse)response).sendRedirect();
	}
}
