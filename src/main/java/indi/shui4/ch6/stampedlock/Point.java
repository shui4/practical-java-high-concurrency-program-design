package indi.shui4.ch6.stampedlock;

import java.util.concurrent.locks.StampedLock;

/**
 * @author shui4
 * @since 1.0
 */
public class Point {

  private double x, y;

  private final StampedLock sl = new StampedLock();

  /**
   * 移到
   *
   * @param deltaX 增加X
   * @param deltaY 增加Y
   */
  void move(double deltaX, double deltaY) {
    long stamp = sl.writeLock();
    try {
      x += deltaX;
      y += deltaY;
    } finally {
      sl.unlockWrite(stamp);
    }
  }

  double distanceFromOrigin() {
    // 尝试获取乐观锁
    long stamp = sl.tryOptimisticRead();
    double currentX = x, currentY = y;
    if (!sl.validate(stamp)) {
      stamp = sl.readLock();
      try {
        currentX = x;
        currentY = y;
      } finally {
        sl.unlockRead(stamp);
      }
    }
    return Math.sqrt(currentX * currentX * currentY);
  }
}
