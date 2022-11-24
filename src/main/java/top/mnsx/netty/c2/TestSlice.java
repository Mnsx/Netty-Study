package top.mnsx.netty.c2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static top.mnsx.netty.c2.BufUtils.log;

/**
 * @BelongsProject: Temp
 * @User: Mnsx_x
 * @CreateTime: 2022/11/16 15:59
 * @Description:
 */
public class TestSlice {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a', 'b', 'c', 'c', 'e', 'f', 'g', 'h', 'i', 'j'});
        log(buf);

        // 切片过程中没有发生数据复制
        ByteBuf f1 = buf.slice(0, 5);
        ByteBuf f2 = buf.slice(5, 5);
        f1.retain();
        f1.retain();

        log(f1);
        log(f2);

        buf.release();
        log(f1);

        f1.release();
        f1.release();
        /*f1.setByte(0, 'x');

        log(buf);
        log(f1);
        log(f2);*/
    }
}
