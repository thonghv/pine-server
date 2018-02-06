package com.netty.pine.common;

public enum  EnumPlayerState {

    NONE(0),
    READY(1),
    PLAYING(2);

    private final int state;
    EnumPlayerState(final int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
