package server.servlet.base;

import java.io.PrintWriter;

import server.entity.Cookie;

public interface ServletResponse {

	/**
	 * 获取输出字符流
	 */
	public PrintWriter getWriter();

	/**
	 * 获取输出资源的类型
	 */
	public String getContentType();
	/**
	 * 响应
	 * */
	public void outWrite(int code,String filePath);
	
	/**
	 * 输出文本
	 * */
	public void textWriter(String result);
	
	/**
	 * 添加Cookie
	 * */
	public void addCookie(Cookie c);

	public  void setEncode(String encode);
}
