package indi.shui4.ch2.jichu;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.TimeUnit;

public class GoodSuspend {

  public static Object u = new Object();

  public static class ChangeObjectThread extends Thread {
    /** 挂起状态 */
    volatile boolean suspendMe = false;

    /** 挂起 */
    public void suspendMe() {
      suspendMe = true;
    }

    /** 继续 */
    public void resumeMe() {
      suspendMe = false;
      synchronized (this) {
        notify();
      }
    }

    @Override
    public void run() {
      while (true) {

        synchronized (this) {
          while (suspendMe) {
            try {
              wait();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
        synchronized (u) {
          System.out.println("in  ChangeObject Thread");
        }
        Thread.yield();
      }
    }
  }

  public static class ReadObjectThread extends Thread {

    @Override
    public void run() {
      while (true) {
        synchronized (u) {
          System.out.println("in ReadObjectThread");
        }
        Thread.yield();
      }
    }
  }

  public static void main(String[] args) {
    ChangeObjectThread t1 = new ChangeObjectThread();
    ReadObjectThread t2 = new ReadObjectThread();
    t1.start();
    t2.start();
    ThreadUtil.sleep(1, TimeUnit.SECONDS);
    // 挂起
    t1.suspendMe();
    System.out.println("suspend t1 2 sec");
    ThreadUtil.sleep(2, TimeUnit.SECONDS);
    System.out.println("resume t2");
    // 继续
    t1.resumeMe();
  }
}
