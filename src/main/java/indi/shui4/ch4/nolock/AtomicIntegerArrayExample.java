package indi.shui4.ch4.nolock;

import cn.hutool.json.JSONUtil;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author shui4
 * @since 1.0
 */
public class AtomicIntegerArrayExample {
  static AtomicIntegerArray arr = new AtomicIntegerArray(10);

  public static class AddThread implements Runnable {
    @Override
    public void run() {
      for (int i = 0; i < 10_000; i++) {
        arr.getAndIncrement(i % arr.length());
      }
    }
  }

  @SuppressWarnings("all")
  public static void main(String[] args) throws InterruptedException {
    Thread[] ts = new Thread[10];
    for (int i = 0; i < 10; i++) {
      ts[i] = new Thread(new AddThread());
    }

    for (int i = 0; i < 10; i++) {
      ts[i].start();
    }

    for (int i = 0; i < 10; i++) {
      ts[i].join();
    }
    System.out.println(arr);
  }
}
