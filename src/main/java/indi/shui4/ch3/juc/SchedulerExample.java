package indi.shui4.ch3.juc;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerExample {

  public static void main(String[] args) {
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
    System.out.println(LocalDateTime.now());
    scheduledExecutorService.scheduleAtFixedRate(
        () -> {
          try {
            TimeUnit.SECONDS.sleep(1);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          System.out.println(LocalDateTime.now());
        },
        0,
        2,
        TimeUnit.SECONDS);
  }
}
