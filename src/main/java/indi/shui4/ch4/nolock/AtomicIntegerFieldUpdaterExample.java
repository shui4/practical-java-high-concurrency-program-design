package indi.shui4.ch4.nolock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author shui4
 * @since 1.0
 */
public class AtomicIntegerFieldUpdaterExample {

  /** 候选人 */
  private static class Candidate {
    int id;
    /**
     * 使用FiledUpdater需要满足以下条件 <br>
     * <li>必须是volatile，否则报错：IllegalArgumentException: Must be volatile type
     * <li>不能是私有的
     * <li>不能是类变量（static）
     */
    public volatile int score;
  }

  private static final AtomicIntegerFieldUpdater<Candidate> scoreUpdater =
      AtomicIntegerFieldUpdater.newUpdater(Candidate.class, "score");

  private static AtomicInteger allScore = new AtomicInteger(0);

  public static void main(String[] args) throws InterruptedException {
    Candidate candidate = new Candidate();
    Thread[] ts = new Thread[10_000];
    for (int i = 0; i < 10_000; i++) {
      ts[i] =
          new Thread(
              () -> {
                if (Math.random() > 0.4) {
                  scoreUpdater.incrementAndGet(candidate);
                  allScore.incrementAndGet();
                }
              });
      ts[i].start();
    }

    for (int i = 0; i < 10_000; i++) {
      ts[i].join();
    }

    System.out.println("score=" + candidate.score);
    System.out.println("allScore=" + allScore);
  }
}
