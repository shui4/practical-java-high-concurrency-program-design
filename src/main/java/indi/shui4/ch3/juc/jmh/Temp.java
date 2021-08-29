package indi.shui4.ch3.juc.jmh;

import java.util.Vector;

/**
 * @author shui4
 * @since 1.0
 */
public class Temp {

  public String[] createStrings() {
    Vector<String> v = new Vector<>();
    for (int i = 0; i < 100; i++) {
      v.add(Integer.toString(i));
    }
    return v.toArray(new String[0]);
  }
}
