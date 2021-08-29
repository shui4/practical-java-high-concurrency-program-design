package indi.shui4.ch5.parallel_mode_and_algorithm.singleton;

import cn.hutool.core.thread.ThreadUtil;

/**
 * @author shui4
 * @since 1.0
 */
public class LazySingleton {

  private static volatile LazySingleton instance = null;

  private LazySingleton() {
    System.out.println("LazySingleton is create");
  }

  @SuppressWarnings("all")
  public static LazySingleton getInstance() {
    if (instance == null) {
      synchronized (LazySingleton.class) {
        if (instance == null) {
          instance = new LazySingleton();
        }
      }
    }
    return instance;
  }

  public static void main(String[] args) {
    for (int i = 0; i < 5; i++) {
      new Thread(
              () -> {
                ThreadUtil.sleep(100);
                System.out.println(getInstance());
              })
          .start();
    }
  }
}
