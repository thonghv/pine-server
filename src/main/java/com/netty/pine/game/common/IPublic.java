package com.netty.pine.game.common;

import com.netty.pine.game.GameUser;

public interface IPublic {

    void init();

    boolean addPlayer(GameUser user);

    boolean removePlayer(String gameId);

    GameUser findPlayer(String gameId);
}
