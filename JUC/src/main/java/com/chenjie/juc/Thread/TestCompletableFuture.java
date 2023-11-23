package com.chenjie.juc.Thread;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

@SpringBootTest
public class TestCompletableFuture {


    /**
     * 构造函数创建
     * <p>
     * 最简单的方式就是通过构造函数创建一个CompletableFuture实例。
     * 如下代码所示。由于新创建的CompletableFuture还没有任何计算结果，这时调用join，当前线程会一直阻塞在这里
     */
    @Test
    public void testCompletableFuture1() {

        CompletableFuture<String> future = new CompletableFuture<>();

        // 此时，如果在另外一个线程中，主动设置该CompletableFuture的值，则上面线程中的结果就能返回。
//         future.complete("test");

        String result = future.join();

        System.out.println(result);
    }

    /**
     * supplyAsync创建
     * <p>
     * CompletableFuture.supplyAsync()也可以用来创建CompletableFuture实例。通过该函数创建的CompletableFuture实例会异步执行当前传入的计算任务。
     * <p>
     * 在调用端，则可以通过get或join获取最终计算结果。supplyAsync有两种签名：
     * public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
     * public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor)
     */
    @Test
    public void testCompletableFuture_supplyAsync() {

        /*
         * 使用supplyAsync创建CompletableFuture的示例：
         */
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    System.out.println("compute test");
                    return "test";
                }
        );
        String result = future.join();
        System.out.println("get result: " + result);
        System.out.println("结束");
    }


    /**
     * CompletableFuture.runAsync()也可以用来创建CompletableFuture实例。
     * 与supplyAsync()不同的是，runAsync()传入的任务要求是Runnable类型的，所以没有返回值。
     * 因此，runAsync适合创建不需要返回值的计算任务。
     * <p>
     * 同supplyAsync()类似，runAsync()也有两种签名：
     * public static CompletableFuture<Void> runAsync(Runnable runnable)
     * public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor)
     */
    @Test
    public void testCompletableFuture_runAsync() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("compute test");
        });

        /*
         * 由于任务没有返回值， 所以最后的打印结果是get result: null
         */
        System.out.println("get result: " + future.join());
    }

    /**
     * 异步回调方法
     * <p>
     * 同Future相比，CompletableFuture最大的不同是支持流式（Stream）的计算处理，多个任务之间，可以前后相连，从而形成一个计算流。
     * 比如：任务1产生的结果，可以直接作为任务2的入参，参与任务2的计算，以此类推。
     * <p>
     * CompletableFuture中常用的流式连接函数包括：
     * thenApply——有入参有返回
     * thenApplyAsync——有入参有返回
     * thenAccept——有入参无返回
     * thenAcceptAsync——有入参无返回
     * thenRun——无入参无返回
     * thenRunAsync——无入参无返回
     * thenCombinethen
     * CombineAsyncthen
     * Composethen
     * ComposeAsyncwhen
     * Completewhen
     * CompleteAsync
     * handle
     * handleAsync
     * 其中，带Async后缀的函数表示需要连接的后置任务会被单独提交到线程池中，从而相对前置任务来说是异步运行的。
     * 除此之外，两者没有其他区别。因此，为了快速理解，在接下来的介绍中，我们主要介绍不带Async的版本。
     */
    @Test
    public void testCompletableFuture_Async_Callback() {

    }

    /**
     * thenApply / thenAccept / thenRun互相依赖
     * <p>
     * thenApply提交的任务类型需遵从Function签名，也就是有入参和返回值，其中入参为前置任务的结果
     * <p>
     * thenAccept提交的任务类型需遵从Consumer签名，也就是有入参但是没有返回值，其中入参为前置任务的结果
     * <p>
     * thenRun提交的任务类型需遵从Runnable签名，即没有入参也没有返回值
     */
    @Test
    public void testCompletableFuture_Async_Callback1() {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("compute 1");
            return 1;
        });
        CompletableFuture<Integer> future2 = future1.thenApply((p) -> {
            System.out.println("compute 2");
            return p + 10;
        });

        /*
         * 在上面的示例中，future1通过调用thenApply将后置任务连接起来，并形成future2。
         * 该示例的最终打印结果为11，可见程序在运行中，future1的结果计算出来后，会传递给通过thenApply连接的任务，从而产生future2的最终结果为1+10=11。
         * 当然，在实际使用中，我们理论上可以无限连接后续计算任务，从而实现链条更长的流式计算。
         */
        System.out.println("result: " + future2.join());
    }

    /**
     * thenApply
     * 表示某个任务执行完成后执行的动作，即回调方法，会将该任务的执行结果即方法返回值作为入参传递到回调方法中，测试用例如下：
     */
    @Test
    public void testCompletableFuture_Async_Callback2() throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();
        // 创建异步执行任务:
        CompletableFuture<Double> job = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + " start job1,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {

            }
            System.out.println(Thread.currentThread() + " exit job1,time->" + System.currentTimeMillis());
            return 1.2;
        }, pool);
        //job关联的异步任务的返回值作为方法入参，传入到thenApply的方法中
        CompletableFuture<Void> job2 = job.thenApply((result) -> {
            System.out.println(Thread.currentThread() + " start job2,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {

            }
            System.out.println(Thread.currentThread() + " exit job2,time->" + System.currentTimeMillis());
            return "test:" + result;
        }).thenAccept((result) -> {
            //接收上一个任务的执行结果作为入参，但是没有返回值
            System.out.println(Thread.currentThread() + " start job3,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {

            }
            System.out.println(result);
            System.out.println(Thread.currentThread() + " exit job3,time->" + System.currentTimeMillis());
        }).thenRun(() -> {
            //无入参，也没有返回值
            System.out.println(Thread.currentThread() + " start job4,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {

            }
            System.out.println("thenRun do something");
            System.out.println(Thread.currentThread() + " exit job4,time->" + System.currentTimeMillis());
        });
        System.out.println("main thread start job.get(),time->" + System.currentTimeMillis());
        //等待子任务执行完成
        System.out.println("run result->" + job.get());
        System.out.println("main thread start job2.get(),time->" + System.currentTimeMillis());
        //job2 等待最后一个thenRun执行完成
        System.out.println("run result->" + job2.get());
        System.out.println("main thread exit,time->" + System.currentTimeMillis());
    }


    /**
     * exceptionally有返回
     * exceptionally方法指定某个任务执行异常时执行的回调方法，会将抛出异常作为参数传递到回调方法中，
     * 如果该任务正常执行则会exceptionally方法返回的CompletionStage的result就是该任务正常执行的结果
     */
    @Test
    public void testCompletableFuture_Async_Callback3() throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();
        // 创建异步执行任务:
        CompletableFuture<Double> job = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "job1 start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {

            }
            if (true) {
                throw new RuntimeException("test");
            } else {
                System.out.println(Thread.currentThread() + "job1 exit,time->" + System.currentTimeMillis());
                return 1.2;
            }
        }, pool);
        //job执行异常时，将抛出的异常作为入参传递给回调方法
        CompletableFuture<Double> job2 = job.exceptionally((param) -> {
            System.out.println(Thread.currentThread() + " start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {

            }
            System.out.println("error stack trace->");
            param.printStackTrace();
            System.out.println(Thread.currentThread() + " exit,time->" + System.currentTimeMillis());
            return -1.1;
        });
        //job正常执行时执行的逻辑，如果执行异常则不调用此逻辑
        CompletableFuture<Void> job3 = job.thenAccept((param) -> {
            System.out.println(Thread.currentThread() + "job2 start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {

            }
            System.out.println("param->" + param);
            System.out.println(Thread.currentThread() + "job2 exit,time->" + System.currentTimeMillis());
        });
        System.out.println("main thread start,time->" + System.currentTimeMillis());
        //等待子任务执行完成,此处无论是job2和job3都可以实现job2退出，主线程才退出，如果是job，则主线程不会等待job2执行完成自动退出了
        //job2.get时，没有异常，但是依然有返回值，就是job的返回值
        System.out.println("run result->" + job2.get());
        System.out.println("main thread exit,time->" + System.currentTimeMillis());
    }

    /**
     * whenComplete无返回
     * whenComplete主要用于注入任务完成时的回调通知逻辑。
     * 这个解决了传统future在任务完成时，无法主动发起通知的问题。前置任务会将计算结果或者抛出的异常作为入参传递给回调通知函数。
     */
    @Test
    public void testCompletableFuture_Async_Callback4() throws ExecutionException, InterruptedException {
        // 创建异步执行任务:
        CompletableFuture<Double> job = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "job1 start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            if (false) {
                throw new RuntimeException("test");
            } else {
                System.out.println(Thread.currentThread() + "job1 exit,time->" + System.currentTimeMillis());
                return 1.2;
            }
        });
        //job执行完成后会将执行结果和执行过程中抛出的异常传入回调方法，如果是正常执行的则传入的异常为null
        CompletableFuture<Double> job2 = job.whenComplete((a, b) -> {
            System.out.println(Thread.currentThread() + "job2 start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            if (b != null) {
                System.out.println("error stack trace->");
                b.printStackTrace();
            } else {
                System.out.println("run succ,result->" + a);
            }
            System.out.println(Thread.currentThread() + "job2 exit,time->" + System.currentTimeMillis());
        });
        //等待子任务执行完成
        System.out.println("main thread start wait,time->" + System.currentTimeMillis());
        //如果job是正常执行的，job2.get的结果就是job执行的结果
        //如果job是执行异常，则job2.get会抛出异常
        System.out.println("run result->" + job2.get());
        System.out.println("main thread exit,time->" + System.currentTimeMillis());
    }


    /**
     * handle有返回
     * handle与whenComplete的作用有些类似，但是handle接收的处理函数有返回值，而且返回值会影响最终获取的计算结果。
     * handle方法返回的CompletableFuture的result是回调方法的执行结果或者回调方法执行期间抛出的异常，与原始CompletableFuture的result无关了
     */
    @Test
    public void testCompletableFuture_Async_Callback5() throws ExecutionException, InterruptedException {
        // 创建异步执行任务:
        CompletableFuture<Double> job = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "job1 start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            if (true) {
                throw new RuntimeException("test");
            } else {
                System.out.println(Thread.currentThread() + "job1 exit,time->" + System.currentTimeMillis());
                return 1.2;
            }
        });
        //job执行完成后会将执行结果和执行过程中抛出的异常传入回调方法，如果是正常执行的则传入的异常为null
        CompletableFuture<String> job2 = job.handle((a, b) -> {
            System.out.println(Thread.currentThread() + "job2 start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            if (b != null) {
                System.out.println("error stack trace->");
                b.printStackTrace();
            } else {
                System.out.println("run succ,result->" + a);
            }
            System.out.println(Thread.currentThread() + "job2 exit,time->" + System.currentTimeMillis());
            if (b != null) {
                return "run error";
            } else {
                return "run succ";
            }
        });
        //等待子任务执行完成
        System.out.println("main thread start wait,time->" + System.currentTimeMillis());
        //get的结果是job2的返回值，跟job没关系了
        System.out.println("run result->" + job2.get());
        System.out.println("main thread exit,time->" + System.currentTimeMillis());
    }


    /**
     * 异步组合方法
     * <p>
     * thenCombine / thenAcceptBoth / runAfterBoth互相不依赖
     * <p>
     * 这三个方法都是将两个CompletableFuture组合起来，只有这两个都正常执行完了才会执行某个任务，
     * 区别在于，thenCombine会将两个任务的执行结果作为方法入参传递到指定方法中，且该方法有返回值；
     * thenAcceptBoth同样将两个任务的执行结果作为方法入参，但是无返回值；runAfterBoth没有入参，也没有返回值。
     * 注意两个任务中只要有一个执行异常，则将该异常信息作为指定任务的执行结果thenCombine最大的不同是连接任务可以是一个独立的。
     * <p>
     * <p>
     * CompletableFuture(或者是任意实现了CompletionStage的类型)，从而允许前后连接的两个任务可以并行执行（后置任务不需要等待前置任务执行完成），
     * 最后当两个任务均完成时，再将其结果同时传递给下游处理任务，从而得到最终结果。
     */
    @Test
    public void testCompletableFuture_Async_Combination() throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();
        // 创建异步执行任务:
        CompletableFuture<Double> job = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + " start job1,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            System.out.println(Thread.currentThread() + " exit job1,time->" + System.currentTimeMillis());
            return 1.2;
        });
        CompletableFuture<Double> job2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + " start job2,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {
            }
            System.out.println(Thread.currentThread() + " exit job2,time->" + System.currentTimeMillis());
            return 3.2;
        });
        //job和job2的异步任务都执行完成后，会将其执行结果作为方法入参传递给job3,且有返回值
        CompletableFuture<Double> job3 = job.thenCombine(job2, (a, b) -> {
            System.out.println(Thread.currentThread() + " start job3,time->" + System.currentTimeMillis());
            System.out.println("job3 param a->" + a + ",b->" + b);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {

            }
            System.out.println(Thread.currentThread() + " exit job3,time->" + System.currentTimeMillis());
            return a + b;
        });
        //job和job2的异步任务都执行完成后，会将其执行结果作为方法入参传递给job3,无返回值
        CompletableFuture job4 = job.thenAcceptBoth(job2, (a, b) -> {
            System.out.println(Thread.currentThread() + " start job4,time->" + System.currentTimeMillis());
            System.out.println("job4 param a->" + a + ",b->" + b);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {
            }
            System.out.println(Thread.currentThread() + " exit job4,time->" + System.currentTimeMillis());
        });
        //job4和job3都执行完成后，执行job5，无入参，无返回值
        CompletableFuture job5 = job4.runAfterBoth(job3, () -> {
            System.out.println(Thread.currentThread() + " start job5,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {

            }
            System.out.println("job5 do something");
            System.out.println(Thread.currentThread() + " exit job5,time->" + System.currentTimeMillis());
        });
        System.out.println("main thread start job.get(),time->" + System.currentTimeMillis());
        //等待子任务执行完成
        System.out.println("job run result->" + job.get());
        System.out.println("main thread start job5.get(),time->" + System.currentTimeMillis());
        System.out.println("job5 run result->" + job5.get());
        System.out.println("main thread exit,time->" + System.currentTimeMillis());
    }


    /**
     * applyToEither / acceptEither / runAfterEither
     * <p>
     * 这三个方法都是将两个CompletableFuture组合起来，只要其中一个执行完了就会执行某个任务(其他线程依然会继续执行)，
     * 其区别在于applyToEither会将已经执行完成的任务的执行结果作为方法入参，并有返回值；acceptEither同样将已经执行完成的任务的执行结果作为方法入参，但是没有返回值；
     * runAfterEither没有方法入参，也没有返回值。
     * 注意两个任务中只要有一个执行异常，则将该异常信息作为指定任务的执行结果。
     */
    @Test
    public void testCompletableFuture_Async_Combination2() throws ExecutionException, InterruptedException {
// 创建异步执行任务:
        CompletableFuture<Double> job = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + " start job1,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            System.out.println(Thread.currentThread() + " exit job1,time->" + System.currentTimeMillis());
            return 1.2;
        });
        CompletableFuture<Double> job2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + " start job2,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {
            }
            System.out.println(Thread.currentThread() + " exit job2,time->" + System.currentTimeMillis());
            return 3.2;
        });
        //job和job2的异步任务都执行完成后，会将其执行结果作为方法入参传递给job3,且有返回值
        CompletableFuture<Double> job3 = job.applyToEither(job2, (result) -> {
            System.out.println(Thread.currentThread() + " start job3,time->" + System.currentTimeMillis());
            System.out.println("job3 param result->" + result);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            System.out.println(Thread.currentThread() + " exit job3,time->" + System.currentTimeMillis());
            return result;
        });
        //job和job2的异步任务都执行完成后，会将其执行结果作为方法入参传递给job3,无返回值
        CompletableFuture job4 = job.acceptEither(job2, (result) -> {
            System.out.println(Thread.currentThread() + " start job4,time->" + System.currentTimeMillis());
            System.out.println("job4 param result->" + result);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {
            }
            System.out.println(Thread.currentThread() + " exit job4,time->" + System.currentTimeMillis());
        });
        //job4和job3都执行完成后，执行job5，无入参，无返回值
        CompletableFuture job5 = job4.runAfterEither(job3, () -> {
            System.out.println(Thread.currentThread() + " start job5,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            System.out.println("job5 do something");
            System.out.println(Thread.currentThread() + " exit job5,time->" + System.currentTimeMillis());
        });
        System.out.println("main thread start job.get(),time->" + System.currentTimeMillis());
        //等待子任务执行完成
        System.out.println("job run result->" + job.get());
        System.out.println("main thread start job5.get(),time->" + System.currentTimeMillis());
        System.out.println("job5 run result->" + job5.get());
        System.out.println("main thread exit,time->" + System.currentTimeMillis());
    }

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