package com.netty.pine.game.handler;

import com.netty.pine.common.EnumCommandType;
import com.netty.pine.game.GameUser;
import com.netty.pine.game.common.BaseHandler;
import com.netty.pine.game.common.IChannelHandler;
import io.netty.channel.ChannelHandlerContext;

public class CmdGetUserInfo extends BaseHandler implements IChannelHandler {

    @Override
    public void requestHandler(GameUser user, String jsonData) {
        logger.info("CmdGetUserInfo() data = {}", jsonData);

    }

}
