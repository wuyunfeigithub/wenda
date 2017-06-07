package com.coodeer.wenda;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by common on 2017/6/6.
 */


//因为本人对java多线程机制和JUC包比较了解，所以就不做过多演示了
public class MultiThreadTest {

    static class myThread extends Thread{
        public void run(){
            System.out.println("i'm working in myThread");
        }
    }

    public static void main(String[] args){
        //线程启动方式
        //1
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                System.out.println("I'm t1 interface");
            }
        });
        //2
        Thread t2 = new myThread();

        //启动，进入就绪态
        t1.start();
        t2.start();

        //显示可重入锁
        //相较内置锁有如下优势：等待锁的时候可相应中锁、定时锁、中断锁和非非块结构加锁，锁分配策略还可以实现公平和非公平策略，其readWriteLock还可以把锁的动作分为读和写，更为灵活，并发度高
        ReentrantLock reentrantLock = new ReentrantLock();
        ReadWriteLock rwlock = new ReentrantReadWriteLock();

        //生产者消费者模式演示阻塞队列
        //有界阻塞队列，linkedBlockingQueue为无界阻塞队列
        final BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(10);
        final String[] words = {"I", "am", "very", "poor", "in", "english", "but", "I", "like", "coding"};
        Thread productor = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < words.length; i++){
                    try {
                        blockingQueue.put(words[i]);
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{
                        System.out.print(blockingQueue.take());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        productor.start();
        consumer.start();

        //线程局部变量
        //底层实现，每个线程都有一个ThreadLocalMap，共享的ThreadLocal的句柄作为key,每个线程独立的副本作为value
        final ThreadLocal<String> threadLocal = new ThreadLocal<String>(){
            protected String initialValue() {
                return "I'm yout init value";
            }
        };

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 10; i++){
                    threadLocal.set("t3's value");
                    System.out.print(threadLocal.get());
                    //让出时间片，让线程切换更频繁
                    Thread.yield();
                }

            }
        });
        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 10; i++){
                    threadLocal.set("t4's value");
                    System.out.print(threadLocal.get());
                    Thread.yield();
                }
            }
        });
        t3.start();
        t4.start();

        TestExecutor();

        //原子类，内存语义上保证可见性和原子性（一些复合操作封装在其方法里），内部实现为CAS
        //可以理解为加强版的volatile，只提供内存语义的可见性和赋值的原子性，对于一些复合操作无法保证原子性如：++
        AtomicInteger ai = new AtomicInteger(1);
    }

    public static void TestExecutor(){
        ExecutorService threadpool = Executors.newFixedThreadPool(2);
        threadpool.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++){
                    System.out.println("t1");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadpool.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++){
                    System.out.println("t2");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //异步任务演示,与callable配合使用，底层实现采用AQS
        Future<Integer> future = threadpool.submit(
                new Callable() {
                  @Override
                  public Integer call() throws Exception {
                      int sum = 0;
                      for(int i = 0; i < 100; i++){
                          sum += i;
                      }
                      return sum;
                  }
              }
        );
        try {
            System.out.print(future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        threadpool.shutdown();
    }

    //锁机制能从java内存语义上保证  可见性  和   原子性。
    //内置锁，锁住的是class的类对象，即MultiThreadTest.class
    public static synchronized void printSomething(){
        System.out.print("printSomething");
    }

    //内置锁，锁住的是MultiThreadTest的实例对象
    public synchronized void doWork(){
        System.out.print("printSomething");
    }

    //内置锁，锁住的是临界资源lock对象
    public void doWork(Object lock){
        synchronized(lock){
            System.out.print("printSomething");
        }
    }

}
