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

  ConcurrentLinkedQueue<Object> bigConcurrentLinkedQueue = new ConcurrentLinkedQueue<>();
  CopyOnWriteArrayList<Object> bigCopyOnWriteArrayList = new CopyOnWriteArrayList<>();
  ConcurrentLinkedQueue<Object> smallConcurrentLinkedQueue = new ConcurrentLinkedQueue<>();
  CopyOnWriteArrayList<Object> smallCopyOnWriteArrayList = new CopyOnWriteArrayList<>();

  @Benchmark
  public void concurrentListGet() {
    smallConcurrentLinkedQueue.peek();
  }

  @Benchmark
  public void concurrentListSize() {
    smallConcurrentLinkedQueue.size();
  }

  @Benchmark
  public void copyOnWriteGet() {
    smallCopyOnWriteArrayList.get(0);
  }

  @Benchmark
  public void copyOnWriteSize() {
    smallCopyOnWriteArrayList.size();
  }

  @Setup
  public void setup() {
    for (int i = 0; i < 10; i++) {
      smallCopyOnWriteArrayList.add(new Object());
      smallConcurrentLinkedQueue.add(new Object());
    }
    for (int i = 0; i < 1000; i++) {
      bigCopyOnWriteArrayList.add(new Object());
      bigConcurrentLinkedQueue.add(new Object());
    }
  }

  @Benchmark
  public void smallCopyOnWrite() {
    smallCopyOnWriteArrayList.add(new Object());
    smallCopyOnWriteArrayList.remove(0);
  }

  @Benchmark
  public void smallConcurrentListWrite() {
    smallConcurrentLinkedQueue.add(new Object());
    smallConcurrentLinkedQueue.remove(0);
  }

  @Benchmark
  public void bigCopyOnWrite() {
    bigCopyOnWriteArrayList.add(new Object());
    bigCopyOnWriteArrayList.remove(0);
  }

  @Benchmark
  public void bigConcurrentListWrite() {
    bigConcurrentLinkedQueue.add(new Object());
    bigConcurrentLinkedQueue.remove(0);
  }
}
