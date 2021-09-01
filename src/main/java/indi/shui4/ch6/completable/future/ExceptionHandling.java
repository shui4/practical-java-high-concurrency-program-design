package indi.shui4.ch6.completable.future;

import java.util.concurrent.CompletableFuture;

/**
 * 异常处理
 *
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings("all")
public class ExceptionHandling {

  public static Integer calc(Integer para) {
    return para / 0;
  }

  public static void main(String[] args) {
    CompletableFuture.supplyAsync(() -> calc(50))
        .exceptionally(
            ex -> {
              System.out.println(ex);
              return 0;
            })
        .thenApply(Object::toString)
        .thenApply(s -> "\"" + s + "\"")
        .thenAccept(System.out::println);
  }
}
