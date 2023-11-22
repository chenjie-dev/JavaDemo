package CAS;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用普通的Integer和AtomicInteger来进行线程安全的操作。
 * 两个线程分别对valueInteger和valueAtomicInteger进行自增操作，最后输出它们的最终值。
 * AtomicInteger使用了CAS（Compare and Swap）机制来保证线程安全。
 */
public class IntegerSafetyExample {
    private static Integer valueInteger = 0;
    private static final AtomicInteger valueAtomicInteger = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                valueInteger++;
                valueAtomicInteger.incrementAndGet();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                valueInteger++;
                valueAtomicInteger.incrementAndGet();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("普通的Integer最终值: " + valueInteger);
        System.out.println("AtomicInteger最终值: " + valueAtomicInteger.get());
    }
}