package com.wangxun.example.multithreading;

public class Thread2 extends Thread {
    public void run(){
        for(int i=0;i<100;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread2:"+i);
        }
    }
}
