package indi.shui4.ch5.parallel_mode_and_algorithm.net.nio;

import com.lmax.disruptor.SleepingWaitStrategy;
import indi.shui4.ch5.parallel_mode_and_algorithm.net.socket.HeavySocketClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shui4
 * @since 1.0
 */
public class NIOEchoServer {
  private static final Map<Socket, Long> time_stat = new HashMap<>();
  private static Selector selector;
  private final ExecutorService tp = Executors.newCachedThreadPool();

  public static void main(String[] args) throws Exception {
    new NIOEchoServer().startServer();
  }

  public void startServer() throws Exception {
    selector = SelectorProvider.provider().openSelector();
    ServerSocketChannel ssc = ServerSocketChannel.open();
    ssc.configureBlocking(false);

    InetSocketAddress isa = new InetSocketAddress(InetAddress.getByName("localhost"), 8000);
    ssc.socket().bind(isa);
    ssc.register(selector, SelectionKey.OP_ACCEPT);
    long e = 0;
    while (true) {
      selector.select();
      Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

      while (iterator.hasNext()) {
        SelectionKey sk = iterator.next();
        SelectableChannel channel = sk.channel();
        // 一定要移除，预防重复处理相同 SelectionKey
        iterator.remove();
        if (sk.isAcceptable()) {
          doAccept(sk);
        } else if (sk.isValid() && sk.isReadable()) {
          Socket socket = ((SocketChannel) channel).socket();
          if (!time_stat.containsKey(socket)) {
            time_stat.put(socket, System.currentTimeMillis());
            doRead(sk);
          }
        } else if (sk.isValid() && sk.isWritable()) {
          Socket socket = ((SocketChannel) channel).socket();
          doWrite(sk);
          e = System.currentTimeMillis();
          long b = time_stat.remove(socket);
          System.out.println("耗时：" + (e - b) + "ms");
        }
      }
    }
  }

  private void doAccept(SelectionKey sk) {

    SelectableChannel channel = sk.channel();
    if (!(channel instanceof ServerSocketChannel)) {
      return;
    }

    final ServerSocketChannel server = (ServerSocketChannel) channel;
    try {
      SocketChannel clientChannel = server.accept();
      clientChannel.configureBlocking(false);
      clientChannel.register(selector, SelectionKey.OP_ACCEPT);
      EchoClient echoClient = new EchoClient();
      sk.attach(echoClient);
      InetAddress clientAddress = clientChannel.socket().getInetAddress();
      System.out.println("接受的连接来自" + clientAddress.getHostAddress() + "。");
    } catch (IOException e) {
      System.out.println("无法接受新客户。");
      e.printStackTrace();
    }
  }

  private void doRead(SelectionKey sk) {

    SelectableChannel channel1 = sk.channel();
    ByteBuffer bb = ByteBuffer.allocate(8192);
    if (!(channel1 instanceof SocketChannel)) {
      return;
    }
    try {
      final SocketChannel channel = (SocketChannel) channel1;
      int len = channel.read(bb);
      if (len < 0) {
        disconnect(sk);
        return;
      }
    } catch (IOException e) {
      System.out.println("无法从客户端读取。");
      e.printStackTrace();
      disconnect(sk);
      return;
    }
    bb.flip();
    tp.execute(new HandleMsg(sk, bb));
  }

  private void doWrite(SelectionKey sk) {
    SocketChannel channel = (SocketChannel) sk.channel();
    EchoClient echoClient = (EchoClient) sk.attachment();
    LinkedList<ByteBuffer> outputQueue = echoClient.getOutputQueue();
    ByteBuffer bb = outputQueue.getLast();
    try {
      int len = channel.write(bb);
      if (len == -1) {
        disconnect(sk);
        return;
      }
      if (bb.remaining() == 0) {
        outputQueue.removeLast();
      }
    } catch (IOException e) {
      System.out.println("无法从客户端写入。");
      e.printStackTrace();
      disconnect(sk);
      return;
    }
    if (outputQueue.size() == 0) {
      sk.interestOps(SelectionKey.OP_READ);
    }
  }

  private void disconnect(SelectionKey sk) {
    try {
      sk.channel().close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static class HandleMsg implements Runnable {
    private ByteBuffer bb;
    private SelectionKey sk;

    public HandleMsg(SelectionKey sk, ByteBuffer bb) {
      this.sk = sk;
      this.bb = bb;
    }

    @Override
    public void run() {
      EchoClient echoClient = (EchoClient) sk.attachment();
      echoClient.enqueue(bb);
      sk.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
      // 强迫selector立即返回
      selector.wakeup();
    }
  }
}
