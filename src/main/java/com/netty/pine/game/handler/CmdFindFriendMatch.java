package com.netty.pine.game.handler;

import com.netty.pine.common.EnumCommandType;
import com.netty.pine.common.EnumResultStatus;
import com.netty.pine.common.GameCommon;
import com.netty.pine.common.GameKeys;
import com.netty.pine.common.model.FindRriendMatchReq;
import com.netty.pine.common.protocol.PObject;
import com.netty.pine.common.protocol.PacketDataProto;
import com.netty.pine.game.GameRoom;
import com.netty.pine.game.GameUser;
import com.netty.pine.game.common.BaseHandler;
import com.netty.pine.game.common.IChannelHandler;

import java.util.List;

public class CmdFindFriendMatch extends BaseHandler implements IChannelHandler {

    @Override
    public void requestHandler(GameUser user, String jsonData) throws InterruptedException {

        logger.info("CmdFindFriendMatch() username = {}, packetId = {}, data = {}", user.getUsername(), user.getPacketId(), jsonData);

        FindRriendMatchReq req = fromJson(jsonData, FindRriendMatchReq.class);

        PacketDataProto.PacketData.Builder res = PacketDataProto.PacketData.newBuilder();
        res.setCmd(EnumCommandType.CMD_FIND_FRIEND_MATCH.getCmd());

        if(req.getQuantity() < 1 || req.getQuantity() < GameCommon.GAME_ROOM_MAX_PLAYER) {
            logger.info("CmdFindFriendMatch() Req quantity invalid [{}]", req.getQuantity());

            res.setResultStatus(EnumResultStatus.IS_EMPTY.getCode());
            sendPacketToUser(user, res);
            return;
        }

        List<Integer> rankList = fromJsonToArrayListInteger(GameCommon.RANK_MATCH_LIST);
        if(rankList.contains(req.getRange())) {
            logger.info("CmdFindFriendMatch() Req rank invalid [{}]", req.getRange());

            res.setResultStatus(EnumResultStatus.RANK_INVALID.getCode());
            sendPacketToUser(user, res);
            return;
        }

        user.setGameType(GameCommon.GAME_ROOM_PLAY_WITH_FRIEND);
        user.setRank(req.getRange());

        GameRoom gameRoom = user.onCreateRoom();
        gameRoom.setMaxUser(req.getQuantity());

        PObject pObject = new PObject();
        pObject.putInt(GameKeys.RANK, req.getRange());
        pObject.putInt(GameKeys.MATCH_ID, gameRoom.getRoomNo());
        pObject.putInt(GameKeys.QUANTITY, req.getQuantity());

        res.setMsg(pObject.toJson());
        res.setResultStatus(EnumResultStatus.SUCCESS.getCode());

        sendPacketToUser(user, res);
    }
}
