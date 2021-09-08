package server.threadPool;


import java.io.IOException;


import java.net.Socket;

import server.config.Constants;
import server.process.DynamicProcess;
import server.process.StaticProcess;
import server.servlet.JeHttpServletRequest;
import server.servlet.JeHttpServletResponse;
import server.threadPool.base.Task;

public class HttpTask implements Task {

    private Socket client;

    public HttpTask(Socket client) {
        this.client = client;
    }

    public Object doTask() {
        JeHttpServletRequest request = null;
        JeHttpServletResponse response = null;
        try {
            request = new JeHttpServletRequest(client.getInputStream());
            response = new JeHttpServletResponse(client.getOutputStream(), request);
            //解析request请求  取得请求的后缀  设定带有.do和.action后缀的请求为动态资源请求
            String uri = request.getRequestURI();
            String suffix = uri.substring(uri.lastIndexOf(".") + 1);
            if ("do".equals(suffix) || "action".equals(suffix)) {
                //动态资源请求
                DynamicProcess dynamicProcess = new DynamicProcess();
                dynamicProcess.process(request, response);
            } else {
                //静态资源请求
                StaticProcess staticProcess = new StaticProcess();
                staticProcess.process(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (response != null){
                response.outWrite(500, Constants.p500);
            }
        }
        return null;
    }
}
