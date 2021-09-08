package server.threadPool;

import server.config.Constants;
import server.threadPool.base.Task;

public class ChildThread extends Thread {

    private volatile boolean runningFlag;

    private Task task;

    public ChildThread() {
        this.runningFlag = false;
    }

    public synchronized void run() {
        try {
            while (true) {
                if (runningFlag) {
                    long startTime = System.currentTimeMillis();
                    this.task.doTask();
                    this.setRunning(false);
                    Constants.logger.info(this.getName() + "执行完毕，耗时:" + (System.currentTimeMillis() - startTime));
                } else {
                    //任务执行完后进入等待
                    this.wait();
                }
            }
        } catch (Exception e) {
            Constants.logger.error(this.getName() + ":" + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return this.runningFlag;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public synchronized void setRunning(boolean flag) {
        this.runningFlag = flag;
        if (flag) {
            this.notifyAll();
        }
    }
}
