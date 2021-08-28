package indi.shui4.ch3.juc.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JMhSample_01_HelloWorld {

  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public void measureThroughput() throws InterruptedException {
    TimeUnit.MILLISECONDS.sleep(100);
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public void measureAvgTime() throws InterruptedException {
    TimeUnit.MILLISECONDS.sleep(100);
  }

  @Benchmark
  @BenchmarkMode(Mode.SampleTime)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public void measureSampleTime() throws InterruptedException {
    TimeUnit.MILLISECONDS.sleep(100);
  }

  public static void main(String[] args) throws RunnerException {

    Options opt =
        new OptionsBuilder()
            .warmupIterations(2)
            .include(JMhSample_01_HelloWorld.class.getSimpleName())
            .forks(1)
            .build();
    new Runner(opt).run();
  }
}
