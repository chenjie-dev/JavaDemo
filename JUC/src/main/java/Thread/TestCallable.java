package Thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        // 线程执行的逻辑
        System.out.println("线程正在运行");

        return "线程正在运行";
    }

    public static void main(String[] args) throws Exception {
        // 创建线程池
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // 提交任务并获取Future对象
        Future<String> future = executorService.submit(new TestCallable());

        // 获取线程执行结果
        String result = future.get();
        System.out.println(result);

        System.out.println("程序运行");

        // 关闭线程池
        executorService.shutdown();
    }
}
