package com.netty.pine.common;

public enum EnumCommandType {

    NONE("NONE", 0),
    AUTH_BORN("AUTH_BORN", 101),
    AUTH_LOGIN("AUTH_LOGIN", 102),
    AUTH_LOGIN_GUEST("AUTH_LOGIN_GUEST", 103),
    AUTH_REGISTER("AUTH_REGISTER", 104),
    AUTH_LOGOUT("AUTH_LOGOUT", 105),
    CMD_GET_USER_INFO("CMD_GET_USER_INFO", 106),

    CMD_FIND_PUBLIC_MATCH("CMD_FIND_PUBLIC_MATCH", 107),
    CMD_FIND_FRIEND_MATCH("CMD_FIND_FRIEND_MATCH", 108),
    CMD_READY_TO_PLAY("CMD_READY_TO_PLAY", 109),
    CMD_START_MATCH("CMD_START_MATCH", 110),
    CMD_FINISH_MATCH("CMD_FINISH_MATCH", 111),
    CMD_GET_LEADER_BOARD("CMD_FINISH_MATCH", 112),
    CMD_SEND_CHOICE("CMD_SEND_CHOICE", 113),
    CMD_UPDATE_GAME_PLAY("CMD_UPDATE_GAME_PLAY", 114),
    CMD_GET_AVAILABLE_MATCH("CMD_GET_AVAILABLE_MATCH", 115),

    CMD_GET_ROOM_LIST("CMD_GET_ROOM_LIST", 220);

    private final int cmd;
    private String desc;

    EnumCommandType(String desc, final int cmd) {
        this.cmd = cmd;
        this.desc = desc;
    }

    public int getCmd() {
        return cmd;
    }

    public String getDesc() {
        return desc;
    }

    public static String fromCmd(int cmd) {
        for (EnumCommandType dir : EnumCommandType.values()) {
            if(dir.getCmd() == cmd) {
                return dir.getDesc();
            }
        }

        return "NOT_DEFINED";
    }
}
