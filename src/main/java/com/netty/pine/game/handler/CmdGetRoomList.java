package com.netty.pine.game.handler;

import com.netty.pine.common.EnumCommandType;
import com.netty.pine.common.EnumResultStatus;
import com.netty.pine.common.model.AuthLogin;
import com.netty.pine.common.protocol.PacketDataProto;
import com.netty.pine.game.GameSpace;
import com.netty.pine.game.GameUser;
import com.netty.pine.game.common.BaseHandler;
import com.netty.pine.game.common.IChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class CmdGetRoomList extends BaseHandler implements IChannelHandler {

    @Override
    public void requestHandler(GameUser user, String jsonData) throws InterruptedException {
        logger.info("CmdGetRoomList() data = {}", jsonData);


    }
}
