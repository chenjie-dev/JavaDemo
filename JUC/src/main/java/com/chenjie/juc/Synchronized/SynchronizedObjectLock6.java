package com.chenjie.juc.Synchronized;

public class SynchronizedObjectLock6 implements Runnable {
    static SynchronizedObjectLock6 instence1 = new SynchronizedObjectLock6();
    static SynchronizedObjectLock6 instence2 = new SynchronizedObjectLock6();

    @Override
    public void run() {
        method();
    }

    // synchronized用在静态方法上，默认的锁就是当前所在的Class类，所以无论是哪个线程访问它，需要的锁都只有一把
    public static synchronized void method() {
        System.out.println("我是线程" + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "结束");
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(instence1);
        Thread t2 = new Thread(instence2);
        t1.start();
        t2.start();
    }
}
