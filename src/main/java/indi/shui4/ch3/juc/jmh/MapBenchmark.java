package indi.shui4.ch3.juc.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
@BenchmarkMode(Mode.Throughput)
@Fork(1)
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class MapBenchmark {

  static Map<String, String> concurrentHashMap = new ConcurrentHashMap<>();
  static Map<String, String> hashMap = new HashMap<>();
  static Map<String, String> syncHashMap = Collections.synchronizedMap(new HashMap<>());

  @Benchmark
  public void concurrentHashMapGet() {
    get(concurrentHashMap);
  }

  private static void get(Map<String, String> map) {
    map.get("4");
  }

  @Benchmark
  public void concurrentHashMapSize() {
    size(concurrentHashMap);
  }

  private static void size(Map<String, String> map) {
    map.size();
  }

  @Benchmark
  public void hashMapGet() {
    get(hashMap);
  }

  @Benchmark
  public void hashMapSize() {
    size(hashMap);
  }

  @Setup
  public void setup() {
    for (int i = 0; i < 10_000; i++) {
      String value = Integer.toString(i);
      hashMap.put(value, value);
      syncHashMap.put(value, value);
      concurrentHashMap.put(value, value);
    }
  }

  @Benchmark
  public void syncHashMapGet() {
    get(syncHashMap);
  }

  @Benchmark
  public void syncHashMapSize() {
    size(syncHashMap);
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder().include(MapBenchmark.class.getSimpleName()).build();
    new Runner(opt).run();
  }
}
