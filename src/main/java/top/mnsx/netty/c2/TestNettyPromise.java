package top.mnsx.netty.c2;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: Temp
 * @User: Mnsx_x
 * @CreateTime: 2022/11/14 16:50
 * @Description:
 */
@Slf4j
public class TestNettyPromise {
    public static void main(String[] args) {
        // 准备eventLoop
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        EventLoop eventLoop = eventLoopGroup.next();
        // 主动创建promise，结果容器
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

        new Thread(() -> {
            try {
                log.debug("开始计算");
                int i = 1 / 0;
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                promise.setSuccess(80);
            } catch (Exception e) {
                promise.setFailure(e);
            }
        }).start();

        // 接收结果
        promise.addListener((future) -> {
            log.debug("执行结果: {}", future.get());
        });
    }
}
