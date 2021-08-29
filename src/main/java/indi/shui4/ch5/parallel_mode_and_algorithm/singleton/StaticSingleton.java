package indi.shui4.ch5.parallel_mode_and_algorithm.singleton;

import cn.hutool.core.thread.ThreadUtil;

/**
 * @author shui4
 * @since 1.0
 */
public class StaticSingleton {
  public static int STATUS = 1;

  private StaticSingleton() {
    System.out.println("StaticSingleton is create");
  }

  public static void main(String[] args) {
    // 不会初始化
    System.out.println(STATUS);
    for (int i = 0; i < 5; i++) {
      new Thread(
              () -> {
                ThreadUtil.sleep(100);
                System.out.println(getInstance());
              })
          .start();
    }
  }

  public static StaticSingleton getInstance() {
    return SingletonHolder.instance;
  }

  private static class SingletonHolder {
    @SuppressWarnings("InstantiationOfUtilityClass")
    private static StaticSingleton instance = new StaticSingleton();
  }
}
