package indi.shui4.ch5.parallel_mode_and_algorithm.assembly_line;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings("InfiniteLoopStatement")
public class Plus implements Runnable {

  public static BlockingQueue<Msg> bq = new LinkedBlockingDeque<>();

  @Override
  public void run() {
    try {
      while (true) {

        Msg msg = bq.take();
        msg.j = msg.i + msg.j;
        Multiplay.bq.add(msg);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
