package com.netty.pine.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //PineObject pineObject = (PineObject) msg;
        System.out.println("THONGH");
        //LoopBackTimeStamp ts = (LoopBackTimeStamp) msg;
//        /ctx.writeAndFlush(new PineObject()); // recieved message sent back directly
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
