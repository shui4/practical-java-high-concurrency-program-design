package indi.shui4.ch5.parallel_mode_and_algorithm.net.nio;

import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * @author shui4
 * @since 1.0
 */
public class EchoClient {

  private LinkedList<ByteBuffer> outputQueue;

  public EchoClient() {
    outputQueue = new LinkedList<>();
  }

  public void enqueue(ByteBuffer bb) {
    outputQueue.addFirst(bb);
  }

  public LinkedList<ByteBuffer> getOutputQueue() {
    return outputQueue;
  }
}
