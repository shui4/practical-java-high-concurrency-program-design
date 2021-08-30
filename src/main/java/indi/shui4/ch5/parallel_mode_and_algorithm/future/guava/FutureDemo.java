package indi.shui4.ch5.parallel_mode_and_algorithm.future.guava;

import com.google.common.util.concurrent.*;
import indi.shui4.ch5.parallel_mode_and_algorithm.future.jdk.RealData;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings({"NumericOverflow", "divzero", "NullableProblems"})
public class FutureDemo {

  public static void main(String[] args) {
    ListeningExecutorService service =
        MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    // 正常处理
    //    ListenableFuture<String> task = service.submit(new RealData("x"));
    // 失败处理
    ListenableFuture<String> task =
        service.submit(
            () -> {
              int i = 1 / 0;
              return "x";
            });
    //    task.addListener(getRunnable(task), MoreExecutors.directExecutor());
    Futures.addCallback(
        task,
        new FutureCallback<String>() {
          @Override
          public void onSuccess(@Nullable String result) {
            System.out.println("异步处理成功，result=" + result);
          }

          @Override
          public void onFailure(Throwable t) {
            System.out.println("异步处理失败,e=" + t);
          }
        },
        MoreExecutors.directExecutor());
    System.out.println("hi");
  }

  private static Runnable getRunnable(ListenableFuture<String> task) {
    return () -> {
      System.out.println("异步处理完成：");
      try {
        System.out.println(task.get());
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    };
  }
}
