package indi.shui4.ch6.concurrenthashmap;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shui4
 * @since 1.0
 */
public class ConcurrentHashMapExample {

  @Test
  public void case1_reduce() {
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
    for (int i = 1; i <= 100; i++) {
      map.put(Integer.toString(i), i);
    }
    // reduceValues方法中的第一个参数表示并行度，表示一个并行任务可以处理的元素个数（估算值）。如果设置为Long.Max_VALUE，则表示完全禁用并行，设置为1则表示使用最大并行可能。
    Integer count = map.reduceValues(2, Integer::sum);
    System.out.println(count);
  }

  @Test
  public void case2_ifInsert() {
    ConcurrentHashMap<String, HeavyObject> map = new ConcurrentHashMap<>();
    HeavyObject obj1 = getOrCreate(map, "1");
    HeavyObject obj2 = getOrCreate_computeIfAbsent(map, "1");
  }

  private HeavyObject getOrCreate(ConcurrentHashMap<String, HeavyObject> map, String key) {
    HeavyObject value = map.get(key);
    if (value == null) {
      value = new HeavyObject();
      map.put(key, value);
    }
    return value;
  }

  private HeavyObject getOrCreate_computeIfAbsent(
      ConcurrentHashMap<String, HeavyObject> map, String key) {
    return map.computeIfAbsent(key, s -> new HeavyObject());
  }

  @Test
  public void case3_search() {
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
    map.search(
        2,
        (s, i) -> {
          if (i % 25 == 0) return i;
          return null;
        });
  }

  @Test
  public void case4_count_newKeySet() {
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
    for (int i = 0; i < 1000; i++) {
      map.put(String.valueOf(i), i);
    }
    System.out.println(map.mappingCount());

    ConcurrentHashMap.KeySetView<String, Integer> set = map.keySet();
  }

  public static class HeavyObject {
    public HeavyObject() {
      System.out.println("HeavyObject created");
    }
  }
}
