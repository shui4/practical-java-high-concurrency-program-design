package indi.shui4.ch5.parallel_mode_and_algorithm.producer_consumer_model;

import java.util.concurrent.*;

/**
 * @author shui4
 * @since 1.0
 */
public class Main {
  public static void main(String[] args) throws InterruptedException {
    BlockingQueue<PCData> queue = new LinkedBlockingQueue<>(10);
    Producer p1 = new Producer(queue);
    Producer p2 = new Producer(queue);
    Producer p3 = new Producer(queue);

    Consumer c1 = new Consumer(queue);
    Consumer c2 = new Consumer(queue);
    Consumer c3 = new Consumer(queue);

    ExecutorService service = Executors.newCachedThreadPool();
    service.execute(p1);
    service.execute(p2);
    service.execute(p3);
    service.execute(c1);
    service.execute(c2);
    service.execute(c3);
    TimeUnit.SECONDS.sleep(10);
    p1.stop();
    p2.stop();
    p3.stop();
    System.out.println("生产者已全部关闭");
    TimeUnit.SECONDS.sleep(3);
    service.shutdown();
    System.out.println("线程池关闭");
  }
}
