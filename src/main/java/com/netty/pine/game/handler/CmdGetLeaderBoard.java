package com.netty.pine.game.handler;

import com.netty.pine.adapter.entity.UserEntity;
import com.netty.pine.adapter.handler.UserAdapterHandler;
import com.netty.pine.common.EnumCommandType;
import com.netty.pine.common.EnumResultStatus;
import com.netty.pine.common.GameCommon;
import com.netty.pine.common.GameKeys;
import com.netty.pine.common.protocol.PArray;
import com.netty.pine.common.protocol.PObject;
import com.netty.pine.common.protocol.PacketDataProto;
import com.netty.pine.game.GameUser;
import com.netty.pine.game.common.BaseHandler;
import com.netty.pine.game.common.IChannelHandler;

import java.util.List;

public class CmdGetLeaderBoard extends BaseHandler implements IChannelHandler {

    @Override
    public void requestHandler(GameUser user, String jsonData) throws InterruptedException {

        logger.info("CmdGetLeaderBoard() username = {}, packetId = {}, data = {}", user.getUsername(), user.getPacketId(), jsonData);

        List<UserEntity> players = UserAdapterHandler.getInstante().getLeaderBoard(GameCommon.LEADRR_BOARD_LIMIT);

        PObject pObject = new PObject();
        PArray pArrayPlayers = new PArray();
        pObject.putPArray(GameKeys.PLAYERS, pArrayPlayers);

        for (UserEntity u : players) {
            PObject pObjectU = new PObject();
            pArrayPlayers.addPObject(pObjectU);

            pObjectU.putUtfString(GameKeys.NICK_NAME, u.getNickname());
            pObjectU.putUtfString(GameKeys.GAME_ID, u.getGameId());
            pObjectU.putUtfString(GameKeys.AVATAR, u.getAvatar());
            pObjectU.putInt(GameKeys.WORLD_RANK, u.getWorldRank());
        }

        PacketDataProto.PacketData.Builder res = PacketDataProto.PacketData.newBuilder();
        res.setCmd(EnumCommandType.CMD_GET_LEADER_BOARD.getCmd());
        res.setResultStatus(EnumResultStatus.SUCCESS.getCode());
        res.setMsg(pObject.toJson());

        sendPacketToUser(user, res);
    }
}
