package indi.shui4.ch5.parallel_mode_and_algorithm.immutable;

/**
 * @author shui4
 * @since 1.0
 */
public final class Product1 {
  private final String name;
  private final String no;
  private final double price;

  public Product1(String no, String name, double price) {
    this.no = no;
    this.name = name;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public String getNo() {
    return no;
  }

  public double getPrice() {
    return price;
  }
}
