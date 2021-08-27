package indi.shui4.ch2.jichu;

import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.TimeUnit;

public class WaitExample {

  private static final Object MONITOR = new Object();

  public static class T1 implements Runnable {
    @Override
    public void run() {
      synchronized (MONITOR) {
        try {
          MONITOR.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      Console.log("线程 -> [{}] 结束", Thread.currentThread().getName());
    }
  }

  public static class T2 implements Runnable {
    @Override
    public void run() {
      synchronized (MONITOR) {
        ThreadUtil.sleep(5, TimeUnit.SECONDS);
        MONITOR.notify();
      }
      Console.log("线程 -> [{}] 结束", Thread.currentThread().getName());
    }
  }

  public static void main(String[] args) {
    new Thread(new T1(), "t1").start();
    new Thread(new T2(), "t2").start();

  }
}
