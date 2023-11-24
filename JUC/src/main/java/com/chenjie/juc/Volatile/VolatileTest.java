package com.chenjie.juc.Volatile;


/**
 * Java内存模型(JMM)规定了所有的变量都存储在主内存中，主内存中的变量为共享变量，
 * 而每条线程都有自己的工作内存，线程的工作内存保存了从主内存拷贝的变量，
 * 所有对变量的操作都在自己的工作内存中进行，完成后再刷新到主内存中，
 */
public class VolatileTest extends Thread {

    public static void main(String[] args) {

    }

}