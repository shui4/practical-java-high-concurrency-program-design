package indi.shui4.ch5.parallel_mode_and_algorithm.search;

import cn.hutool.core.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shui4
 * @since 1.0
 */
public class SearchExample {

  static final int THREAD_NUM = 2;
  static int[] arr;
  static ExecutorService pool = Executors.newCachedThreadPool();
  static AtomicInteger result = new AtomicInteger(-1);

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    arrInitialization();
    int i = pSearch(999);
    System.out.println("下标:" + i);
    System.out.println(arr[i]);
  }

  private static void arrInitialization() {
    arr = new int[10_000];
    for (int i = 0; i < 10_000 - 2; i++) {
      int randomInt = RandomUtil.randomInt(99999);
      arr[i] = randomInt;
    }
    arr[10_000 - 1] = 999;
  }

  public static int search(int searchValue, int beginPos, int endPos) {
    for (int i = beginPos; i < endPos; i++) {
      if (result.get() >= 0) {
        return result.get();
      }

      if (arr[i] == searchValue) {
        if (result.compareAndSet(-1, i)) {
          return result.get();
        }
        return i;
      }
    }

    return -1;
  }

  public static class SearchTask implements Callable<Integer> {
    int begin, end, searchValue;

    public SearchTask(int searchValue, int begin, int end) {
      this.begin = begin;
      this.end = end;
      this.searchValue = searchValue;
    }

    @Override
    public Integer call() {
      return search(searchValue, begin, end);
    }
  }

  public static int pSearch(int searchValue) throws ExecutionException, InterruptedException {
    int subArraySize = arr.length / THREAD_NUM + 1;
    List<Future<Integer>> re = new ArrayList<>();
    for (int i = 0; i < arr.length; i++) {
      int end = i + subArraySize;
      if (end >= arr.length) end = arr.length;
      re.add(pool.submit(new SearchTask(searchValue, i, end)));
    }
    for (final Future<Integer> fu : re) {
      if (fu.get() >= 0) return fu.get();
    }
    pool.shutdown();
    return -1;
  }
}
