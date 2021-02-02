package com.wangxun.example.multithreading;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MyCallable implements Callable<Integer> {
    public Integer call(){
        int sum=0;
        for(int i=0;i<100;i++){
//            System.out.println(Thread.currentThread().getName() + " " + i);
            sum+=i;
        }
        return sum;
    }

    public static void main(String[] args) {
        // 创建MyCallable对象
        Callable<Integer> myCallable = new MyCallable();
        //使用FutureTask来包装MyCallable对象
        FutureTask<Integer> ft = new FutureTask<Integer>(myCallable);
        for(int i=0;i<100;i++){
            System.out.println(Thread.currentThread().getName() + " " + i);
            if(i==20){
                //FutureTask对象作为Thread对象的target创建新的线程
                Thread thread = new Thread(ft);
                //线程进入到就绪状态
                thread.start();
            }

        }

        int sum = 0;
        try {
            //取得新创建的新线程中的call()方法返回的结果,ft.get()方法会一直阻塞，直到call()方法执行完毕才能取到返回值。
            sum = ft.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("sum = " + sum);




    }
}
