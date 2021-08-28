package indi.shui4.ch3.juc.collection;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author shui4
 * @since 1.0
 */
public class ConcurrentLinkedQueueExample {
  public static void main(String[] args) {
    ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
    queue.offer("1");
    queue.offer("2");
    System.out.println(queue.poll());
  }
}
