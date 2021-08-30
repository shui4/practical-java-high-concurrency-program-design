package indi.shui4.ch5.parallel_mode_and_algorithm.disruptor;

import cn.hutool.core.lang.Console;
import com.lmax.disruptor.WorkHandler;

/**
 * @author shui4
 * @since 1.0
 */
public class Consumer implements WorkHandler<PCData> {
  @Override
  public void onEvent(PCData event) {
    long result = event.getValue() * event.getValue();
    Console.log("{}:Event:--{}--", Thread.currentThread().getId(), result);
  }
}
