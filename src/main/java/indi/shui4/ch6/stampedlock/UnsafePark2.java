package indi.shui4.ch6.stampedlock;

import lombok.SneakyThrows;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * Unsafe#park(false,0)会让线程一直等待，直到系统...，当其它线程对这个线程打断（interrupt）时，会使得park（挂起）结束
 *
 * @author shui4
 * @since 1.0
 */
public class UnsafePark2 {

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
    LockSupport.park();
    System.out.println("结束");
  }
}
