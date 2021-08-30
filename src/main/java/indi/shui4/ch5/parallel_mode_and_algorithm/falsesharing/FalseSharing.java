package indi.shui4.ch5.parallel_mode_and_algorithm.falsesharing;

/**
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings({
})
public class FalseSharing implements Runnable {

  private static final long ITERATIONS = 500L * 1000L * 1000L;
  private static final int NUM_THREADS = 2;
  private static final VolatileLong[] longs = new VolatileLong[NUM_THREADS];

  static {
    for (int i = 0; i < longs.length; i++) {
      longs[i] = new VolatileLong();
    }
  }

  private final int arrayIndex;

  public FalseSharing(int arrayIndex) {
    this.arrayIndex = arrayIndex;
  }

  public static void main(String[] args) throws InterruptedException {
    final long start = System.currentTimeMillis();
    runTest();
    System.out.println("duration=" + (System.currentTimeMillis() - start));
  }

  private static void runTest() throws InterruptedException {
    Thread[] threads = new Thread[NUM_THREADS];
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Thread(new FalseSharing(i));
    }
    for (final Thread thread : threads) {
      thread.start();
    }

    for (final Thread thread : threads) {
      thread.join();
    }
  }

  @Override
  public void run() {
    long i = ITERATIONS + 1;
    while (0 != --i) {
      longs[arrayIndex].value = i;
    }
  }

  private static class VolatileLong {
    public volatile long value = 0L;
    private long p1, p2, p3, p4, p5, p6, p7;
  }
}
