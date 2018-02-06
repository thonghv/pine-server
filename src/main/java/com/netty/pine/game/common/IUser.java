package com.netty.pine.game.common;

import com.netty.pine.game.GameRoom;

public interface IUser {

    void init();

    void onLogin() throws InterruptedException;

    void onDisconnect();

    GameRoom onCreateRoom();

    void onFindRoom();

    void onJoinRoom(int roomNo);

    boolean isInRoom();
}
