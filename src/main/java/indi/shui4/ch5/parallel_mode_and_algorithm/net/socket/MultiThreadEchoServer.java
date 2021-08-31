package indi.shui4.ch5.parallel_mode_and_algorithm.net.socket;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Console;
import com.google.common.base.Stopwatch;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shui4
 * @since 1.0
 */
@SuppressWarnings("InfiniteLoopStatement")
public class MultiThreadEchoServer {
  private static final ExecutorService tp = Executors.newCachedThreadPool();

  static class HandleMsg implements Runnable {
    final Socket clientSocket;

    public HandleMsg(Socket clientSocket) {
      this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
      try (BufferedReader reader =
              IoUtil.toBuffered(new InputStreamReader(clientSocket.getInputStream()));
          PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

        Stopwatch stopwatch = Stopwatch.createStarted();
        String read = IoUtil.read(reader);
        System.out.println(read);
        writer.println(read);

        stopwatch.stop();
        System.out.println(stopwatch);
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        if (clientSocket != null) {
          try {
            clientSocket.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  public static void main(String[] args) throws IOException {
    ServerSocket echoServer = new ServerSocket(8000);
    while (true) {
      Socket clientSocket = echoServer.accept();
      Console.log("{}连接", clientSocket.getRemoteSocketAddress());
      tp.execute(new HandleMsg(clientSocket));
    }
  }
}
