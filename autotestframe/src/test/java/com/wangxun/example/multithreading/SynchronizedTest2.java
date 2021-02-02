package com.wangxun.example.multithreading;

public class SynchronizedTest2 {

    //静态同步方法，锁是当前类的class对象
    private static synchronized void method1(){
        System.out.println("Method 1 start");
        try {
            System.out.println("Method 1 run");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Method 1 end");
    }
    private static synchronized void method2(){
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
        final SynchronizedTest2 test = new SynchronizedTest2();
        final SynchronizedTest2 test2 = new SynchronizedTest2();
        new Thread(new Runnable() {
            public void run(){
                test.method1();
            }
        }).start();
        new Thread(new Runnable() {
            public void run(){
                test2.method2();
            }
        }).start();
    }
}
