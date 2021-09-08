package server.entity;

import java.util.HashMap;
import java.util.Map;

import server.entity.impl.Session;

public class SessionContainer {

	private static SessionContainer container;

	private Map<String, Session> sessionMap = new HashMap<String, Session>();

	public static SessionContainer getInstance() {
		if (container == null)
			container = new SessionContainer();
		return container;
	}

	public Map<String, Session> getSessionMap() {
		return this.sessionMap;
	}

	public void addSessionMap(String cookieId, Session session) {
		this.sessionMap.put(cookieId, session);
	}
}
