package com.netty.pine.game;

import com.netty.pine.game.common.ISpace;
import com.netty.pine.game.common.SpaceSender;

import java.util.ArrayList;
import java.util.List;

public class GameSpace extends SpaceSender implements ISpace {

    private static GameSpace instance = null;

    private GameSpace() {}

    public static GameSpace getInstance() {
        if(instance == null) {
            instance = new GameSpace();
        }

        return instance;
    }

    @Override
    public void init() {

    }

    @Override
    public GameUser getUser(String gameId) {

        if(!getUserHashMap().containsKey(gameId)) {
            return null;
        }

        return getUserHashMap().get(gameId);
    }

    @Override
    public void addUser(GameUser user) {
        getUserHashMap().put(user.getGameId(), user);
    }

    @Override
    public void addRoom(GameRoom room) {
        getRoomHashMap().put(room.getRoomNo(), room);
    }

    @Override
    public GameRoom getRoom(int roomId) {

        if(!getRoomHashMap().containsKey(roomId)) {
            return null;
        }

        return getRoomHashMap().get(roomId);
    }

    @Override
    public List<GameRoom> getRooms() {
        return (ArrayList) getRoomHashMap().values();
    }

    @Override
    public List<GameUser> getUsers() {
        return (ArrayList) getUserHashMap().values();
    }

    @Override
    public boolean isOnline(String gameId) {
        return getUserHashMap().containsKey(gameId);
    }

    public GameRoom findRoom(int roomNo) {
        if(getRoomHashMap().containsKey(roomNo)) {
            return null;
        }

        return getRoomHashMap().get(roomNo);
    }
}
