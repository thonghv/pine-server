package com.netty.pine.game.common;

import com.netty.pine.game.GameRoom;
import com.netty.pine.game.GameUser;

import java.util.HashMap;

public abstract class SpaceSender {

    private HashMap<Integer, GameRoom> roomHashMap = new HashMap<>();
    private HashMap<String, GameUser> userHashMap = new HashMap<>();

    public HashMap<Integer, GameRoom> getRoomHashMap() {
        return roomHashMap;
    }

    public void setRoomHashMap(HashMap<Integer, GameRoom> roomHashMap) {
        this.roomHashMap = roomHashMap;
    }

    public HashMap<String, GameUser> getUserHashMap() {
        return userHashMap;
    }

    public void setUserHashMap(HashMap<String, GameUser> userHashMap) {
        this.userHashMap = userHashMap;
    }
}
