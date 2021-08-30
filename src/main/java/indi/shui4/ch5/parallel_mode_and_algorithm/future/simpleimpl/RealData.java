package indi.shui4.ch5.parallel_mode_and_algorithm.future.simpleimpl;

import javax.management.relation.RelationSupport;

/**
 * @author shui4
 * @since 1.0
 */
public class RealData implements Data {

  protected final String result;

  public RealData(String para) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      sb.append(para);
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    result = sb.toString();
  }

  @Override
  public String getResult() {
    return result;
  }
}
