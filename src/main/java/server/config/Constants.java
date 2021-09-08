package server.config;

import org.apache.log4j.Logger;

import java.util.Map;

/**
 * @Author: durry
 * @DateTime: 2021/9/7 9:37
 * @Describe:服务器常量
 */
public class Constants {
    /**
     * 加载server.xml路径
     */
    public final static String SERVER_CONFIG = "conf/server.xml";

    /**
     * web.xml路径
     */
    public final static String WEB_CONFIG = "conf/web.xml";

    /**
     * 日志对象
     */
    public final static Logger logger = Logger.getLogger(Constants.class);

    /**
     * 服务器路径
     */
    public final static String SYSTEM_PATH = System.getProperty("user.dir");

    /**
     * 返回所有响应类型
     */
    private static Map<String, String> types = null;
    /**
     * 响应页面路径
     */
    public static final String p500 = "ROOT/500.html";
    public static final String p404 = "ROOT/404.html";
}
