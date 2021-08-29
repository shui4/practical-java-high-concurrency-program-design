package indi.shui4.ch4.nolock;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/** 参考：https://juejin.cn/post/6844903841134018573#heading-3 */
public class ABATestSolution {

  static class Stack {
    // 将top放在原子类中
    //    private final AtomicReference<Node> top = new AtomicReference<>();
    private final AtomicStampedReference<Node> top = new AtomicStampedReference<>(null, 0);
    // 栈中节点信息
    static class Node {
      int value;
      Node next;

      @Override
      public String toString() {
        return "Node{" + "value=" + value + ", next=" + next + '}';
      }

      public Node(int value) {
        this.value = value;
      }
    }
    // 出栈操作
    public Node pop() {
      for (; ; ) {
        int stamp = top.getStamp();
        // 获取栈顶节点
        Node t = top.getReference();
        if (t == null) {
          return null;
        }
        // 栈顶下一个节点
        Node next = t.next;
        // CAS更新top指向其next节点
        if (top.compareAndSet(t, next, stamp, stamp + 1)) {
          // 把栈顶元素弹出，应该把next清空防止外面直接操作栈
          t.next = null;
          return t;
        }
      }
    }
    // 入栈操作
    public void push(Node node) {
      for (; ; ) {
        // 获取栈顶节点
        int stamp = top.getStamp();
        Node next = top.getReference();
        // 设置栈顶节点为新节点的next节点
        node.next = next;
        // CAS更新top指向新节点
        if (top.compareAndSet(next, node, stamp, stamp + 1)) {
          return;
        }
      }
    }
  }

  private static void testStack() throws InterruptedException {
    // 初始化栈为 top->1->2->3
    Stack stack = new Stack();
    stack.push(new Stack.Node(3));
    stack.push(new Stack.Node(2));
    stack.push(new Stack.Node(1));

    // 线程1出栈一个元素
    Thread t1 = new Thread(stack::pop);
    t1.start();

    Thread t2 =
        new Thread(
            () -> {
              // 线程2出栈两个元素
              Stack.Node A = stack.pop();
              Stack.Node B = stack.pop();
              // 线程2又把A入栈了
              stack.push(A);
            });
    t2.start();

    t1.join();
    t2.join();
  }

  public static void main(String[] args) throws InterruptedException {
    testStack();
  }
}
