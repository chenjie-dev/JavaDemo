package Thread;


public class TestRunnable implements Runnable {
    @Override
    public void run() {
        // 线程运行的逻辑
        System.out.println("线程正在运行");
    }

    public static void main(String[] args) {
        // 创建线程实例
        TestRunnable myRunnable = new TestRunnable();
        Thread thread = new Thread(myRunnable);

        System.out.println("程序运行");

        // 启动线程
        thread.start();
    }
}