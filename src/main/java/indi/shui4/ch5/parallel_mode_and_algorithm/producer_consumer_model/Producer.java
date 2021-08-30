package indi.shui4.ch5.parallel_mode_and_algorithm.producer_consumer_model;

import cn.hutool.core.lang.Console;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings("ReplacePseudorandomGenerator")
public class Producer implements Runnable {

  /** 休眠时间（单位秒） */
  private static final int SLEEP_TIME = 1_000;

  private static final AtomicInteger count = new AtomicInteger();
  private final BlockingQueue<PCData> queue;
  private volatile boolean isRunning = true;

  public Producer(BlockingQueue<PCData> queue) {
    this.queue = queue;
  }

  @Override
  public void run() {
    PCData data;
    Random r = new Random();
    Console.log("开始消费 id  -> [{}]", Thread.currentThread().getId());
    try {
      while (isRunning) {
        TimeUnit.MILLISECONDS.sleep(r.nextInt(SLEEP_TIME));
        data = new PCData(count.incrementAndGet());
        System.out.println("数据加入队列");
        if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
          System.out.println("加入数据失败" + data);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void stop() {
    this.isRunning = false;
  }
}
