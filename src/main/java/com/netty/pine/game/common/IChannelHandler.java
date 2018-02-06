package com.netty.pine.game.common;

import com.netty.pine.game.GameUser;
import io.netty.channel.ChannelHandlerContext;

public interface IChannelHandler {

    void requestHandler(GameUser user, String jsonData) throws InterruptedException;
}
