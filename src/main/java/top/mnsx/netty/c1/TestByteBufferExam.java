package top.mnsx.netty.c1;

import java.nio.ByteBuffer;

import static top.mnsx.netty.c1.ByteBufferUtil.debugAll;

/**
 * @BelongsProject: Temp
 * @User: Mnsx_x
 * @CreateTime: 2022/11/6 15:16
 * @Description:
 */
public class TestByteBufferExam {
    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);
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
