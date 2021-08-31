package indi.shui4.ch5.parallel_mode_and_algorithm.sort;

import java.util.concurrent.CountDownLatch;

/**
 * 奇偶交换
 *
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public class ParitySwapExample {

  static int exchFlag = 1;

  /**
   * 冒泡排序
   *
   * @param arr the arr
   */
  public static void bubbleSort(int[] arr) {
    for (int i = arr.length - 1; i > 0; i--) {
      for (int j = 0; j < i; j++) {
        if (arr[j] > arr[j + 1]) {
          int temp = arr[j];
          arr[j] = arr[j + 1];
          arr[j + 1] = temp;
        }
      }
    }
  }

  public static void oddEventSort(int[] array) {
    int exchFlag = 1, start = 0;

    while (exchFlag == 1 || start == 1) {
      exchFlag = 0;
      for (int i = start; i < array.length - 1; i += 2) {
        if (array[i] > array[i + 1]) {
          int temp = array[i];
          array[i] = array[i + 1];
          array[i + 1] = temp;
          exchFlag = 1;
        }
      }

      if (start == 0) {
        start = 1;
      } else {
        start = 0;
      }
    }
  }

  static synchronized int getExchFlag() {
    return exchFlag;
  }

  static synchronized void setExchFlag(int v) {
    exchFlag = v;
  }

  static int[] arr;

  public static class OddEvenSortTask implements Runnable {
    int i;
    CountDownLatch latch;

    public OddEvenSortTask(int i, CountDownLatch latch) {
      this.i = i;
      this.latch = latch;
    }

    @Override
    public void run() {
      if (arr[i] > arr[i + 1]) {
        int temp = arr[i];
        arr[i] = arr[i + 1];
        arr[i + 1] = temp;
        setExchFlag(1);
      }
      latch.countDown();
    }
  }
  
  
  
  
}
