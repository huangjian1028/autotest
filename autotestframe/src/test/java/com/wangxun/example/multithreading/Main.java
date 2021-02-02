package com.wangxun.example.multithreading;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // 创建一个Runnable实现类的对象
        Runnable runnable1 = new Runnable1();
        // 将myRunnable作为Thread target创建新的线程
        Thread thread1 = new Thread(runnable1);

        Thread thread2 = new Thread2();
//        thread1.run();
//        thread2.run();
        System.out.println("结束");


        ScheduledExecutorService timerService = Executors.newScheduledThreadPool(4);
        timerService.scheduleAtFixedRate(thread2, 10000, 100000, TimeUnit.MILLISECONDS);
    }
}
