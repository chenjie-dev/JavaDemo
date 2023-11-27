package com.chenjie.juc.Volatile;


/**
 * 在多线程环境下使用volatile关键字时可能出现的重排序问题。
 * 代码中创建了两个线程，一个线程修改变量a和x的值，另一个线程修改变量b和y的值。
 * 在每次循环中，线程one先执行，然后线程two执行。线程one将变量a设置为1，然后将变量b的值赋给变量x。
 * 线程two将变量b设置为1，然后将变量a的值赋给变量y。
 * 由于重排序问题，可能会出现x和y都为0的情况，导致程序陷入无限循环。
 * 该代码通过循环执行并输出结果，直到出现x和y都为0的情况，然后退出循环。
 */
public class VolatileReSortTest extends Thread {

    private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        while (true) {
            i++;
            x = 0;
            y = 0;
            a = 0;
            b = 0;
            Thread one = new Thread(() -> {
                //由于线程one先启动，下面这句话让它等一等线程two. 读着可根据自己电脑的实际性能适当调整等待时间.
                shortWait(100000);
                a = 1;
                x = b;
            });

            Thread other = new Thread(() -> {
                b = 1;
                y = a;
            });

            one.start();
            other.start();
            one.join();
            other.join();

            String result = "第" + i + "次 (" + x + "," + y + "）";
            if (x == 0 && y == 0) {
                System.err.println(result);
                break;
            } else {
                System.out.println(result);
            }
        }
    }


    public static void shortWait(long interval) {
        long start = System.nanoTime();
        long end;
        do {
            end = System.nanoTime();
        } while (start + interval >= end);
    }
}