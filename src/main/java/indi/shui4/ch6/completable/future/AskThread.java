package indi.shui4.ch6.completable.future;

import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
public class AskThread implements Runnable {

  public AskThread(CompletableFuture<Integer> re) {
    this.re = re;
  }

  private final CompletableFuture<Integer> re;

  @Override
  public void run() {
    int myRe = 0;
    try {
      myRe = re.get() * re.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    System.out.println(myRe);
  }

  @SneakyThrows
  public static void main(String[] args) {
    CompletableFuture<Integer> future = new CompletableFuture<>();
    new Thread(new AskThread(future)).start();
    TimeUnit.SECONDS.sleep(3);
    future.complete(60);
  }
}
