package com.chenjie.juc.Synchronized;

public class SynchronizedObjectLock7 implements Runnable {
    static SynchronizedObjectLock7 instence1 = new SynchronizedObjectLock7();
    static SynchronizedObjectLock7 instence2 = new SynchronizedObjectLock7();

    @Override
    public void run() {
        // 所有线程需要的锁都是同一把
        synchronized (SynchronizedObjectLock7.class) {
            System.out.println("我是线程" + Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "结束");
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(instence1);
        Thread t2 = new Thread(instence2);
        t1.start();
        t2.start();
    }
}
