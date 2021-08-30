package indi.shui4.ch5.parallel_mode_and_algorithm.assembly_line;

import cn.hutool.core.util.StrUtil;

/**
 * @author shui4
 * @since 1.0
 */
public class PStreamMain {

  public static void main(String[] args) {
    new Thread(new Plus()).start();
    new Thread(new Multiplay()).start();
    new Thread(new Div()).start();
    for (int i = 0; i <= 1000; i++) {
      for (int j = 0; j <= 1000; j++) {
        Msg msg = new Msg();
        msg.i = i;
        msg.j = j;
        msg.orgStr = StrUtil.format("(({}+{}))*{}/2", i, j, i);
        Plus.bq.add(msg);
      }
    }
  }
}
