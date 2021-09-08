package server.entity.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import server.entity.HttpSession;

public class Session implements HttpSession {

	private Map<String, Object> session = new HashMap<String,Object>();

	public void setAttribute(String name, Object value) {
		this.session.put(name, value);
	}

	public Object getAttribute(String name) {
		return this.session.get(name);
	}

	public Set<String> getNames() {
		return session.keySet();
	}

	public void removeAttribute(String name) {
		this.session.remove(name);
	}

}
