package indi.shui4.ch3.juc.pool;

import java.util.concurrent.*;

public class TraceThreadPoolExecutor extends ThreadPoolExecutor {

  public TraceThreadPoolExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
  }

  public TraceThreadPoolExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      ThreadFactory threadFactory) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
  }

  public TraceThreadPoolExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      RejectedExecutionHandler handler) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
  }

  public TraceThreadPoolExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      ThreadFactory threadFactory,
      RejectedExecutionHandler handler) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
  }

  public static void main(String[] args) {
    TraceThreadPoolExecutor poolExecutor =
        new TraceThreadPoolExecutor(
            0, Integer.MAX_VALUE, 0L, TimeUnit.SECONDS, new SynchronousQueue<>());
    for (int i = 0; i < 10; i++) {
      final int finalI = i;
      poolExecutor.execute(
          () -> {
            System.out.println(100 / finalI);
          });
    }
  }

  @Override
  public void execute(Runnable command) {
    super.execute(wrap(command, clientTrace(), Thread.currentThread().getName()));
  }

  @Override
  public Future<?> submit(Runnable task) {
    return super.submit(wrap(task, clientTrace(), Thread.currentThread().getName()));
  }

  private Runnable wrap(Runnable task, Exception clientStack, String clientThreadName) {
    return () -> {
      try {
        task.run();
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    };
  }

  private Exception clientTrace() {
    return new Exception("Client stack trace");
  }
}
