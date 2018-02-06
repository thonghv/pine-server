package com.netty.pine.game.handler;

import com.netty.pine.adapter.handler.UserAdapterHandler;
import com.netty.pine.common.EnumCommandType;
import com.netty.pine.common.EnumResultStatus;
import com.netty.pine.common.GameCommon;
import com.netty.pine.common.GameKeys;
import com.netty.pine.common.model.SendChoiceReq;
import com.netty.pine.common.protocol.PArray;
import com.netty.pine.common.protocol.PObject;
import com.netty.pine.common.protocol.PacketDataProto;
import com.netty.pine.game.GameRoom;
import com.netty.pine.game.GameUser;
import com.netty.pine.game.common.BaseHandler;
import com.netty.pine.game.common.IChannelHandler;

import java.util.Collections;
import java.util.List;

public class CmdSendChoice extends BaseHandler implements IChannelHandler {

    @Override
    public void requestHandler(GameUser user, String jsonData) throws InterruptedException {

        logger.info("CmdSendChoice() username = {}, packetId = {}, data = {}", user.getUsername(), user.getPacketId(), jsonData);

        PacketDataProto.PacketData.Builder res = PacketDataProto.PacketData.newBuilder();
        res.setPacketId(user.getPacketId());
        res.setCmd(EnumCommandType.CMD_SEND_CHOICE.getCmd());
        res.setResultStatus(EnumResultStatus.SUCCESS.getCode());

        SendChoiceReq req = fromJson(jsonData, SendChoiceReq.class);

        if(req.getChoice() <= 0) {
            logger.error("CmdSendChoice() Req data is empty.");

            res.setResultStatus(EnumResultStatus.IS_EMPTY.getCode());
            sendPacketToUser(user, res);
            return;
        }

        GameRoom gameRoom = user.getRoom();
        if(gameRoom == null) {
            logger.error("CmdSendChoice() Req data is empty.");

            res.setResultStatus(EnumResultStatus.ERROR.getCode());
            sendPacketToUser(user, res);
            return;
        }

        if(req.getChoice() != gameRoom.getNumberCurrent()) {
            user.decreasePoint();
            if(onCanLose(gameRoom, user.getPoint())) {
                onEndGameProcess(gameRoom);
                return;
            }
        } else {
            user.decreasePoint();
            gameRoom.increaseCurrent();

            if(gameRoom.isEndGame()) {
                onEndGameProcess(gameRoom);
            }
        }

        PObject pObject = new PObject();
        pObject.putInt(GameKeys.CURRENT, gameRoom.getNumberCurrent());

        PObject pPlayer = new PObject();
        pPlayer.putUtfString(GameKeys.GAME_ID, user.getGameId());
        pPlayer.putInt(GameKeys.POINT, user.getPoint());

        PArray pArrayPlayers = new PArray();
        pObject.putPArray(GameKeys.PLAYERS, pArrayPlayers);
        pArrayPlayers.addPObject(pPlayer);

        res.setCmd(EnumCommandType.CMD_UPDATE_GAME_PLAY.getCmd());
        res.setMsg(pObject.toJson());
        gameRoom.onBroadCastToRoom(res.build());
    }

    private boolean onCanLose(GameRoom gameRoom, int point) {

        int targetPoint = gameRoom.getRank() - gameRoom.getNumberCurrent();
        if((-1) * point > targetPoint) {
            return true;
        }

        return false;
    }

    private void onEndGameProcess(GameRoom gameRoom) throws InterruptedException {

        PacketDataProto.PacketData.Builder res = PacketDataProto.PacketData.newBuilder();
        res.setCmd(EnumCommandType.CMD_FINISH_MATCH.getCmd());
        res.setResultStatus(EnumResultStatus.SUCCESS.getCode());

        List<GameUser> players = gameRoom.getPlayers();

        Collections.sort(players, GameUser.PlayerPointCardComparator_DESC);

        GameUser winPlayer = players.get(0);

        for (GameUser player : players) {

            PObject pObject = new PObject();

            PObject pPlayer = new PObject();
            pPlayer.putUtfString(GameKeys.GAME_ID, player.getGameId());
            pPlayer.putInt(GameKeys.POINT, player.getPoint());

            PArray pArrayPlayers = new PArray();
            pObject.putPArray(GameKeys.PLAYERS, pArrayPlayers);
            pArrayPlayers.addPObject(pPlayer);

            res.setMsg(pObject.toJson());
            player.sendPacket(res.build());

            if(winPlayer.getGameId().equals(player.getGameId())) {
                player.getUserInfo().increaseWinNum();
            } else {
                player.getUserInfo().increaseLoseNum();
            }
        }

        // only update world rank with type = public
        if(gameRoom.getGameType() != GameCommon.GAME_ROOM_FIND_PUBLIC_MATCH) {
            return;
        }

        // update data
        for (GameUser player : players) {
            if(winPlayer.getGameId().equals(player.getGameId())) {
                UserAdapterHandler.getInstante().updateUserWinNum(player.getGameId(), player.getUserInfo().getWinNum(), player.getUserInfo().getWorldRank());
            } else {
                UserAdapterHandler.getInstante().updateUserLoseNum(player.getGameId(), player.getUserInfo().getLoseNum(), player.getUserInfo().getWorldRank());
            }
        }
    }
}
