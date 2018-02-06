package com.netty.pine.common;

public class GameCommon {

    /**
     * Game common config
     */
    // ====================================================================
    public static final String AUTHENTICATION_BASE_CODE     = "org.pine.monkey";
    public static final String RANK_MATCH_LIST              = "[10,100,500,1000,5000]";
    public static final int LEADRR_BOARD_LIMIT              = 100;
    public static final int AVAILABLE_MATCH_LIMIT           = 5;

    /**
     * Game room config
     */
    // ====================================================================
    public static final int GAME_ROOM_MAX_PLAYER        =   10;
    public static final int GAME_ROOM_MIN_PLAYER        =   2;

    public static final int GAME_ROOM_FIND_PUBLIC_MATCH        =   1;
    public static final int GAME_ROOM_PLAY_WITH_FRIEND         =   2;
    public static final int GAME_ROOM_PLAY_WITH_CUSTOM         =   3;
}
