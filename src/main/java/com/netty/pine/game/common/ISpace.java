package com.netty.pine.game.common;

import com.netty.pine.game.GameRoom;
import com.netty.pine.game.GameUser;

import java.util.List;

public interface ISpace {

    void init();

    GameUser getUser(String gameId);

    void addUser(GameUser user);

    void addRoom(GameRoom room);

    GameRoom getRoom(int roomId);

    List<GameRoom> getRooms();

    List<GameUser> getUsers();

    boolean isOnline(String gameId);
}
