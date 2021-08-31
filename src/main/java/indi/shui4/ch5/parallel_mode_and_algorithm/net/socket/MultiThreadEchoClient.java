package indi.shui4.ch5.parallel_mode_and_algorithm.net.socket;

import cn.hutool.core.lang.Console;
import com.google.common.base.Stopwatch;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author shui4
 * @since 1.0
 */
public class MultiThreadEchoClient {
  public static void main(String[] args) throws IOException, InterruptedException {
    Socket client = new Socket();
    client.connect(new InetSocketAddress("localhost", 8000));
    PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
    writer.println("Hello");
    writer.flush();
    writer.close();
    BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
    Console.log("来自服务:{}", reader.readLine());
    TimeUnit.SECONDS.sleep(5);
  }
}
