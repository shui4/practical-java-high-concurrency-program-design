package indi.shui4.ch5.parallel_mode_and_algorithm.producer_consumer_model;

import cn.hutool.core.lang.Console;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings({"ReplacePseudorandomGenerator", "BusyWait", "InfiniteLoopStatement"})
public class Consumer implements Runnable {

  private static final int SLEEP_TIME = 1_000;
  private final BlockingQueue<PCData> queue;

  public Consumer(BlockingQueue<PCData> queue) {
    this.queue = queue;
  }

  @Override
  public void run() {
    Console.log("开始消费，消费者 id  -> [{}]", Thread.currentThread().getId());
    Random random = new Random();

    try {
      while (true) {
        PCData data = queue.take();
        int data1 = data.getData();
        int re = data1 * data1;
        Console.log("{}*{}={}", data1, data1, re);
        Thread.sleep(random.nextInt(SLEEP_TIME));
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
