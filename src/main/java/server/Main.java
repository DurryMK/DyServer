package server;

import server.config.ConfigFactory;
import server.config.Constants;
import server.threadPool.HttpTask;
import server.threadPool.ThreadPoolManger;
import server.threadPool.base.Task;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * @Author: durry
 * @DateTime: 2021/9/7 9:34
 * @Describe:
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.startServer();
    }

    static {
        //初始化配置文件
        ConfigFactory.initConfig();
    }

    private boolean flag = true;

    private void startServer() throws Exception {
        ServerSocket ss;
        HashMap<String, String> serverConfig = ConfigFactory.serverConfig;
        HashMap<String, String> threadPoolConfig = ConfigFactory.theadPoolConfig;
        int accessNum = 0;
        //线程池实例
        ThreadPoolManger threadPoolManger = null;
        //启动监听
        try {
            int startPort = Integer.parseInt(serverConfig.get("start"));
            ss = new ServerSocket(startPort);
            //启动线程池
            if (threadPoolConfig.size() > 0) {
                //取得线程池配置
                String poolName = threadPoolConfig.get("name");
                int maxThreadNum = Integer.parseInt(threadPoolConfig.get("maxThreadNum"));
                int minThreadNum = Integer.parseInt(threadPoolConfig.get("minThreadNum"));
                int initThreadNum = Integer.parseInt(threadPoolConfig.get("initThreadNum"));
                int waitQueueLength = Integer.parseInt(threadPoolConfig.get("waitQueueLength"));
                //初始化线程池
                threadPoolManger = new ThreadPoolManger(poolName, initThreadNum, maxThreadNum,
                        minThreadNum, waitQueueLength);
                threadPoolManger.start();
                long startTime = System.currentTimeMillis();
                while (!threadPoolManger.getStatus()) {
                    //确保线程池已启动
                    if (System.currentTimeMillis() - startTime >= 30000) {
                        Constants.logger.info("线程池启动超时");
                        shutdown();
                        break;
                    }
                }
            }
            //输出启动日志
            Constants.logger.info("服务器已启动，端口号：" + startPort);
        } catch (Exception e) {
            Constants.logger.info("服务器启动失败，原因:");
            e.printStackTrace();
            return;
        }
        while (flag) {
            try {
                Socket client = ss.accept();
                accessNum++;
                Constants.logger.info("客户端访问，来源：" + client.getInetAddress() + "总访问数：" + accessNum);
                if (threadPoolManger != null) {
                    HttpTask task = new HttpTask(client);
                    threadPoolManger.addTask(task);
                } else {
                    // 未配置线程池
                    Task ts = new HttpTask(client);
                    ts.doTask();
                }
            } catch (Exception e) {
                //服务器运行时出错日志
                Constants.logger.error("访问异常：" + e.getMessage());
                e.printStackTrace();
                return;
            }
        }
        ss.close();
    }

    private void shutdown() {
        this.flag = false;
    }
}
