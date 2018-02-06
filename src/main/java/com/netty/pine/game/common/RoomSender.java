package com.netty.pine.game.common;

import com.netty.pine.game.GameUser;
import com.netty.pine.server.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public abstract class RoomSender {

    protected static Logger logger = LoggerFactory.getLogger(RoomSender.class);

    private int roomNo;
    private int minUser;
    private int maxUser;

    public RoomSender(int roomNo) {
        this.roomNo = roomNo;
    }

    private HashMap<String, GameUser> playerHashMap = new HashMap<String, GameUser>();

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public int getMaxUser() {
        return maxUser;
    }

    public void setMaxUser(int maxUser) {
        this.maxUser = maxUser;
    }

    public int getMinUser() {
        return minUser;
    }

    public void setMinUser(int minUser) {
        this.minUser = minUser;
    }

    public HashMap<String, GameUser> getPlayerHashMap() {
        return playerHashMap;
    }

    public void setPlayerHashMap(HashMap<String, GameUser> playerHashMap) {
        this.playerHashMap = playerHashMap;
    }

    public List<GameUser> getPlayers() {
        return (List<GameUser>) playerHashMap.values();
    }

    public int getCurrentPlayer() {
        return playerHashMap.size();
    }
}
