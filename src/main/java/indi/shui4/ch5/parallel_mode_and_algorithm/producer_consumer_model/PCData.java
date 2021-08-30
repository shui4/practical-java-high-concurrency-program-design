package indi.shui4.ch5.parallel_mode_and_algorithm.producer_consumer_model;

/**
 * @author shui4
 * @since 1.0
 */
public class PCData {
  public int getData() {
    return data;
  }

  public PCData(String d) {
    data = Integer.parseInt(d);
  }

  private final int data;

  @Override
  public String toString() {
    return "PCData{" + "count=" + data + '}';
  }

  public PCData(int count) {
    this.data = count;
  }
}
