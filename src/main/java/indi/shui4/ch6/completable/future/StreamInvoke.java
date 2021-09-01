package indi.shui4.ch6.completable.future;

import cn.hutool.core.lang.Console;
import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 流式调用
 *
 * @author shui4
 * @since 1.0
 */
public class StreamInvoke {

  public static Integer calc(Integer para) {
    try {
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    int result = para * para;
    return result;
  }

  @SneakyThrows
  public static void main(String[] args) {
    CompletableFuture<Void> fu =
        CompletableFuture.supplyAsync(() -> calc(50))
            .thenApply(Object::toString)
            .thenAccept(System.out::println);
    fu.get();
  }
}
