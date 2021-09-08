package server.entity;

public class CookieContainer {
	private static CookieContainer container;

	private Cookie[] cookies = new Cookie[15];

	public static CookieContainer getInstance() {
		if (container == null)
			container = new CookieContainer();
		return container;
	}

	public Cookie[] getCookies() {
		int length = 0;
		for(Cookie c : cookies){
			if(c!=null){
				length++;
			}
		}
		Cookie[] result = new Cookie[length];
		for (int i = 0; i < result.length; i++) {
			result[i] = cookies[i];
		}
		return result;
	}

	public void addCookies(Cookie c) {
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i] != null) {
				if (cookies[i].getName().equals(c.getName())) {
					cookies[i] = c;
					return;
				}
			} else {
				cookies[i] = c;
				return;
			}
		}
		cookies[cookies.length - 1] = c;
	}
}
