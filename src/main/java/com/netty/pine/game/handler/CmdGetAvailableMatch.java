package com.netty.pine.game.handler;

import com.netty.pine.common.EnumCommandType;
import com.netty.pine.common.EnumResultStatus;
import com.netty.pine.common.GameCommon;
import com.netty.pine.common.GameKeys;
import com.netty.pine.common.protocol.PArray;
import com.netty.pine.common.protocol.PObject;
import com.netty.pine.common.protocol.PacketDataProto;
import com.netty.pine.game.GamePublic;
import com.netty.pine.game.GameUser;
import com.netty.pine.game.common.BaseHandler;
import com.netty.pine.game.common.IChannelHandler;

import java.util.Collections;
import java.util.List;

public class CmdGetAvailableMatch extends BaseHandler implements IChannelHandler{

    @Override
    public void requestHandler(GameUser user, String jsonData) throws InterruptedException {

        logger.info("CmdGetAvailableMatch() username = {}, packetId = {}, data = {}", user.getUsername(), user.getPacketId(), jsonData);

        PacketDataProto.PacketData.Builder res = PacketDataProto.PacketData.newBuilder();
        res.setCmd(EnumCommandType.CMD_GET_AVAILABLE_MATCH.getCmd());

        List<GameUser> players = GamePublic.getInstance().getPlayers();

        Collections.sort(players, GameUser.PlayerRankCardComparator_ASC);

        int limit = GameCommon.AVAILABLE_MATCH_LIMIT;
        if(limit > players.size()) {
            limit = players.size();
        }

        PObject pObject = new PObject();
        PArray pArrayPlayers = new PArray();
        pObject.putPArray(GameKeys.PLAYERS, pArrayPlayers);

        for (int i = 0;i < limit; i++) {
            GameUser player = players.get(i);

            PObject pObjectU = new PObject();
            pArrayPlayers.addPObject(pObjectU);

            pObjectU.putUtfString(GameKeys.GAME_ID, player.getGameId());
            pObject.putUtfString(GameKeys.NICK_NAME, player.getUserInfo().getNickName());
            pObject.putInt(GameKeys.RANK, player.getRank());
        }

        res.setResultStatus(EnumResultStatus.SUCCESS.getCode());
        sendPacketToUser(user, res);
    }
}
