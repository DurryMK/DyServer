package server.utils;


import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import server.config.Constants;

public class XMLParser {
    /**
     * 解析sever.xml
     */
    public static void parserServerConfig(HashMap<String, String> serverConfig, HashMap<String, String> theadPoolConfig) throws Exception {
        //解析Server.xml文档
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(Constants.SERVER_CONFIG);
        //获取所有Connector节点
        NodeList nodes = doc.getElementsByTagName("Connector");
        //遍历所有的Connector节点 获取端口信息
        for (int i = 0; i < nodes.getLength(); i++) {
            Element node = (Element) nodes.item(i);
            serverConfig.put(node.getAttribute("action"), node.getAttribute("port"));
        }
        //解析线程池配置
        nodes = doc.getElementsByTagName("Executor");
        //遍历所有的Executor节点
        for (int i = 0; i < nodes.getLength(); i++) {
            Element node = (Element) nodes.item(i);
            theadPoolConfig.put("name", node.getAttribute("name"));
            theadPoolConfig.put("initThreadNum", node.getAttribute("initThreadNum"));
            theadPoolConfig.put("maxThreadNum", node.getAttribute("maxThreadNum"));
            theadPoolConfig.put("minThreadNum", node.getAttribute("minThreadNum"));
            theadPoolConfig.put("waitQueueLength", node.getAttribute("waitQueueLength"));
        }
    }

    /**
     * 解析web.xml
     */
    public static HashMap<String, String> parserWebXml() throws Exception {
        HashMap<String, String> types = new HashMap<String, String>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(Constants.WEB_CONFIG);
        NodeList nodes = doc.getElementsByTagName("mime-mapping");
        NodeList welcomePage = doc.getElementsByTagName("welcome-file");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            NodeList childNodes = e.getChildNodes();
            types.put(childNodes.item(1).getTextContent(), childNodes.item(3).getTextContent());
        }
        for (int i = 0; i < welcomePage.getLength(); i++) {
            types.put("welcome-file", welcomePage.item(i).getTextContent());
        }
        return types;
    }

    /**
     * 生成一个cookie标识
     */
    public static String getCookieID() {
        String id = "";
        Random rd = new Random();
        for (int i = 0; i < 3; i++)
            id += (char) (rd.nextInt(25) + 65);
        id += new Date().getTime();
        return id;
    }
}
