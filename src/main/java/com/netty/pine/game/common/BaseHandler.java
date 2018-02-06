package com.netty.pine.game.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netty.pine.common.EnumCommandType;
import com.netty.pine.common.protocol.PacketDataProto;
import com.netty.pine.game.GameUser;
import com.netty.pine.server.ServerHandler;
import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BaseHandler implements IChannelHandler {

    protected static Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    protected String generateMessage(Object... params) {
        StringBuilder result = new StringBuilder();
        for (Object param : params) {
            result.append(param).append("\t");
        }
        return result.toString();
    }

    protected void sendPacketToListUser(String command, List<GameUser> userList, PacketDataProto.PacketData.Builder res) throws InterruptedException {

        logger.info("[SERVER INFO] [{}] Send data to list user successful {}:{}", command, res.toString());

        for (GameUser user : userList) {
            user.sendPacket(res.build());
        }
    }

    protected void sendPacketToUser(GameUser gameUser, PacketDataProto.PacketData.Builder res) throws InterruptedException {
        logger.info("[SERVER INFO] [{}] Send data successful {};{}:{}", gameUser.getUsername(), EnumCommandType.fromCmd(res.getCmd()), res.toString());

        res.setPacketId(gameUser.getPacketId());

        gameUser.sendPacket(res.build());
    }

    /**
     * su dung co cac hashmap, arraylist, kieu du lieu so mac dinh la: Long hoac Double
     * @param json
     * @return
     */
    public static <T> T fromJson(String json) {
        return new Gson().fromJson(json, new TypeToken<T>() {
        }.getType());
    }

    public static <K, V> HashMap<K, V> fromJsonToHashMap(String json, Class<K> classKey, Class<V> classValue) {
        return new Gson().fromJson(json, new TypeToken<HashMap<K, V>>() {
        }.getType());
    }

    public static <T> ArrayList<T> fromJsonToArrayList(String json, Class<T> clazz) {
        return new Gson().fromJson(json, new TypeToken<ArrayList<T>>() {
        }.getType());
    }

    public static <T> T fromJson(String json, Class<T> clzz) {
        return new Gson().fromJson(json, clzz);
    }

    public static ArrayList<Integer> fromJsonToArrayListInteger(String json) {
        return new Gson().fromJson(json, new TypeToken<ArrayList<Integer>>() {
        }.getType());
    }

    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }

}
