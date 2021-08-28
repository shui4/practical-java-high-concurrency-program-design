package indi.shui4.ch3.juc.jmh;

import org.joda.time.DateTime;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.sql.SQLOutput;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class DateBenchMark {

  @Benchmark
  public Calendar runCalendar() {
    return Calendar.getInstance();
  }

  @Benchmark
  public DateTime runJoda() {
    return new DateTime();
  }

  @Benchmark
  public long runSystem() {
    return System.currentTimeMillis();
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder().include(DateBenchMark.class.getSimpleName()).build();
    new Runner(opt).run();
  }
  
  
  
  
}
