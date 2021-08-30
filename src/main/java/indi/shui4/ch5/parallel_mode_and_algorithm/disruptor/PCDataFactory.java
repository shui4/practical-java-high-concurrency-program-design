package indi.shui4.ch5.parallel_mode_and_algorithm.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @author shui4
 * @since 1.0
 */
public class PCDataFactory  implements EventFactory<PCData> {
  @Override
  public PCData newInstance() {
    return new PCData();
  }
}
