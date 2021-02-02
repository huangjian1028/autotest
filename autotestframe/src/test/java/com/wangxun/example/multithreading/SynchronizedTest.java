package com.wangxun.example.multithreading;

public class SynchronizedTest{

    //普通同步方法，锁是当前实例对象
    private synchronized void method1(){
        System.out.println("Method 1 start");
        try {
            System.out.println("Method 1 run");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Method 1 end");
    }
    private synchronized void method2(){
        System.out.println("Method 2 start");
        try {
            System.out.println("Method 2 run");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Method 2 end");
    }

    public static void main(String[] args) {
        final SynchronizedTest test = new SynchronizedTest();
        new Thread(new Runnable() {
            public void run(){
                test.method1();
            }
        }).start();
        new Thread(new Runnable() {
            public void run(){
                test.method2();
            }
        }).start();
    }
}
