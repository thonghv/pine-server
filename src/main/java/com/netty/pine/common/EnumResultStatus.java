package com.netty.pine.common;

public enum EnumResultStatus {

    NONE(-1),
    SUCCESS(0),
    ERROR(1),
    LOGIN_INVALID(401),

    RANK_INVALID(505),
    IS_EMPTY(501),
    NOT_FOUND_ENEMY(502);

    private final int code;
    EnumResultStatus(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
