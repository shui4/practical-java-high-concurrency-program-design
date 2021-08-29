package indi.shui4.ch4.lock;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings({"ReplacePseudorandomGenerator", "unchecked"})
public class ThreadLocalPerformanceExample {
  private static final int GET_COUNT = 10000000;
  private static final int THREAD_COUNT = 4;
  private static ExecutorService exe = Executors.newFixedThreadPool(THREAD_COUNT);

  private static Random rnd = new Random();
  private static ThreadLocal<Random> tRnd = ThreadLocal.withInitial(() -> new Random(123));

  public static class RndTask implements Callable<Long> {
    private int mode;

    public RndTask(int mode) {
      this.mode = mode;
    }

    private Random getRandom() {
      if (mode == 0) {
        return rnd;
      } else if (mode == 1) {
        return tRnd.get();
      } else {
        return null;
      }
    }

    @Override
    public Long call() {
      long b = System.currentTimeMillis();
      for (int i = 0; i < GET_COUNT; i++) {
        Objects.requireNonNull(getRandom()).nextInt();
      }
      long e = System.currentTimeMillis();
      long spend = e - b;
      System.out.println(Thread.currentThread().getName() + "花费:" + spend + "ms");
      return spend;
    }
  }

  @SuppressWarnings("SpellCheckingInspection")
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    Future<Long>[] futs = new Future[THREAD_COUNT];
    for (int i = 0; i < THREAD_COUNT; i++) {
      futs[i] = exe.submit(new RndTask(0));
    }
    long totaltime = 0;
    for (int i = 0; i < THREAD_COUNT; i++) {
      totaltime += futs[i].get();
    }
    // 15137ms
    System.out.println("多线程访问同一个Random实例：" + totaltime + "ms");

    for (int i = 0; i < THREAD_COUNT; i++) {
      futs[i] = exe.submit(new RndTask(1));
    }
    totaltime = 0;
    for (int i = 0; i < THREAD_COUNT; i++) {
      totaltime += futs[i].get();
    }
    // 577ms
    System.out.println("使用ThreadLocal包装Random实例：" + totaltime + "ms");
    exe.shutdown();
  }
}
