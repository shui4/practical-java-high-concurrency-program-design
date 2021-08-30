package indi.shui4.ch5.parallel_mode_and_algorithm.future.jdk;

import java.util.concurrent.Callable;

/**
 * @author shui4
 * @since 1.0
 */
public class RealData implements Callable<String> {
  private String para;

  public RealData(String para) {
    this.para = para;
  }

  @Override
  public String call() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      sb.append(para);
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    return sb.toString();
  }
}
