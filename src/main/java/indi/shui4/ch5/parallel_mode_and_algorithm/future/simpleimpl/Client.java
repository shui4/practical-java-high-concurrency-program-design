package indi.shui4.ch5.parallel_mode_and_algorithm.future.simpleimpl;

/**
 * @author shui4
 * @since 1.0
 */
public class Client {

  public Data request(final String queryStr) {
    FutureData future = new FutureData();
    new Thread(
            () -> {
              RealData realData = new RealData(queryStr);
              future.setRealData(realData);
            })
        .start();
    return future;
  }

  public static void main(String[] args) {
    Client client = new Client();
    Data data = client.request("name");
    System.out.println("请求完毕");
    System.out.println("数据=" + data.getResult());
  }
}
