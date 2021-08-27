package indi.shui4.ch2.jichu;

/**
 * 挂起和继续执行存在问题，参考：{@link GoodSuspend}
 *
 */
public class SuspendAndResumeExample {

  public static Object u = new Object();

  static ChangeObjectThread t1 = new ChangeObjectThread("t1");
  static ChangeObjectThread t2 = new ChangeObjectThread("t2");

  public static void main(String[] args) throws InterruptedException {
    t1.start();
    Thread.sleep(100);
    t2.start();
    t1.resume();
    t2.resume();
    t1.join();
    t2.join();
  }

  private static class ChangeObjectThread extends Thread {
    public ChangeObjectThread(String name) {
      super(name);
    }

    @Override
    public void run() {
      synchronized (u) {
        System.out.println("in " + getName());
        Thread.currentThread().suspend();
      }
    }
  }
}
