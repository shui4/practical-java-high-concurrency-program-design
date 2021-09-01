package indi.shui4.ch5.parallel_mode_and_algorithm.net.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

/**
 * @author shui4
 * @since 1.0
 */
public class NIOEchoClient {
  private Selector selector;

  public static void main(String[] args) throws IOException {
    NIOEchoClient client = new NIOEchoClient();
    client.init("localhost", 8000);
    client.working();
  }

  public void init(String ip, int port) throws IOException {
    SocketChannel channel = SocketChannel.open();
    channel.configureBlocking(false);
    this.selector = SelectorProvider.provider().openSelector();
    channel.connect(new InetSocketAddress(ip, port));
    channel.register(selector, SelectionKey.OP_CONNECT);
  }

  public void working() throws IOException {
    while (true) {
      if (!selector.isOpen()) {
        break;
      }

      selector.select();
      Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

      while (iterator.hasNext()) {
        SelectionKey key = iterator.next();
        iterator.remove();
        if (key.isConnectable()) {
          connect(key);
        } else if (key.isReadable()) {
          read(key);
        }
      }
    }
  }

  private void connect(SelectionKey key) throws IOException {
    SocketChannel channel = (SocketChannel) key.channel();
    if (channel.isConnectionPending()) {
      channel.finishConnect();
    }
    channel.configureBlocking(false);
    channel.write(ByteBuffer.wrap(new String("hello server!").getBytes()));
    channel.register(this.selector, SelectionKey.OP_READ);
  }

  private void read(SelectionKey key) throws IOException {
    SocketChannel channel = (SocketChannel) key.channel();
    ByteBuffer buffer = ByteBuffer.allocate(100);
    channel.read(buffer);
    byte[] data = buffer.array();
    String msg = new String(data).trim();
    System.out.println("客户端收到消息：" + msg);
    channel.close();
    key.selector().close();
  }
}
