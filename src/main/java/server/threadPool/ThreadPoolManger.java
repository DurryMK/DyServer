package server.threadPool;

import server.config.Constants;
import server.threadPool.base.Task;

import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author: durry
 * @DateTime: 2021/9/7 9:34
 * @Describe:线程池管理器
 */
public class ThreadPoolManger extends Thread {
    private volatile boolean status;
    String name = "DyThreadPool";
    //线程容器
    private Vector<Object> vector;
    //等待队列
    private volatile BlockingQueue<Task> waitQueue;
    private int waitQueueLength;
    private int maxNum;
    private int minNum;
    private boolean running = true;
    //线程容器每次扩充数量
    private int capacity = 5;

    public ThreadPoolManger(String name, int initThreads, int maxNum, int minNum, int waitQueueLength) {
        this.name = name;
        this.maxNum = maxNum;
        this.minNum = minNum;
        this.waitQueueLength = waitQueueLength;
        this.waitQueue = new LinkedBlockingQueue<Task>();
        this.vector = new Vector<Object>();
        if (waitQueueLength > 0) {
            this.waitQueue = new ArrayBlockingQueue<Task>(waitQueueLength);
        }
        for (int i = 1; i <= initThreads; i++) {
            ChildThread thread = new ChildThread();
            thread.setName(this.name + "-" + i);
            thread.start();
            vector.addElement(thread);
        }
        System.out.println(this.vector.size());
    }

    /**
     * 运行
     */
    public synchronized void run() {
        Constants.logger.info("线程池启动");
        this.status = true;
        System.out.println(this.status);
        while (running) {
            if (this.waitQueue.size() <= 0)
                continue;
            Constants.logger.info("当前队列数 " + this.waitQueue.size());
            Task task = waitQueue.peek();
            if (process(task)) {
                waitQueue.remove(task);
            }
        }
    }

    /**
     * 添加任务到队列
     *
     * @param task
     */
    public void addTask(Task task) {
        if (this.waitQueueLength < 0) {
            this.waitQueue.add(task);
            return;
        }
        if (this.waitQueueLength > 0 && this.waitQueue.size() >= this.waitQueueLength) {
            Constants.logger.debug("线程池已达到最大处理容量");
            return;
        }

    }

    /**
     * 执行任务
     *
     * @param task
     * @return
     */
    public boolean process(Task task) {
        int busy = 0;
        int i;
        //从线程池中依次取出未使用的线程,启动
        for (i = 0; i < vector.size(); i++) {
            ChildThread currentThread = (ChildThread) vector.elementAt(i);
            if (currentThread.isRunning()) {
                busy++;
                continue;
            }
            Constants.logger.debug("繁忙的线程:" + busy);
            currentThread.setTask(task);
            currentThread.setRunning(true);
            return true;
        }
        capacity();
        return false;
    }

    /*
     * 线程容器扩容
     * */
    public void capacity() {
        //vector 中所有线程均已在运行,则对vector进行扩容,每次扩大5
        int size = vector.size();
        if (size >= this.maxNum) {
            Constants.logger.debug("线程数已达到最大数量");
        }
        for (int j = size + 1; j <= size + this.capacity; j++) {
            ChildThread thread = new ChildThread();
            thread.setName(this.name + "-" + j);
            thread.start();
            vector.addElement(thread);
        }
    }

    public boolean getStatus() {
        return this.status;
    }
}
