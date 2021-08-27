package indi.shui4.ch2.jichu;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.TimeUnit;

public class StopThreadExample {

  public static void main(String[] args) {
    Thread thread =
        new Thread(
            () -> {
              System.out.println("start");
              ThreadUtil.sleep(10, TimeUnit.SECONDS);
              System.out.println("end");
            });
    thread.stop();
  }
}
