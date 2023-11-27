package com.chenjie.juc.Volatile;


/**
 * Java内存模型(JMM)规定了所有的变量都存储在主内存中，主内存中的变量为共享变量，
 * 而每条线程都有自己的工作内存，线程的工作内存保存了从主内存拷贝的变量，
 * 所有对变量的操作都在自己的工作内存中进行，完成后再刷新到主内存中，
 * <p>
 *
 * 可见性：
 *  - 写volatile修饰的变量时，JMM会把本地内存中值刷新到主内存
 *  - 读volatile修饰的变量时，JMM会设置本地内存无效
 * <p>
 *
 * 有序性：
 * - 要避免指令重排序，synchronized、lock作用的代码块自然是有序执行的，volatile关键字有效的禁止了指令重排序，实现了程序执行的有序性；
 *
 */
public class VolatileTest extends Thread {

    /**
     * 用volatile修饰的变量，线程在每次使用变量的时候，都会读取变量修改后的最的值。
     * volatile很容易被误用，用来进行原子性操作。
     * 不加的话 当flag设置为false的时候 t1线程还是会继续运行
     */
    private volatile boolean flag = true;

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        while (flag) {

        }
        System.out.println("over!!!!!!!!");
    }

    public static void main(String[] args) throws InterruptedException {
        final VolatileTest vt = new VolatileTest();
        vt.start();
        Thread.sleep(1000);
        vt.setFlag(false);
    }

}