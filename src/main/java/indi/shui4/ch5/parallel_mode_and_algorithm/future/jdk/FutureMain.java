package indi.shui4.ch5.parallel_mode_and_algorithm.future.jdk;

import cn.hutool.core.date.StopWatch;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author shui4
 * @since 1.0
 */
public class FutureMain {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    FutureTask<String> future = new FutureTask<>(new RealData("a"));
    ExecutorService executor = Executors.newFixedThreadPool(1);
    executor.submit(future);
    StopWatch stopWatch = StopWatch.create("main");
    stopWatch.start();
    System.out.println("数据=" + future.get());
    stopWatch.stop();
    System.out.println("耗时=" + stopWatch.getTotalTimeMillis());
  }
}
