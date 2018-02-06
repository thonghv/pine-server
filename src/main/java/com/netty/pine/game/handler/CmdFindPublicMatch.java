package com.netty.pine.game.handler;

import com.netty.pine.common.EnumCommandType;
import com.netty.pine.common.EnumResultStatus;
import com.netty.pine.common.GameCommon;
import com.netty.pine.common.model.FindPublicMatchReq;
import com.netty.pine.common.model.FindPublicMatchRes;
import com.netty.pine.common.protocol.PacketDataProto;
import com.netty.pine.game.GamePublic;
import com.netty.pine.game.GameUser;
import com.netty.pine.game.common.BaseHandler;
import com.netty.pine.game.common.IChannelHandler;

import java.util.List;

public class CmdFindPublicMatch extends BaseHandler implements IChannelHandler {

    @Override
    public void requestHandler(GameUser user, String jsonData) throws InterruptedException {

        logger.info("CmdFindPublicMatch() username = {}, packetId = {}, data = {}", user.getUsername(), user.getPacketId(), jsonData);

        FindPublicMatchReq rank = fromJson(jsonData, FindPublicMatchReq.class);

        PacketDataProto.PacketData.Builder res = PacketDataProto.PacketData.newBuilder();
        res.setCmd(EnumCommandType.CMD_FIND_PUBLIC_MATCH.getCmd());

        List<Integer> rankList = fromJsonToArrayListInteger(GameCommon.RANK_MATCH_LIST);
        if(rankList.contains(rank.getRange())) {
            logger.info("CmdFindPublicMatch() Rank request invalid [{}]", rank.getRange());

            res.setResultStatus(EnumResultStatus.RANK_INVALID.getCode());
            sendPacketToUser(user, res);
            return;
        }

        user.setRank(rank.getRange());
        user.setGameType(GameCommon.GAME_ROOM_FIND_PUBLIC_MATCH);

        GameUser player = GamePublic.getInstance().findPlayer(rank.getRange());

        if(player == null) {
            GamePublic.getInstance().addPlayer(user);
            return;
        }

        GamePublic.getInstance().addPlayer(user);


        res.setResultStatus(EnumResultStatus.SUCCESS.getCode());
        res.setMsg(toJson(new FindPublicMatchRes(player.getGameId())));
        sendPacketToUser(user, res);

        res.setMsg(toJson(new FindPublicMatchRes(user.getGameId())));
        sendPacketToUser(player, res);
    }
}
