package top.mnsx.netty.c1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @BelongsProject: Temp
 * @User: Mnsx_x
 * @CreateTime: 2022/11/9 15:05
 * @Description:
 */
public class WriteServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        ssc.bind(new InetSocketAddress(8080));

        while (true) {
            selector.select();
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    SelectionKey scKey = sc.register(selector, 0, null);

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 30000000; ++i) {
                        sb.append("a");
                    }

                    ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());

                    // 返回值代表实际写入的字节数
                    int length = sc.write(buffer);
                    System.out.println(length);

                    // 判断是否有剩余内容
                    if (buffer.hasRemaining()) {
                        // 关注可写事件
                        scKey.interestOps(scKey.interestOps() + SelectionKey.OP_WRITE);
                        // 把未写完的数据搞到scKey中
                        scKey.attach(buffer);
                    }
                } else if (key.isWritable()) {
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    SocketChannel sc = (SocketChannel) key.channel();
                    int write = sc.write(buffer);
                    System.out.println(write);
                    // 清理操作
                    if (!buffer.hasRemaining()) {
                        key.attach(null); // 清除buffer
                        key.interestOps(key.interestOps() - SelectionKey.OP_WRITE); // 不去关注可写事件
                    }
                }
            }
        }
    }
}
