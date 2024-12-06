package com.liu.mall;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class test {
    public static void main(String[] args) {
//        System.out.println("请输入想循环打印的数量：");
//        Scanner scanner = new Scanner(System.in);
//        Integer maxNum=scanner.nextInt();
//        reentrantLock(maxNum);
                Scanner in = new Scanner(System.in);
                // 注意 hasNext 和 hasNextLine 的区别
                while (in.hasNextInt()) { // 注意 while 处理多个 case
                    int a = in.nextInt();
                    int b = in.nextInt();
                    //System.out.println(a + b);
                    double result=1;
                    double none=(double)(a-1)/a;
                    double record=1;
                    for(int i=1;i<=b;i++){
                        result=result*i/a;
                    }
                    if(none==0) {//只有一杯水的情况
                        System.out.println("1");continue;
                    }
                    for(int i =2*b-1;i<1000;i++){
                        if(i==2*b-1) {
                            record=i;
                            continue;
                        }
                        record=record+(i*none);
                        none=none*none;
                    }
                    result*=record;
//                    DecimalFormat df = new DecimalFormat("#0.00");
//                    String k=df.format(result);
//                    BigDecimal bigde= new BigDecimal(result);
//                    result= bigde.setScale(2,4).doubleValue();

                    result= Math.ceil(result);
                    System.out.println(result);
                }
            }



    /**
     * 使用synchronized锁+双重检验
     * @param max
     */
    public static void doubleDetection(int max) {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Object lock = new Object();
        AtomicInteger atomicInteger = new AtomicInteger();
        new Thread(()->{
            while (atomicInteger.get()<max){
                if(atomicInteger.get()%2!=0) continue;
                synchronized (lock){
                    //if(atomicInteger.get()%2==0){
                        System.out.println(atomicInteger.get()+": A");
                        atomicInteger.getAndIncrement();
                    //}
                }
            }
            countDownLatch.countDown();
        }).start();
        new Thread(()->{
            while (atomicInteger.get()<max-1){
                if(atomicInteger.get()%2==0) continue;
                synchronized (lock){
                    //if(atomicInteger.get()%2!=0){
                        System.out.println(atomicInteger.get()+": B");
                        atomicInteger.getAndIncrement();
                    //}
                }
            }
            countDownLatch.countDown();
        }).start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 采用reentrantLock公平锁
     * @param maxNum
     */
    public static void reentrantLock(int maxNum){
        CountDownLatch countDownLatch = new CountDownLatch(2);
        ReentrantLock reentrantLock = new ReentrantLock(true);
        AtomicInteger atomicInteger = new AtomicInteger();
        AtomicBoolean atomicBoolean = new AtomicBoolean();

        new Thread(()->{
            while (atomicInteger.get()<maxNum-1){
                //System.out.println("A在尝试获取锁");
                if(!atomicBoolean.get()) continue;
                reentrantLock.lock();
                System.out.println(atomicInteger.get()+": A");
                atomicInteger.getAndIncrement();
                reentrantLock.unlock();

            }
            countDownLatch.countDown();
        }).start();
        new Thread(()->{
            atomicBoolean.set(true);
            while (atomicInteger.get()<maxNum-1){
                //System.out.println("B在尝试获取锁");
                reentrantLock.lock();
                System.out.println(atomicInteger.get()+": B");
                atomicInteger.getAndIncrement();
                reentrantLock.unlock();
            }
            countDownLatch.countDown();
        }).start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
