package indi.shui4.ch6.stampedlock;

import indi.shui4.ch5.parallel_mode_and_algorithm.falsesharing.FalseSharing;
import lombok.SneakyThrows;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/**
 *  Unsafe#park(false,0)会让线程一直等待，直到系统...，当其它线程对这个线程打断（interrupt）时，会使得park（挂起）结束
 * @author shui4
 * @since 1.0
 */
public class UnsafePark {
  private static Unsafe getUnsafe() {
    try {
      Field field = Unsafe.class.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      return (Unsafe) field.get(null);
    } catch (Exception e) {
      return null;
    }
  }

  static Unsafe unsafe = getUnsafe();

  private static class InnerThread extends Thread {
    private final Thread mainThread;

    public InnerThread(Thread mainThread) {
      this.mainThread = mainThread;
      this.setName("InnerThread");
      this.start();
    }

    @SneakyThrows
    @Override
    public void run() {
      TimeUnit.SECONDS.sleep(5);
      mainThread.interrupt();
      System.out.println("main线程打断了");
    }
  }

  public static void main(String[] args) {
    new InnerThread(Thread.currentThread());
    unsafe.park(false, 0);
    System.out.println("结束");
  }
}
