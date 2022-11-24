package top.mnsx.netty.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @BelongsProject: Temp
 * @User: Mnsx_x
 * @CreateTime: 2022/11/12 14:06
 * @Description:
 */
@Slf4j
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        // 带有future、promise的类型都是和异步方法配套使用，用来处理结果
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 异步非阻塞，main发起了调用，真正执行的是nio线程
                .connect(new InetSocketAddress("localhost", 8080));

        // 使用sync方法来同步处理结果
        /*channelFuture.sync(); // 阻塞，直到nio线程执行结果完毕
        // 无阻塞的向下执行获取channel
        Channel channel = channelFuture.channel();
        log.debug("{}", channel);
        channel.writeAndFlush("Hello world");*/

        // 使用addListener异步获取结果
        channelFuture.addListener((ChannelFutureListener) future -> {
            Channel channel = future.channel();
            log.debug("{}", channel);
            channel.writeAndFlush("Hello world");
        });
    }
}
