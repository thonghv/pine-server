package com.netty.pine.game.common;

import com.netty.pine.common.EnumCommandType;
import com.netty.pine.common.protocol.PacketDataProto;
import com.netty.pine.server.ServerHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public abstract class UserSender {

    protected static Logger logger = LoggerFactory.getLogger(UserSender.class);

    private ChannelHandlerContext context;
    private HashMap<String, Object> cacheData;

    private int packetId;
    private EnumCommandType lastRequestCommand;

    private String gameId;
    private String username;

    public UserSender(ChannelHandlerContext context, String gameId, String username) {
        this.context = context;
        this.gameId = gameId;
        this.username = username;

        this.cacheData = new HashMap<String, Object>();
    }

    /**
     * Get and setter
     * @return
     */
    // ======================================================================
    public int getPacketId() {
        return packetId;
    }

    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public EnumCommandType getLastRequestCommand() {
        return lastRequestCommand;
    }

    public void setLastRequestCommand(EnumCommandType lastRequestCommand) {
        this.lastRequestCommand = lastRequestCommand;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public HashMap<String, Object> getCacheData() {
        return cacheData;
    }

    public void setCacheData(HashMap<String, Object> cacheData) {
        this.cacheData = cacheData;
    }

    // ======================================================================

    /**
     * Send packket data
     * @param packetData
     * @throws InterruptedException
     */
    public void sendPacket(PacketDataProto.PacketData packetData) throws InterruptedException {

        ChannelFuture channelFuture = context.writeAndFlush(packetData);

        //channelFuture.await();
    }
}


