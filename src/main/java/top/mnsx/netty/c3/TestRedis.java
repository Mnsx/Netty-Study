package top.mnsx.netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @BelongsProject: Temp
 * @User: Mnsx_x
 * @CreateTime: 2022/11/19 11:14
 * @Description:
 */
public class TestRedis {
    public static void main(String[] args) {
        final byte[] LINE = {13, 10};
        NioEventLoopGroup worker = new NioEventLoopGroup();
        new Bootstrap()
                .channel(NioSocketChannel.class)
                .group(worker)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler());
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                ByteBuf buf = ctx.alloc().buffer();
                                buf.writeBytes("*2".getBytes(StandardCharsets.UTF_8));
                                buf.writeBytes(LINE);
                                buf.writeBytes("$4".getBytes(StandardCharsets.UTF_8));
                                buf.writeBytes(LINE);
                                buf.writeBytes("auth".getBytes(StandardCharsets.UTF_8));
                                buf.writeBytes(LINE);
                                buf.writeBytes("$6".getBytes(StandardCharsets.UTF_8));
                                buf.writeBytes(LINE);
                                buf.writeBytes("123123".getBytes(StandardCharsets.UTF_8));
                                buf.writeBytes(LINE);
                                buf.writeBytes("*3".getBytes(StandardCharsets.UTF_8));
                                buf.writeBytes(LINE);
                                buf.writeBytes("$3".getBytes(StandardCharsets.UTF_8));
                                buf.writeBytes(LINE);
                                buf.writeBytes("set".getBytes(StandardCharsets.UTF_8));
                                buf.writeBytes(LINE);
                                buf.writeBytes("$4".getBytes(StandardCharsets.UTF_8));
                                buf.writeBytes(LINE);
                                buf.writeBytes("name".getBytes(StandardCharsets.UTF_8));
                                buf.writeBytes(LINE);
                                buf.writeBytes("$8".getBytes(StandardCharsets.UTF_8));
                                buf.writeBytes(LINE);
                                buf.writeBytes("zhangsan".getBytes(StandardCharsets.UTF_8));
                                buf.writeBytes(LINE);
                                ctx.writeAndFlush(buf);
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                System.out.println(buf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                })
                .connect("mnsx.top", 6379);
    }
}
