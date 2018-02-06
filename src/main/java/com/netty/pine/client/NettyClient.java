package com.netty.pine.client;

import com.netty.pine.common.Utils;
import com.netty.pine.common.model.AuthLogin;
import com.netty.pine.common.protocol.PacketDataProto;
import com.netty.pine.server.ServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class NettyClient {

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);

        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                ch.pipeline().addLast(new ProtobufDecoder(PacketDataProto.PacketData.getDefaultInstance()));
                ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                ch.pipeline().addLast(new ProtobufEncoder());
                ch.pipeline().addLast(new ClientHandler());
            }
        });

        String serverIp = "127.0.0.1";
        //b.connect(serverIp, 8080);
        ChannelFuture f = b.connect(serverIp, 8082);
        Channel channel = f.awaitUninterruptibly().channel();

        String token = "123456" + "0e1287268cc1d97c65339b2f35496ee8";
        AuthLogin authLogin = new AuthLogin("thonghv", token);
        PacketDataProto.PacketData.Builder res = PacketDataProto.PacketData.newBuilder();
        res.setMsg(Utils.toJson(authLogin));
        res.setCmd(102);
        res.setPacketId(0);
        res.setResultStatus(101);
        channel.writeAndFlush(res.build());
    }

}
