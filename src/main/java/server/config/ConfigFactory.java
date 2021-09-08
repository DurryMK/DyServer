package server.config;

import org.apache.log4j.Logger;
import server.utils.XMLParser;

import java.util.HashMap;

/**
 * @Author: durry
 * @DateTime: 2021/9/7 9:38
 * @Describe:服务器配置初始化
 */
public class ConfigFactory {
    public static Logger logger = Logger.getLogger(ConfigFactory.class);
    
    public final static HashMap<String,String> serverConfig = new HashMap<String,String>();

    public final static HashMap<String,String> theadPoolConfig = new HashMap<String, String>();

    public static HashMap<String,String> webMapping = new HashMap<String, String>();
    
    public static void initConfig(){
        logger.debug("初始化Server配置");
        try{
            XMLParser.parserServerConfig(serverConfig,theadPoolConfig);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("初始化Server配置失败");
        }
        try{
            webMapping = XMLParser.parserWebXml();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("初始化Server配置失败");
        }
        logger.debug("配置初始化完成");
    }
}
