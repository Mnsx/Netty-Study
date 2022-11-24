package top.mnsx.netty.c2;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: Temp
 * @User: Mnsx_x
 * @CreateTime: 2022/11/13 9:57
 * @Description:
 */
@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {
        // 创建事件循环对象
        NioEventLoopGroup group = new NioEventLoopGroup(2); // io事件、普通任务、定时任务
//        DefaultEventLoopGroup group = new DefaultEventLoopGroup(); // 普通任务、定时任务

        // 获取下一个事件对象
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        // 执行普通任务
        /*group.next().submit(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log.debug("ok");
        });*/

        // 执行定时任务
        group.next().scheduleAtFixedRate(() -> {
            log.debug("ok");
        }, 0, 1, TimeUnit.SECONDS);

        log.debug("main");
    }
}
