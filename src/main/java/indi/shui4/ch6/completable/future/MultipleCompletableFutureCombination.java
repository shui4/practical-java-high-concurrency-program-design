package indi.shui4.ch6.completable.future;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

/**
 * 组合多个 {@link CompletableFuture}
 *
 * @author shui4
 * @since 1.0
 */
public class MultipleCompletableFutureCombination {

  @SneakyThrows
  @Test
  public void case1_thenCompose() {
    CompletableFuture<Void> future =
        CompletableFuture.supplyAsync(() -> calc(50))
            .thenCompose(integer -> CompletableFuture.supplyAsync(() -> calc(integer)))
            .thenApply(integer -> "\"" + integer + "\"")
            .thenAccept(System.out::println);
    future.get();
  }

  public static Integer calc(Integer para) {
    return para / 2;
  }

  @SneakyThrows
  @Test
  public void case2_thenCombine() {
    CompletableFuture<Integer> intFuture = CompletableFuture.supplyAsync(() -> calc(50));
    CompletableFuture<Integer> intFuture2 = CompletableFuture.supplyAsync(() -> calc(50));
    CompletableFuture<Void> fu =
        intFuture
            .thenCombine(intFuture2, (integer, integer2) -> integer + integer2)
            .thenApply(integer -> "\"" + integer + "\"")
            .thenAccept(System.out::println);
    fu.get();
  }
}
