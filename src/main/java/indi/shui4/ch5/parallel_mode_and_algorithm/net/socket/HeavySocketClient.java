package indi.shui4.ch5.parallel_mode_and_algorithm.net.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.WildcardType;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

/**
 * @author shui4
 * @since 1.0
 */
public class HeavySocketClient {
  private static final int SLEEP_TIME = 1000 * 1000 * 1000;
  private static ExecutorService tp = Executors.newCachedThreadPool();

  public static class EchoClient implements Runnable {

    @Override
    public void run() {
      try (Socket client = new Socket(InetAddress.getByName("localhost"), 8000);
          PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
          BufferedReader reader =
              new BufferedReader(new InputStreamReader(client.getInputStream()))) {
        writer.print("H");
        LockSupport.parkNanos(SLEEP_TIME);
        writer.print("e");
        LockSupport.parkNanos(SLEEP_TIME);

        writer.print("l");
        LockSupport.parkNanos(SLEEP_TIME);

        writer.print("l");
        LockSupport.parkNanos(SLEEP_TIME);
        writer.print("o");
        LockSupport.parkNanos(SLEEP_TIME);
        writer.print("!");
        writer.println();
        writer.flush();
        writer.close();

        System.out.println("from server:" + reader.readLine());

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    EchoClient ec = new EchoClient();
    for (int i = 0; i < 10; i++) {
      tp.execute(ec);
    }
  }
}
