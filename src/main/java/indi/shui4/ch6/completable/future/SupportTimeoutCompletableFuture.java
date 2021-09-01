package indi.shui4.ch6.completable.future;

import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
public class SupportTimeoutCompletableFuture {

  @SneakyThrows
  public static void main(String[] args) {

    /* CompletableFuture<Integer> integerCompletableFuture = CompletableFuture
    .supplyAsync(
        () -> {
          try {
            TimeUnit.SECONDS.sleep(2);
          } catch (InterruptedException e) {
          }
          return calc(50);
        })
    //jdk9
    .orTimeout(1, TimeUnit.SECONDS);*/

    CompletableFuture<Integer> future =
        CompletableFuture.supplyAsync(
            () -> {
              try {
                TimeUnit.SECONDS.sleep(2);
              } catch (InterruptedException e) {
              }
              return calc(50);
            });
    future.get(1, TimeUnit.SECONDS);
  }

  public static Integer calc(Integer para) {
    return para / 2;
  }
}
