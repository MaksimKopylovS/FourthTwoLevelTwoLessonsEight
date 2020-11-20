package packageClient;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import packageMessage.FileMessage;
import packageMessage.MyMessage;

import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Network {
    private SocketChannel channel;
    private static String msg;
    private static final Logger LOGGER = Logger.getLogger(Network.class);


    public Network() {
        new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel;
                                socketChannel.pipeline().addLast(new ObjectDecoder(1024 * 1024 * 100, ClassResolvers.cacheDisabled(null)),
                                        new ObjectEncoder(),
                                        new ChannelInboundHandlerAdapter() {

                                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                                Thread.sleep(50);
                                                new NetworkMessageHandler(ctx, msg);

                                            }

                                        });
                            }
                        });
                ChannelFuture future = b.connect("localhost", 8189).sync();
                LOGGER.log(Level.INFO, "Клиент запущен");
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        }).start();
    }

    public void sendMessage(String msg) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Object o = new MyMessage(msg);
                channel.writeAndFlush(o);
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void sendFile(String msg) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Path path = Paths.get(msg);
                    System.out.println(path.getFileName());
                    Object o = new FileMessage(path);
                    channel.writeAndFlush(o);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();


    }


}
