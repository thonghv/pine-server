package com.netty.pine.game.common;

import com.netty.pine.game.GameRoom;
import com.netty.pine.game.GameUser;

public interface IRoom {

    void init();

    GameRoom onCreateRoom();

    GameUser onJoinRoom();

    void onStart(int rank);

    void onDestroyRoom();
}
