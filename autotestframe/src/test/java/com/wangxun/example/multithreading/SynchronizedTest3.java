package com.wangxun.example.multithreading;

public class SynchronizedTest3 {

    //同步方法块，锁是括号里面的对象
    private void method1(){
        System.out.println("Method 1 start");
        try {
            synchronized (this) {
            System.out.println("Method 1 run");
            Thread.sleep(10000);
        }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Method 1 end");
    }
    private void method2(){
        System.out.println("Method 2 start");
        try {
            synchronized (this) {
                System.out.println("Method 2 run");
                Thread.sleep(1000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Method 2 end");
    }

    public static void main(String[] args) {
        final SynchronizedTest3 test = new SynchronizedTest3();
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
