package server.entity;


public class Cookie {
	private String name;
	private String value;
	private String domain;
	private int maxAge;
	private String path = "/";
	private String comment;
	private boolean secure;

	public Cookie(String name, String value) {
		this.name = name;
		this.value = value;
	}
	public Cookie(){
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isSecure() {
		return secure;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	@Override
	public String toString() {
		return "Cookie [name=" + name + ", value=" + value + ", domain=" + domain + ", maxAge=" + maxAge + ", path="
				+ path + ", comment=" + comment + ", secure=" + secure + "]";
	}
}
