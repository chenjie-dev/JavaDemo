# AbstractQueuedSynchronizer简介
> AQS是一个用来构建锁和同步器的框架，使用AQS能简单且高效地构造出应用广泛的大量的同步器，比如我们提到的ReentrantLock，Semaphore，其他的诸如ReentrantReadWriteLock，SynchronousQueue，FutureTask等等皆是基于AQS的。
> 当然，我们自己也能利用AQS非常轻松容易地构造出符合我们自己需求的同步器。

# AQS 核心思想
AQS核心思想是，如果被请求的共享资源空闲，则将当前请求资源的线程设置为有效的工作线程， 并且将共享资源设置为锁定状态。如果被请求的共享资源被占用，那么就需要一套线程阻塞等待以及被唤醒时锁分配的机制，**这个机制AQS是用CLH队列锁实现的**，即将暂时获取不到锁的线程加入到队列中。 
> CLH(Craig,Landin,and Hagersten)队列是一个虚拟的双向队列(虚拟的双向队列即不存在队列实例，仅存在结点之间的关联关系)。AQS是将每条请求共享资源的线程封装成一个CLH锁队列的一个结点(Node)来实现锁的分配。

AQS使用一个int成员变量来表示同步状态，通过内置的FIFO队列来完成获取资源线程的排队工作。AQS使用CAS对该同步状态进行原子操作实现对其值的修改。
```java
    private volatile int state;//共享变量，使用volatile修饰保证线程可见性
```

状态信息通过procted类型的getState，setState，compareAndSetState进行操作

```java
    //返回同步状态的当前值
    protected final int getState() {  
    return state;
    }
    // 设置同步状态的值
    protected final void setState(int newState) {
    state = newState;
    }
    //原子地(CAS操作)将同步状态值设置为给定值update如果当前同步状态的值等于expect(期望值)
    protected final boolean compareAndSetState(int expect, int update) {
    return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }
```

# AQS 对资源的共享方式
AQS定义两种资源共享方式
- Exclusive(独占)：只有一个线程能执行，如ReentrantLock。
    - 又可分为公平锁和非公平锁： 公平锁：按照线程在队列中的排队顺序，先到者先拿到锁
    - 非公平锁：当线程要获取锁时，无视队列顺序直接去抢锁，谁抢到就是谁的
- Share(共享)：多个线程可同时执行，如Semaphore/CountDownLatch。Semaphore、CountDownLatCh、 CyclicBarrier、ReadWriteLock 我们都会在后面讲到。
  
ReentrantReadWriteLock 可以看成是组合式，因为ReentrantReadWriteLock也就是读写锁允许多个线程同时对某一资源进行读。

不同的自定义同步器争用共享资源的方式也不同。自定义同步器在实现时只需要实现共享资源 state 的获取与释放方式即可，至于具体线程等待队列的维护(如获取资源失败入队/唤醒出队等)，AQS已经在上层已经帮我们实现好了。

# AQS底层使用了模板方法模式


# AQS作为并发编程的框架，下面列出了JUC中的几种同步工具，大体介绍一下AQS的应用场景：

| 同步工具 | 同步工具与AQS的关联 |
| --- | --- |
| ReentrantLock | 使用AQS保存锁重复持有的次数。当一个线程获取锁时，ReentrantLock记录当前获得锁的线程标识，用于检测是否重复获取，以及错误线程试图解锁操作时异常情况的处理。 |
| Semaphore | 使用AQS同步状态来保存信号量的当前计数。tryRelease会增加计数，acquireShared会减少计数。 |
| CountDownLatch | 使用AQS同步状态来表示计数。计数为0时，所有的Acquire操作（CountDownLatch的await方法）才可以通过。 |
| ReentrantReadWriteLock | 使用AQS同步状态中的16位保存写锁持有的次数，剩下的16位用于保存读锁的持有次数。 |
| ThreadPoolExecutor | Worker利用AQS同步状态实现对独占线程变量的设置（tryAcquire和tryRelease）。 |

