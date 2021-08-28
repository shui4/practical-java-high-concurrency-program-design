package indi.shui4.ch3.juc.jmh;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 可以看到，在并发条件下，写的性能远远低于读的性能。而对于CopyOnWriteArrayList类来说，
 * 当内部存有1000个元素的时候，由于复制的成本，写性能要远远低于只包含少数元素的List，
 * 但依然优于ConcurrentLinkedQueue类。就读的性能而言，进行只读不写的Get操作，两者性能都不错。
 * 但是由于实现上的差异，ConcurrentLinkedQueue类的size操作明显要慢于CopyOnWriteArrayList类的。
 * 因此，可以得出结论，即便有少许的写入，在并发场景下，复制的消耗依然相对较小，当元素总量不大时，在绝大部分场景中，
 * CopyOnWriteArrayList类要优于ConcurrentLinkedQueue类。
 *
 * @author shui4
 * @since 1.0
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class ListBenchmark {

  ConcurrentLinkedQueue<Object> bigConcurrentList = new ConcurrentLinkedQueue<>();
  CopyOnWriteArrayList<Object> bigCopyOnWriteList = new CopyOnWriteArrayList<>();
  ConcurrentLinkedQueue<Object> smallConcurrentList = new ConcurrentLinkedQueue<>();
  CopyOnWriteArrayList<Object> smallCopyOnWriteList = new CopyOnWriteArrayList<>();

  @Benchmark
  public void concurrentListGet() {
    smallConcurrentList.peek();
  }

  @Benchmark
  public void concurrentListSize() {
    smallConcurrentList.size();
  }

  @Benchmark
  public void copyOnWriteGet() {
    smallCopyOnWriteList.get(0);
  }

  @Benchmark
  public void copyOnWriteSize() {
    smallCopyOnWriteList.size();
  }

  @Setup
  public void setup() {
    for (int i = 0; i < 10; i++) {
      smallCopyOnWriteList.add(new Object());
      smallConcurrentList.add(new Object());
    }
    for (int i = 0; i < 1000; i++) {
      bigCopyOnWriteList.add(new Object());
      bigConcurrentList.add(new Object());
    }
  }

  @Benchmark
  public void smallCopyOnWriteWrite() {
    smallCopyOnWriteList.add(new Object());
    smallCopyOnWriteList.remove(0);
  }

  @Benchmark
  public void smallConcurrentListWrite() {
    smallConcurrentList.add(new Object());
    smallConcurrentList.remove(0);
  }

  @Benchmark
  public void bigCopyOnWrite() {
    bigCopyOnWriteList.add(new Object());
    bigCopyOnWriteList.remove(0);
  }

  @Benchmark
  public void bigConcurrentListWrite() {
    bigConcurrentList.add(new Object());
    bigConcurrentList.remove(0);
  }
}
