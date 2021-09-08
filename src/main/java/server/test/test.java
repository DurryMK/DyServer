package server.test;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * @Author: durry
 * @DateTime: 2021/9/8 14:51
 * @Describe:服务器性能测试
 */
public class test {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 20; i++) {
            Th th = new Th();
            th.start();
        }
        Scanner sc = new Scanner(System.in);
        int i = sc.nextInt();
    }
}

class Th extends Thread {
    private volatile int a = 0;

    @Override
    public synchronized void run() {
        URL url = null;
        try {
            url = new URL("http://localhost:9099/");
            URLConnection urlcon = null;
            for (int i = 0; i < 10000; i++) {
                urlcon = url.openConnection();
                urlcon.getInputStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
