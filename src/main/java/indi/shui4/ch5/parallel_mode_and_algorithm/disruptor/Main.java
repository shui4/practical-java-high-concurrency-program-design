package indi.shui4.ch5.parallel_mode_and_algorithm.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings({"deprecation", "InfiniteLoopStatement", "BusyWait"})
public class Main {
  public static void main(String[] args) throws InterruptedException {
    ExecutorService executor = Executors.newCachedThreadPool();
    PCDataFactory factory = new PCDataFactory();
    Disruptor<PCData> disruptor =
        new Disruptor<>(factory, 1024, executor, ProducerType.MULTI, new BlockingWaitStrategy());
    disruptor.handleEventsWithWorkerPool(
        new Consumer(), new Consumer(), new Consumer(), new Consumer());
    disruptor.start();
    RingBuffer<PCData> ringBuffer = disruptor.getRingBuffer();
    Producer producer = new Producer(ringBuffer);
    ByteBuffer bb = ByteBuffer.allocate(8);
    for (long l = 0; true; l++) {
      bb.putLong(0, l);
      producer.pushData(bb);
      Thread.sleep(100);
      System.out.println("add data " + l);
    }
  }
}
