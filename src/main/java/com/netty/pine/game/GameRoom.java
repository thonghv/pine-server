package com.netty.pine.game;

import com.netty.pine.common.protocol.PacketDataProto;
import com.netty.pine.game.common.IRoom;
import com.netty.pine.game.common.RoomSender;

import java.util.List;

public class GameRoom extends RoomSender implements IRoom {

    private int rank;
    private int numberCurrent;
    private int gameType;

    public GameRoom(int roomNo) {
        super(roomNo);

        this.init();
    }

    /**
     * Getter and setter
     */
    // ===========================================================
    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public int getNumberCurrent() {
        return numberCurrent;
    }

    public void increaseCurrent() {
        this.numberCurrent ++;
    }

    @Override
    public void onStart(int rank) {
        this.rank = rank;

        numberCurrent = 1;
    }

    @Override
    public void init() {

    }

    @Override
    public GameRoom onCreateRoom() {
        // Call when user have created room
        // TODO:
        return null;
    }

    @Override
    public GameUser onJoinRoom() {
        // Call when user have joined room
        // TODO:
        return null;
    }

    @Override
    public void onDestroyRoom() {

    }

    public void addPlayer(GameUser player) {

        int numPlayer = getCurrentPlayer();

        if(numPlayer > getMaxUser()) {
            logger.warn("addPlayer() limit user in room {}", numPlayer);
            return;
        }

        getPlayerHashMap().put(player.getGameId(), player);
    }

    public void onBroadCastToRoom(PacketDataProto.PacketData res) throws InterruptedException {

        for (GameUser player : getPlayerHashMap().values()) {
            player.sendPacket(res);
        }

    }

    public boolean isEndGame() {
        return getNumberCurrent() >= getRank();
    }
}
