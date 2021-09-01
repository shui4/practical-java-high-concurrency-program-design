package indi.shui4.ch6.stampedlock;

import lombok.SneakyThrows;
import sun.misc.Unsafe;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.StampedLock;

/**
 * 请先看：{@link UnsafePark2 }理解 {@link Unsafe#park(boolean, long)}的特性
 *
 * @author shui4
 * @since 1.0
 */
public class StampedLockCPUDemo {

  static final StampedLock lock = new StampedLock();
  static Thread[] holdCpuThreads = new Thread[3];

  @SneakyThrows
  public static void main(String[] args) {
    new Thread(
            () -> {
              long readLong = lock.writeLock();
              LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(600));
              lock.unlockWrite(readLong);
            })
        .start();

    TimeUnit.MILLISECONDS.sleep(100);
    for (int i = 0; i < 3; ++i) {
      holdCpuThreads[i] = new Thread(new HoldCPUReadThread(), "HoldCPUReadThread-" + (i + 1));
      holdCpuThreads[i].start();
    }
    TimeUnit.SECONDS.sleep(5);
    System.out.println("interrupt");
    for (final Thread holdCpuThread : holdCpuThreads) {
      // 被打断之后，StampedLock.acquireRead中的1286行无法挂起，造成疯狂循环，吃CPU
      holdCpuThread.interrupt();
    }
    // StampedLock#acquireRead 中 1227行，无限循环造成CPU飙升
  }

  private static class HoldCPUReadThread implements Runnable {
    @Override
    public void run() {
      // 第一次在：StampedLock.acquireRead的1286行挂起
      long lockr = lock.readLock();
      System.out.println(Thread.currentThread().getName() + " 获得读锁");
      lock.unlockRead(lockr);
    }
  }
}
