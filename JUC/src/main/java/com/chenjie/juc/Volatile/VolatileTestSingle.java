package com.chenjie.juc.Volatile;


/**
 * 其实single = new Single()这段代码并不具备原子性，从代码层面上来说确实没问题，但是如果了解JVM指令的就知道其实在执行这句代码的时候在JVM中是需要执行三个指令来完成的，如下：
 * <p>
 * 1：分配对象的内存空间
 * memory = allocate();
 * 2：初始化对象
 * ctorInstance(memory);
 * 3：设置instance指向刚分配的内存地址
 * instance = memory;
 *
 *
 * 假设有A、B两个线程去调用该单例方法，当A线程执行到single = new Single()时，如果编译器和处理器对指令重新排序，指令重排后：
 *
 * 1：分配对象的内存空间
 * memory = allocate();
 * 3：设置instance指向刚分配的内存地址，此时对象还没被初始化
 * instance = memory;
 * 2：初始化对象
 * ctorInstance(memory);
 *
 * 当A线程执行到第二步（3：设置instance指向刚分配的内存地址，此时对象还没被初始化）变量single指向内存地址之后就不为null了，
 * 此时B线程进入第一个if，由于single已经不为null了，那么就不会执行到同步代码块，而是直接返回未初始化对象的变量single，从而导致后续代码报错。
 */
public class VolatileTestSingle extends Thread {

    //单例在私有静态变量single前面加了修饰符volatile能够防止JVM指令重排，从而解决了single对象可能出现成员变量未初始化的问题
    //private static volatile VolatileTestSingle single;
    private static VolatileTestSingle single;

    private VolatileTestSingle() {
    }

    public static VolatileTestSingle getInstance() {
        if (null == single) {
            synchronized (VolatileTestSingle.class) {
                if (null == single) {
                    single = new VolatileTestSingle();
                }
            }
        }
        return single;
    }

}