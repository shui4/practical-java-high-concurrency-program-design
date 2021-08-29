package indi.shui4.ch4;

import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
public class DeadLock extends Thread {

  static final Object fork1 = new Object();
  static final Object fork2 = new Object();
  protected Object tool;

  public DeadLock(Object obj) {
    this.tool = obj;
    if (tool == fork1) {
      this.setName("哲学家A");
    }
    if (tool == fork2) {
      this.setName("哲学家B");
    }
  }

  @Override
  public void run() {
    if (tool == fork1) {
      synchronized (fork1) {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        synchronized (fork2) {
          System.out.println("哲学家A开始吃饭了");
        }
      }
    }
    if (tool == fork2) {
      synchronized (fork2) {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        synchronized (fork1) {
          System.out.println("哲学家B开始吃饭了");
        }
      }
    }
  }

  public static void main(String[] args) {
    DeadLock a = new DeadLock(fork1);
    DeadLock b = new DeadLock(fork2);
    a.start();b.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
