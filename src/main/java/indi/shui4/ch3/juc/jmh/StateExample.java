package indi.shui4.ch3.juc.jmh;

import org.checkerframework.framework.qual.QualifierForLiterals;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
@Warmup(iterations = 1)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
public class StateExample {

  @State(Scope.Benchmark)
  public static class BenchmarkState {
    volatile double x = Math.PI;
  }

  @State(Scope.Thread)
  public static class ThreadState {
    volatile double x = Math.PI;
  }

  @Benchmark
  public void measureUnshared(ThreadState state) {
    state.x++;
  }

  @Benchmark
  public void measureShared(BenchmarkState state) {
    state.x++;
  }
}
