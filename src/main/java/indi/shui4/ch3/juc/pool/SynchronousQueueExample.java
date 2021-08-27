package indi.shui4.ch3.juc.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SynchronousQueueExample {

  public static void main(String[] args) {
    ThreadPoolExecutor executor =
        new ThreadPoolExecutor(
            1,
            3,
            1,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(1),
            Executors.defaultThreadFactory(),
            new CustomRejectedExecutionHandler());

    Runnable runnable =
        () -> {
          try {
            TimeUnit.SECONDS.sleep(2);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          System.out.println(Thread.currentThread().getName());
        };
    for (int i = 0; i < 10; i++) {
      int finalI = i;
      executor.execute(
          () -> {
            System.out.println(finalI);
          });
    }
  }

  static class CustomRejectedExecutionHandler extends ThreadPoolExecutor.CallerRunsPolicy {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
      super.rejectedExecution(r, e);
      System.out.println(r.toString());
    }
  }
}
