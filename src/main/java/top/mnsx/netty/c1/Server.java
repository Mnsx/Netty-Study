package top.mnsx.netty.c1;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

import static top.mnsx.netty.c1.ByteBufferUtil.debugAll;

/**
 * @BelongsProject: Temp
 * @User: Mnsx_x
 * @CreateTime: 2022/11/7 15:38
 * @Description:
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        // 创建Selector，管理多个channel
        Selector selector = Selector.open();

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        // 建立Selector和Channel之间的联系
        // SelectionKey事件发生侯，通过这个可以获取事件，并且得知是哪个Channel获取的
        SelectionKey sscKey = ssc.register(selector, 0, null);
        // key只关注accept事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("register: {}", sscKey);

        ssc.bind(new InetSocketAddress(8080));
        while (true) {
            // 调用select方法，没有事件发生就阻塞，有事件发生恢复运行
            // select在事件未处理时，他不会阻塞，事件发生后，要么处理，要么取消
            selector.select();
            // 处理时间，内部包含了所有发生的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 处理key的时候要从selectedKeys中删除这个key，否则下次处理会报错
                iterator.remove();
                log.debug("register: {}", key);
                // 区分事件类型
                if (key.isAcceptable()) { // 如果时读取事件
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16); // attachment 附件
                    // 将ByteBuffer作为附件绑定在key上
                    SelectionKey scKey = sc.register(selector, 0, buffer);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("{}", sc);
                } else if (key.isReadable()) { // 如果是读取事件
                    try {
                        SocketChannel channel = (SocketChannel) key.channel(); // 触发事件的Channel
                        // 获取SelectionKey上关联的附件
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = channel.read(buffer); // 如果是正常断开，read方法返回值是-1
                        if (read == -1) {
                            key.cancel();
                        } else {
//                            buffer.flip();
//                            debugRead(buffer);
//                            System.out.println(Charset.defaultCharset().decode(buffer));
                            split(buffer);
                            if (buffer.position() == buffer.limit()) {
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel(); // 因为客户端，断开了，因此需要将key取消，从selectedKeys中真正的删除
                    }
                }
//                key.cancel();
            }
        }
    }

    private static void split(ByteBuffer source) {
        source.flip();

        for (int i = 0; i < source.limit(); ++i) {
            // 找到一条完整消息
            if (source.get(i) == '\n') {
                int length = i - source.position() + 1;
                // 存入一个新的ByteBuffer
                ByteBuffer target = ByteBuffer.allocate(length);
                // 从Source读，向target写
                for (int j = 0; j < length; ++j) {
                    target.put(source.get());
                }
                debugAll(target);
            }
        }

        source.compact();
    }
}
