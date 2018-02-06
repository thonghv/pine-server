package com.netty.pine.game.handler;

import com.netty.pine.common.*;
import com.netty.pine.common.model.ReadyToPlayReq;
import com.netty.pine.common.protocol.PArray;
import com.netty.pine.common.protocol.PObject;
import com.netty.pine.common.protocol.PacketDataProto;
import com.netty.pine.game.GamePublic;
import com.netty.pine.game.GameRoom;
import com.netty.pine.game.GameUser;
import com.netty.pine.game.common.BaseHandler;
import com.netty.pine.game.common.IChannelHandler;


public class CmdReadyToPlay extends BaseHandler implements IChannelHandler {

    @Override
    public void requestHandler(GameUser user, String jsonData) throws InterruptedException {

        logger.info("CmdReadyToPlay() username = {}, packetId = {}, data = {}", user.getUsername(), user.getPacketId(), jsonData);

        PacketDataProto.PacketData.Builder res = PacketDataProto.PacketData.newBuilder();
        res.setPacketId(user.getPacketId());
        res.setCmd(EnumCommandType.CMD_READY_TO_PLAY.getCmd());

        ReadyToPlayReq req = fromJson(jsonData, ReadyToPlayReq.class);

        if(req.getPlayerId().isEmpty()) {
            logger.error("CmdReadyToPlay() Player ID is empty.");

            res.setResultStatus(EnumResultStatus.IS_EMPTY.getCode());
            sendPacketToUser(user, res);
            return;
        }

        user.setPlayerState(EnumPlayerState.READY);

        GameUser enemyPlayer = GamePublic.getInstance().findPlayer(req.getPlayerId());
        if(enemyPlayer == null) {
            logger.error("CmdReadyToPlay() Not found enemy player {}", req.getPlayerId());

            res.setResultStatus(EnumResultStatus.NOT_FOUND_ENEMY.getCode());
            sendPacketToUser(user, res);
            return;
        }

        if(!enemyPlayer.getPlayerState().equals(EnumPlayerState.READY)) {
            return;
        }

        // create room - start match
        GameRoom gameRoom = onStartMatch(user, enemyPlayer, user.getRank());

        PObject pObject = new PObject();

        pObject.putInt(GameKeys.MATCH_ID, gameRoom.getRoomNo());
        pObject.putInt(GameKeys.RANK, user.getRank());
        pObject.putInt(GameKeys.TYPE, GameCommon.GAME_ROOM_FIND_PUBLIC_MATCH);

        PArray pArrayPlayers = new PArray();
        pArrayPlayers.addPObject(user.toPObjectPlayer());
        pArrayPlayers.addPObject(enemyPlayer.toPObjectPlayer());

        pObject.putPArray(GameKeys.PLAYERS, pArrayPlayers);

        res.setCmd(EnumCommandType.CMD_START_MATCH.getCmd());
        res.setMsg(pObject.toJson());

        // send packet data
        sendPacketToUser(user, res);
        sendPacketToUser(enemyPlayer, res);
    }

    private GameRoom onStartMatch(GameUser gamePlayer, GameUser enemyPlayer, int rank) {
        logger.info("onStartMatch() {}", gamePlayer.toString());

        GameRoom gameRoom = gamePlayer.onCreateRoom();
        gameRoom.setGameType(gamePlayer.getGameType());
        enemyPlayer.onJoinRoom(gameRoom.getRoomNo());

        gameRoom.onStart(rank);
        return gameRoom;
    }
}
