package server.process.base;

import server.servlet.base.ServletRequest;
import server.servlet.base.ServletResponse;

import java.net.MalformedURLException;

public interface Processor {
	void process(ServletRequest request,ServletResponse response) throws Exception;
}
