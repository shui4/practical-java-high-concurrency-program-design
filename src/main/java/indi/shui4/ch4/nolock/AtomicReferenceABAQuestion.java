package indi.shui4.ch4.nolock;

import cn.hutool.core.lang.Console;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 遇到这种情况，使用{@link AtomicStampedReference}
 *
 * @author shui4
 * @since 1.0
 */
public class AtomicReferenceABAQuestion {

  static AtomicReference<Integer> money = new AtomicReference<>();

  public static void main(String[] args) {
    money.set(19);
    new Thread(
            () -> {
              for (int i = 0; i < 100; i++) {
                while (true) {
                  Integer m = money.get();
                  if (m > 10) {
                    System.out.println("大于10元");
                    if (money.compareAndSet(m, m - 10)) {
                      Console.log("成功消费10元，余额：{}", money.get());
                      break;
                    }
                  } else {
                    System.out.println("没有足够的金额");
                    break;
                  }
                }
                try {
                  Thread.sleep(100);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            })
        .start();
    for (int i = 0; i < 3; i++) {
      new Thread(
              () -> {
                while (true) {
                  Integer m = money.get();
                  if (m < 20) {
                    if (money.compareAndSet(m, m + 20)) {
                      Console.log("余额小于20元，充值成功，余额：{}元", money.get());
                      break;
                    }
                  } else {
                    System.out.println("余额大于20元，无需充值");
                    break;
                  }
                }
              })
          .start();
    }
  }
}
