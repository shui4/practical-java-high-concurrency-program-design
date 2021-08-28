package indi.shui4.ch3.juc.pool;

import cn.hutool.core.lang.Console;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author shui4 1078185271@qq.com
 * @since 1.0 2021/8/28
 */
public class GuavaPoolExample {
  @SuppressWarnings("UnstableApiUsage")
  public static void main(String[] args) {
    //            directExecutor();
    daemon();
  }

  private static void daemon() {
    ExecutorService executor =
        MoreExecutors.getExitingExecutorService(
            (ThreadPoolExecutor) Executors.newFixedThreadPool(2));
    executor.execute(
        () -> {
          Console.log("I am running in -> [{}]", Thread.currentThread().getName());
        });
  }

  private static void directExecutor() {
    Executor executor = MoreExecutors.directExecutor();
    executor.execute(
        () -> {
          Console.log("I am running in  -> [{}] ", Thread.currentThread().getName());
        });
  }
}
