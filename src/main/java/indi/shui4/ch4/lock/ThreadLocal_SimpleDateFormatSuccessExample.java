package indi.shui4.ch4.lock;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.NumberUtil;
import com.google.common.base.Stopwatch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
public class ThreadLocal_SimpleDateFormatSuccessExample {

  private static final ThreadLocal<SimpleDateFormat> t1 = new ThreadLocal<>();

  public static class ParseDate implements Runnable {
    int i;

    public ParseDate(int i) {
      this.i = i;
    }

    @Override
    public void run() {
      try {
        SimpleDateFormat sdf = t1.get();
        if (sdf == null) {
          sdf = new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
          t1.set(sdf);
        }
        Date t = sdf.parse("2015-03-29 19:29:" + NumberUtil.decimalFormat("00", i % 60));
        System.out.println(i + ":" + t);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static void main(String[] args) throws InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    for (int i = 0; i < 1000; i++) {
      executorService.execute(new ParseDate(i));
    }
    executorService.shutdown();
    Stopwatch stopwatch = Stopwatch.createStarted();
    executorService.awaitTermination(10, TimeUnit.SECONDS);
    stopwatch.stop();
    System.out.println(stopwatch.elapsed(TimeUnit.NANOSECONDS));
  }
}
