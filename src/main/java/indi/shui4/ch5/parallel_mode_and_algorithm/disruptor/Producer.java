package indi.shui4.ch5.parallel_mode_and_algorithm.disruptor;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * @author shui4
 * @since 1.0
 */
public class Producer {

  private final RingBuffer<PCData> ringBuffer;

  public Producer(RingBuffer<PCData> ringBuffer) {
    this.ringBuffer = ringBuffer;
  }

  public void pushData(ByteBuffer byteBuffer) {

    long sequence = ringBuffer.next();
    try {
      PCData event = ringBuffer.get(sequence);
      event.setValue(byteBuffer.getLong(0));
    } finally {
      ringBuffer.publish(sequence);
    }
  }
}
