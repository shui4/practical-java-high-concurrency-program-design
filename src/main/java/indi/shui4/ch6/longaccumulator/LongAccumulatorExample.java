package indi.shui4.ch6.longaccumulator;

import lombok.SneakyThrows;

import java.util.Random;
import java.util.concurrent.atomic.LongAccumulator;

/**
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings("all")
public class LongAccumulatorExample {
  @SneakyThrows
  public static void main(String[] args) {
    LongAccumulator accumulator = new LongAccumulator(Long::max, Long.MIN_VALUE);
    Thread[] ts = new Thread[1000];
    for (int i = 0; i < 1000; i++) {
      Thread thread =
          new Thread(
              () -> {
                Random random = new Random();
                long value = random.nextLong();
                // +=
                accumulator.accumulate(value);
              });
      ts[i] = thread;
      thread.start();
    }

    for (int i = 0; i < 1000; i++) {
      ts[i].join();
    }
    System.out.println(accumulator.longValue());
  }
}
