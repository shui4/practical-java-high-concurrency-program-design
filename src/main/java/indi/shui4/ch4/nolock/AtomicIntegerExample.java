package indi.shui4.ch4.nolock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shui4
 * @since 1.0
 */
public class AtomicIntegerExample {

  static AtomicInteger i = new AtomicInteger();

  public static class AddThread implements Runnable {
    @Override
    public void run() {
      for (int i1 = 0; i1 < 10_000; i1++) {
        i.incrementAndGet();
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    Thread[] threads = new Thread[10];
    for (int k = 0; k < 10; k++) {
      threads[k] = new Thread(new AddThread());
    }

    for (int i1 = 0; i1 < 10; i1++) {
      threads[i1].start();
    }
    for (int i1 = 0; i1 < 10; i1++) {
      threads[i1].join();
    }
    System.out.println(i);
  }
}
