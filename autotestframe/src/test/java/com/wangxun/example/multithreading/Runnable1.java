package com.wangxun.example.multithreading;

public class Runnable1 implements Runnable{
    public synchronized void run() {
        for(int i=0;i<100;i++){
            System.out.println("Thread1:"+i);
        }
    }
}
