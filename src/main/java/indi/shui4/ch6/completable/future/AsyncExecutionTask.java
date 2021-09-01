package indi.shui4.ch6.completable.future;

import cn.hutool.core.lang.Console;
import com.google.common.base.Stopwatch;
import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
public class AsyncExecutionTask {

  public static Integer calc(Integer para) {
    try {
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return para * para;
  }

  @SneakyThrows
  public static void main(String[] args) {
    CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> calc(50));
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(future.get());
    stopwatch.stop();
    Console.log("耗时 -> {}", stopwatch);
  }
}
