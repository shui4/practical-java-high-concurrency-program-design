package indi.shui4.ch4.lock;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.NumberUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
public class ThreadLocalDemo_Gc {
  static volatile ThreadLocal<SimpleDateFormat> t1 =
      new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected void finalize() {
          System.out.println(this + " is gc");
        }
      };

  static volatile CountDownLatch cd = new CountDownLatch(10_000);

  public static class ParseDate implements Runnable {
    int i;

    public ParseDate(int i) {
      this.i = i;
    }

    @Override
    public void run() {
      SimpleDateFormat sdf = t1.get();
      if (sdf == null) {
        sdf =
            new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN) {
              @Override
              protected void finalize() {
                System.out.println(this + " is gc");
              }
            };
        t1.set(sdf);
        System.out.println(Thread.currentThread().getId() + ":create SimpleDateFormat");
      }

      try {
        sdf.parse("2015-03-29 19:29:" + NumberUtil.decimalFormat("00", i % 60));
      } catch (ParseException e) {
        e.printStackTrace();
      } finally {
        cd.countDown();
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    ExecutorService es = Executors.newFixedThreadPool(10);
    for (int i = 0; i < 10_000; i++) {
      es.execute(new ParseDate(i));
    }
    cd.await();
    System.out.println("任务完成！！");
    t1 = null;
    System.gc();
    System.out.println("第1次GC完成");
    t1 = new ThreadLocal<>();
    cd = new CountDownLatch(10_000);
    for (int i = 0; i < 10_000; i++) {
      es.execute(new ParseDate(i));
    }
    cd.await();
    TimeUnit.SECONDS.sleep(1);
    
    System.gc();

    System.out.println("第2次GC完成");
  }
}
