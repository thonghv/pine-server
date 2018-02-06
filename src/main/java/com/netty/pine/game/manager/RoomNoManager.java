package com.netty.pine.game.manager;

public class RoomNoManager {

    private static Boolean[] rooms = new Boolean[1000];

    public static int getRoomNo() {
        for (int i = 0; i< rooms.length; i++) {
            if(rooms[i] == false) {
                rooms[i] = true;
                return i + 1;
            }
        }

        return -9999;
    }

    public static void removeRoomNo(int roomId) {
        if(rooms[roomId-1] == true) {
            rooms[roomId-1] = false;
        }
    }
}
