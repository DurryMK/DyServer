package server.servlet.base;


import java.util.Map;

import server.entity.Cookie;
import server.entity.impl.Session;

public interface ServletRequest {
	 String getRealPath();

	 Object getAttribute(String key);

	 void setAttribute(String key,Object value);

	 String getParameter(String key);

	 Map<String, String> getParameterMap();

	 void parse();

	 String getServerName();
	
	 Session getSession();

	 int getServerPort();
	
	 Cookie[] getCookies();
}
