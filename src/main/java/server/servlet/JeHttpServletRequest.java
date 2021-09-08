package server.servlet;

import java.io.File;


import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import server.entity.Cookie;
import server.entity.CookieContainer;
import server.entity.SessionContainer;
import server.entity.impl.Session;
import server.servlet.base.ServletRequest;
import server.utils.XMLParser;

public class JeHttpServletRequest implements ServletRequest {
    private String fullRequest;
    private String method;// 请求方法
    private String protocal;// 协议版本
    private String serverName;// 服务器名
    private int serverPort;// 端口号;
    private String requestURI;// 资源的地址
    private String requestURL;// 绝对路径
    private String contextPath;// 项目上下文路径
    private String realPath = System.getProperty("user.dir") + File.separatorChar + "webapps";// 项目路径
    private InputStream iis;
    private Map<String, String> cookies = new HashMap<String, String>();
    private Session session;

    public JeHttpServletRequest(InputStream iis) {
        this.iis = iis;
        parseRequest();
    }

    private void parseRequest() {
        this.fullRequest = readFromInputStream();// 从输入流上读取请求头
        if (fullRequest == null || "".equals(fullRequest)) {
            return;
        }
        //解析request 字符串
        StringTokenizer st = new StringTokenizer(fullRequest);
        if (st.hasMoreTokens()) {
            this.method = st.nextToken();
            String next = st.nextToken();
            this.requestURI = ("/".equals(next)) ? "/ROOT/index.html" : next;
            this.protocal = st.nextToken();
            this.contextPath = "/" + this.requestURI.split("/")[1];
        }
        parse();//解析requestURI
        //cookieHanle();//解析cookie
    }

    private void cookieHanle() {
        SessionContainer Container = SessionContainer.getInstance();
        Map<String, Session> map = Container.getSessionMap();

        if (cookies.isEmpty()) {
            // cookies为空,说明请求中没有cookie,客户端首次访问服务器,新建一个session
            String id = XMLParser.getCookieID();
            this.session = new Session();
            Container.addSessionMap(id, this.session);
            //创建新的cookie存入cookie容器
            Cookie cookie = new Cookie("JERRYSESSIONID", id);
            CookieContainer.getInstance().addCookies(cookie);
        } else {
            String id = cookies.get("JERRYSESSIONID");// 取出cookieid
            if (map.get(id) != null) {
                this.session = map.get(id);// 保存对应的session
            } else {
                // 没有id对应的session ,说明cookieid被修改,则新建session
                this.session = new Session();
                Container.addSessionMap(id, this.session);
            }
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                Cookie item = new Cookie(entry.getKey(), entry.getValue());
                CookieContainer.getInstance().addCookies(item);
            }
        }
        for (int i = 0; i < CookieContainer.getInstance().getCookies().length; i++) {
            System.out.println(CookieContainer.getInstance().getCookies()[i]);
        }
    }

    /**
     * 读取输入流中的数据
     */
    private String readFromInputStream() {
        String protocal = null;
        StringBuffer sb = new StringBuffer(1024 * 10);//
        int length = -1;
        byte[] bs = new byte[1024 * 10];
        try {
            //直接读取输入流 会有乱码
            length = this.iis.read(bs);

        } catch (Exception e) {
            e.printStackTrace();
            length = -1;
        }
        for (int i = 0; i < length; i++) {
            sb.append((char) bs[i]);
        }
        try {
            //读取后得到的数据并不是乱码  只需解码即可
            protocal = URLDecoder.decode(sb.toString(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return protocal;
    }

    /**
     * 解析请求中的路径,参数,cookie
     */
    public void parse() {
        String uri = this.requestURI;
        String[] params = null;
        // POST请求
        if ("post".equalsIgnoreCase(this.method)) {
            params = (fullRequest.substring(fullRequest.lastIndexOf("\r\n\r\n") + 1)).split("&");// 截取请求内容最后一行的参数部分
        }
        // GET请求
        if ("get".equalsIgnoreCase(this.method)) {
            if (uri.contains("?")) {// 是否有参数
                params = (uri.split("[?]")[1]).split("&");// 获取 ? 后的参数
                this.requestURI = uri.split("[?]")[0];// 去掉后面的参数返回资源地址
            }
        }
        // 保存参数
        if (params != null) {
            for (String param : params) {
                this.parameter.put(param.split("=")[0], param.split("=")[1]);
            }
        }
        // 解析cookie
        if (fullRequest.contains("Cookie")) {
            String cookieString = fullRequest.split("Cookie")[1].split("\n")[0].split(" ")[1];// 取到所有的cookie
            String[] cookieParams = cookieString.split(";");
            for (String cookieParam : cookieParams) {
                cookies.put(cookieParam.split("=")[0], cookieParam.split("=")[1]);// 存入Map
            }
        }
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public InputStream getIis() {
        return iis;
    }

    public void setIis(InputStream iis) {
        this.iis = iis;
    }

    Map<String, Object> attribute = new HashMap<String, Object>();
    Map<String, String> parameter = new HashMap<String, String>();

    public String getParameter(String key) {
        return parameter.get(key);
    }

    public Map<String, String> getParameterMap() {
        return parameter;
    }

    public Object getAttribute(String key) {
        return attribute.get(key);
    }

    public void setAttribute(String key, Object value) {
        attribute.put(key, value);
    }

    public Session getSession() {
        return this.session;
    }

    public Cookie[] getCookies() {
        return CookieContainer.getInstance().getCookies();
    }

}
