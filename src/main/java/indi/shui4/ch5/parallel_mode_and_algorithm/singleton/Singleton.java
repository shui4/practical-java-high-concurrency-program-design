package indi.shui4.ch5.parallel_mode_and_algorithm.singleton;

import org.junit.jupiter.api.Test;

/**
 * @author shui4
 * @since 1.0
 */
public class Singleton {
  public static int STATUS = 1;

  private Singleton() {
    System.out.println("Singleton is create");
  }

  private static final Singleton instance = new Singleton();

  public static Singleton getInstance() {
    return instance;
  }

  @Test
  public void test1() {
    System.out.println(STATUS);
  }

  public static void main(String[] args) {
    // 调用的是类的其它变量造成 instance 被加载
    System.out.println(STATUS);
  }
}
