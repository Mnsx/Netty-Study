package top.mnsx.netty.c1;

import java.nio.ByteBuffer;

/**
 * @BelongsProject: Temp
 * @User: Mnsx_x
 * @CreateTime: 2022/11/6 14:47
 * @Description:
 */
public class TestByteBufferAllocate {
    public static void main(String[] args) {
        System.out.println(ByteBuffer.allocate(16).getClass());
        System.out.println(ByteBuffer.allocateDirect(16).getClass());
        /*
         class java.nio.HeapByteBuffer -java堆内存，读写效率较低，收到GC影响
         class java.nio.DirectByteBuffer -直接内存，读写效率高，少一次拷贝，不会收到GC影响，分配效率较低，使用不当会造成内存泄漏
         */
    }
}
