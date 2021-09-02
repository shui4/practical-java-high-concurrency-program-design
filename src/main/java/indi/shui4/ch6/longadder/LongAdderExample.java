package indi.shui4.ch6.longadder;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings("all")
public class LongAdderExample {
  /** 线程数 */
  private static final int MAX_THREADS = 3;
  /** 目标数 */
  private static final long TARGET_COUNT = 1_000_000;
  /** 任务数 */
  private static final int TASK_COUNT = 3;

  static CountDownLatch cdladdr = new CountDownLatch(TASK_COUNT);
  static CountDownLatch cdlatomic = new CountDownLatch(TASK_COUNT);
  static CountDownLatch cdlsync = new CountDownLatch(TASK_COUNT);
  private final AtomicLong account = new AtomicLong();
  private final LongAdder lAccount = new LongAdder();
  private long count = 0;

  @Test
  @SneakyThrows
  public void testAtomic() {
    ExecutorService exe = Executors.newFixedThreadPool(MAX_THREADS);
    long starttime = System.currentTimeMillis();
    AtomicThread atomic = new AtomicThread(starttime);
    for (int i = 0; i < TASK_COUNT; i++) {
      exe.submit(atomic);
    }
    cdlatomic.await();
    exe.shutdown();
  }

  @Test
  @SneakyThrows
  public void testSync() {
    ExecutorService exe = Executors.newFixedThreadPool(MAX_THREADS);
    long starttime = System.currentTimeMillis();
    SyncThread sync = new SyncThread(starttime, this);
    for (int i = 0; i < TASK_COUNT; i++) {
      exe.submit(sync);
    }
    cdlsync.await();
    exe.shutdown();
  }

  @Test
  @SneakyThrows
  public void testAtomicLong() {
    ExecutorService exe = Executors.newFixedThreadPool(MAX_THREADS);
    long starttime = System.currentTimeMillis();
    LongAddrThread atomic = new LongAddrThread(starttime);
    for (int i = 0; i < TASK_COUNT; i++) {
      exe.submit(atomic);
    }
    cdladdr.await();
    exe.shutdown();
  }

  protected synchronized long getCount() {
    return count;
  }

  protected synchronized long inc() {
    return ++count;
  }

  public class AtomicThread implements Runnable {
    protected String name;
    protected long startime;

    public AtomicThread(long startime) {
      this.startime = startime;
    }

    @Override
    public void run() {
      long v = account.get();
      while (v < TARGET_COUNT) {
        v = account.incrementAndGet();
      }
      long endtime = System.currentTimeMillis();
      System.out.println("AtomicThread spend:" + (endtime - startime) + "ms v=" + v);
      cdlatomic.countDown();
    }
  }

  public class SyncThread implements Runnable {
    protected String name;
    protected long startime;
    LongAdderExample out;

    public SyncThread(long startime, LongAdderExample out) {
      this.startime = startime;
      this.out = out;
    }

    @Override
    public void run() {
      long v = out.getCount();
      while (v < TARGET_COUNT) {
        v = out.inc();
      }
      long endtime = System.currentTimeMillis();
      System.out.println("SyncThread spend:" + (endtime - startime) + "ms" + " v=" + v);
      cdlsync.countDown();
    }
  }

  public class LongAddrThread implements Runnable {
    protected String name;
    protected long starttime;

    public LongAddrThread(long starttime) {
      this.starttime = starttime;
    }

    @Override
    public void run() {
      long v = lAccount.sum();
      while (v < TARGET_COUNT) {
        lAccount.increment();
        v = lAccount.sum();
      }
      long endtime = System.currentTimeMillis();
      System.out.println("LongAdder spend:" + (endtime - starttime) + "ms v=" + v);
      cdladdr.countDown();
    }
  }
}
