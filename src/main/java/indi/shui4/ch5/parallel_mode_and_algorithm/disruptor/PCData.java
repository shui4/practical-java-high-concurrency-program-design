package indi.shui4.ch5.parallel_mode_and_algorithm.disruptor;

/**
 * @author shui4
 * @since 1.0
 */
public class PCData {
  private long value;

  public PCData() {}

  public PCData(long value) {
    this.value = value;
  }

  public long getValue() {
    return value;
  }

  public void setValue(long value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "PCData{" + "value=" + value + '}';
  }
}
