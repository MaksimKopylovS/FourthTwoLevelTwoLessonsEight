package packageServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


import java.util.ArrayList;

public class Server {

    private static final Logger LOGGER = Logger.getLogger(Server.class);



    public Server() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(
                                    new ObjectDecoder(1024 * 1024 * 100, ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new ClientHandler()
                            );
                        }
                    });

            ChannelFuture future = bootstrap.bind(8189).sync();
            LOGGER.log(Level.INFO, "Server Start");
            System.out.println("Server Start");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }



    public static void main(String[] args) {
        new Server();
    }
}
