package com.netty.pine.common.model;

public class FindPublicMatchRes {

    private String playerId;

    public FindPublicMatchRes(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
