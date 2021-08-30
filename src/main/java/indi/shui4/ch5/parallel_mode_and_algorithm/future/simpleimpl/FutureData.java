package indi.shui4.ch5.parallel_mode_and_algorithm.future.simpleimpl;

/**
 * @author shui4
 * @since 1.0
 */
public class FutureData implements Data {

  protected RealData realData;

  protected boolean isReady = false;

  public synchronized void setRealData(RealData realData) {
    if (isReady) {
      return;
    }
    this.realData = realData;
    isReady = true;
    notifyAll();
  }

  @Override
  public synchronized String getResult() {
    while (!isReady) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return realData.result;
  }
}
