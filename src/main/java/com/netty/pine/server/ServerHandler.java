package com.netty.pine.server;

import com.netty.pine.adapter.entity.UserEntity;
import com.netty.pine.adapter.handler.UserAdapterHandler;
import com.netty.pine.common.*;
import com.netty.pine.common.model.AuthLogin;
import com.netty.pine.common.protocol.PacketDataProto;
import com.netty.pine.game.GameSpace;
import com.netty.pine.game.GameUser;
import com.netty.pine.game.common.IChannelHandler;
import com.netty.pine.game.handler.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class ServerHandler extends ChannelInboundHandlerAdapter{

    public static Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    ChannelGroup channels =  new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private GameUser gameUser = null;

    @Override
    public void handlerAdded(ChannelHandlerContext context) throws Exception {
        super.handlerAdded(context);

        channels.add(context.channel());

        logger.info("[SERVER INFO] User {} have connect ú ú ú.... ", context.channel().remoteAddress().toString());

        // on authentication
        onAuthentication(context);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) throws Exception {
        context.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        context.close();
    }

//    @Override
//    public void channelUnregistered(ChannelHandlerContext context) throws Exception {
//        super.channelUnregistered(context);
//
//        gameUser.onDisconnect();
//    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object obj) throws Exception {
        PacketDataProto.PacketData packetData = (PacketDataProto.PacketData) obj;

        logger.warn("[SERVER INFO] Channel read data {}", packetData.toString());

        IChannelHandler channelHandler = null;

        if(packetData.getCmd() == EnumCommandType.AUTH_LOGIN.getCmd()) {
            gameUser = onLogin(context, packetData.getMsg());

            if(gameUser == null) {
                logger.error("[SERVER INFO] Game user is null ...!");
            }

            return;
        }

        gameUser.setPacketId(packetData.getPacketId());

        if(packetData.getCmd() == EnumCommandType.CMD_GET_USER_INFO.getCmd()) {
            channelHandler = new CmdGetUserInfo();
        } else if(packetData.getCmd() == EnumCommandType.CMD_GET_ROOM_LIST.getCmd()) {
            channelHandler = new CmdGetRoomList();
        } else if(packetData.getCmd() == EnumCommandType.CMD_FIND_PUBLIC_MATCH.getCmd()) {
            channelHandler = new CmdFindPublicMatch();
        } else if(packetData.getCmd() == EnumCommandType.CMD_READY_TO_PLAY.getCmd()) {
            channelHandler = new CmdReadyToPlay();
        } else if(packetData.getCmd() == EnumCommandType.CMD_FIND_FRIEND_MATCH.getCmd()) {
            channelHandler = new CmdFindFriendMatch();
        } else if(packetData.getCmd() == EnumCommandType.CMD_GET_LEADER_BOARD.getCmd()) {
            channelHandler = new CmdGetLeaderBoard();
        } else if(packetData.getCmd() == EnumCommandType.CMD_GET_AVAILABLE_MATCH.getCmd()) {
            channelHandler = new CmdGetAvailableMatch();
        } else {
            logger.warn("[SERVER INFO] Command request invalid {}", packetData.getCmd());
            return;
        }

        channelHandler.requestHandler(gameUser, packetData.getMsg());
    }

    private void onAuthentication(ChannelHandlerContext context) throws Exception {

        String valueToEnc = GameCommon.AUTHENTICATION_BASE_CODE + context.channel().id();

        try {
            String token = JwtUtil.getMD5HexString(valueToEnc);
            PacketDataProto.PacketData.Builder tokenRes = PacketDataProto.PacketData.newBuilder();

            HashMap<String, String> tokenMap = new HashMap<String, String>();
            tokenMap.put("token", token);

            tokenRes.setMsg(Utils.toJson(tokenMap));
            tokenRes.setCmd(EnumCommandType.AUTH_BORN.getCmd());
            tokenRes.setPacketId(0);

            context.writeAndFlush(tokenRes.build());

            logger.info("OnAuthentication() Send token authentication successful {}", Utils.toJson(tokenMap));

        } catch (Exception e) {
            logger.error("OnAuthentication() Exception = {}", e.getMessage());
        }
    }

    private GameUser onLogin(ChannelHandlerContext context, String jsonData) {

        logger.info("CmdLoginRequest() jsonData = {}", jsonData);

        PacketDataProto.PacketData.Builder authLoginRes = PacketDataProto.PacketData.newBuilder();

        try {

            AuthLogin authLogin = Utils.fromJson(jsonData, AuthLogin.class);

            String username = authLogin.getUserName();
            String token = authLogin.getToken();

            if(username.isEmpty() || token.isEmpty()) {
                logger.info("CmdLoginRequest() Username invalid = {}", username);

                authLoginRes.setResultStatus(EnumResultStatus.LOGIN_INVALID.getCode());
                context.writeAndFlush(authLoginRes.build());

                context.close();
                return null;
            }

            String baseToken = JwtUtil.getMD5HexString(GameCommon.AUTHENTICATION_BASE_CODE + context.channel().id());
            String password = token.substring(0, token.indexOf(baseToken));

            UserEntity userEntity = UserAdapterHandler.getInstante().getUserInfoEntity(username, password);
            if(userEntity == null) {
                logger.info("CmdLoginRequest() User info data not exist, username = {}", username);

                authLoginRes.setResultStatus(EnumResultStatus.LOGIN_INVALID.getCode());
                context.writeAndFlush(authLoginRes.build());

                context.close();
                return null;
            }

            //authLoginRes.setResultStatus(EnumResultStatus.SUCCESS.getCode());
            //context.writeAndFlush(authLoginRes.build());

            logger.info("CmdLoginRequest() Login successful ... !");

            gameUser = new GameUser(context, userEntity.getGameId(), userEntity.getUsername());
            // init data user controller
            gameUser.init();
            // add game space
            GameSpace.getInstance().addUser(gameUser);
            // login successful
            gameUser.onLogin();

            return gameUser;

        } catch (Exception e) {
            logger.error("CmdLoginRequest() Exception = {}", e.getMessage());
        }

        return  null;
    }

}
