package Thread;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TestCompletableFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建第一个异步任务，模拟耗时操作
        CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("completableFuture1 " + Thread.currentThread().getName());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hello";
        });

        // 创建第二个异步任务，模拟耗时操作
        CompletableFuture<String> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("completableFuture2 " + Thread.currentThread().getName());

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "World";
        });

        // 使用thenCombine方法组合两个异步任务的结果
        CompletableFuture<String> combinedFuture = completableFuture1.thenCombine(completableFuture2, (result1, result2) -> result1 + " " + result2);

        // 使用get方法获取组合后的结果
        String result = combinedFuture.get();
        System.out.println(result); // 输出：Hello World
    }
}