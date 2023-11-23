package com.chenjie.juc.Thread;

public class TestThread extends Thread {
    @Override
    public void run() {
        // 线程运行的逻辑
        System.out.println("线程正在运行");
    }

    public static void main(String[] args) {
        // 创建线程实例
        TestThread thread = new TestThread();
        // 启动线程
        thread.start();

        System.out.println("程序运行");

    }
}
