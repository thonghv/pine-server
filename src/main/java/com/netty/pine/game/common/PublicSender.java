package com.netty.pine.game.common;

import com.netty.pine.game.GameUser;

import java.util.HashMap;

public abstract class PublicSender {

    private HashMap<String, GameUser> users = new HashMap<>();

    public HashMap<String, GameUser> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, GameUser> users) {
        this.users = users;
    }
}
