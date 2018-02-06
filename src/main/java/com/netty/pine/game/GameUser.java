package com.netty.pine.game;

import com.netty.pine.adapter.entity.UserEntity;
import com.netty.pine.adapter.handler.UserAdapterHandler;
import com.netty.pine.common.*;
import com.netty.pine.common.model.UserInfo;
import com.netty.pine.common.protocol.PObject;
import com.netty.pine.common.protocol.PacketDataProto;
import com.netty.pine.game.common.IUser;
import com.netty.pine.game.common.UserSender;
import com.netty.pine.game.manager.RoomNoManager;
import io.netty.channel.ChannelHandlerContext;

import java.util.Comparator;

public class GameUser extends UserSender implements IUser {

    private UserInfo userInfo;
    private GameRoom room;
    private int gameType;
    /**
     * Game play
     */
    private int rank;
    private EnumPlayerState playerState;
    private int point;


    public GameUser(ChannelHandlerContext context, String gameId, String username) {
        super(context, gameId, username);
    }

    /**
     * Getter and Setter
     * @return
     */
    // ==========================================================================
    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public GameRoom getRoom() {
        return room;
    }

    public void setRoom(GameRoom room) {
        this.room = room;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public int getGameType() {
        return gameType;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public EnumPlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(EnumPlayerState playerState) {
        this.playerState = playerState;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void increasePoint() {
        this.point ++;
    }

    public void decreasePoint() {
        this.point --;
    }

    // =========================================================================

    @Override
    public void init() {

        initUserInfo();
    }

    @Override
    public void onLogin() throws InterruptedException {
        logger.info("onLogin() User login ...! {}", getUsername());

        onSendUserInfo();
    }

    @Override
    public GameRoom onCreateRoom() {
        logger.info("onCreateRoom() gameId = {}, username = {}", getGameId(), getUsername());

        // init game room
        GameRoom gameRoom = new GameRoom(RoomNoManager.getRoomNo());
        gameRoom.setMinUser(GameCommon.GAME_ROOM_MIN_PLAYER);
        gameRoom.setMaxUser(GameCommon.GAME_ROOM_MAX_PLAYER);

        gameRoom.addPlayer(this);

        // add room game space
        GameSpace.getInstance().addRoom(gameRoom);

        // remove if in game public
        GamePublic.getInstance().removePlayer(getGameId());

        this.setRoom(gameRoom);

        return gameRoom;
    }

    @Override
    public void onJoinRoom(int roomNo) {
        logger.info("onJoinRoom() gameId = {}, username = {}, roomNo = {}", getGameId(), getUsername(), roomNo);

        GameRoom gameRoom = GameSpace.getInstance().findRoom(roomNo);

        if(gameRoom == null) {
            logger.warn("onJoinRoom() Not found room with no {}", roomNo);
            return;
        }

        gameRoom.addPlayer(this);

        this.setRoom(gameRoom);

        // remove if in game public
        GamePublic.getInstance().removePlayer(getGameId());
    }

    @Override
    public void onFindRoom() {

    }

    @Override
    public boolean isInRoom() {
        return room != null;
    }

    public boolean isGamePublic() {
        return GamePublic.getInstance().containPlayer(getGameId());
    }

    @Override
    public void onDisconnect() {
        logger.info("onDisconnect() User disconnect ...!", getUsername());

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[gameId: ").append(getGameId()).append(", ");
        sb.append("username: ").append(getUsername()).append(", ");
        sb.append("nickname: ").append(getUserInfo().getNickName()).append("]");

        return sb.toString();
    }

    private void initUserInfo() {

        logger.info("initUserInfo() init user data ...! {}", getUsername());

        UserEntity userEntity = UserAdapterHandler.getInstante().getUserInfoByGameId(getGameId());
        if(userEntity == null) {
            return;
        }

        userInfo = new UserInfo();

        userInfo.setGameId(userEntity.getGameId());
        userInfo.setUserName(userEntity.getUsername());
        userInfo.setNickName(userEntity.getNickname());
        userInfo.setAvatar(userEntity.getAvatar());
        userInfo.setLoseNum(userEntity.getLoseNum());
        userInfo.setWinNum(userEntity.getWinNum());
        userInfo.setWorldRank(userEntity.getWorldRank());

        setPlayerState(EnumPlayerState.NONE);
    }


    private void onSendUserInfo() throws InterruptedException {

        if(userInfo == null) {
            logger.error("onSendUserInfo() UserInfo is null");
            return;
        }

        PObject pObject = new PObject();
        pObject.putUtfString(GameKeys.GAME_ID, getGameId());
        pObject.putUtfString(GameKeys.AVATAR, getUserInfo().getAvatar());
        pObject.putUtfString(GameKeys.NICK_NAME, getUserInfo().getNickName());
        pObject.putInt(GameKeys.NUM_WIN, 0);
        pObject.putInt(GameKeys.NUM_LOSE, 0);

        PacketDataProto.PacketData.Builder userInfoRes = PacketDataProto.PacketData.newBuilder();
        userInfoRes.setMsg(Utils.toJson(userInfo));
        userInfoRes.setPacketId(0);
        userInfoRes.setResultStatus(EnumResultStatus.SUCCESS.getCode());
        userInfoRes.setCmd(EnumCommandType.CMD_GET_USER_INFO.getCmd());

        sendPacket(userInfoRes.build());
    }

    /**
     * Game Function Common
     */
    public PObject toPObjectPlayer() {
        PObject pObjectResult = new PObject();

        pObjectResult.putUtfString(GameKeys.GAME_ID, getGameId());
        pObjectResult.putUtfString(GameKeys.AVATAR, getUserInfo().getAvatar());
        pObjectResult.putUtfString(GameKeys.NICK_NAME, getUserInfo().getNickName());
        pObjectResult.putInt(GameKeys.NUM_WIN, getUserInfo().getWinNum());
        pObjectResult.putInt(GameKeys.WORLD_RANK, getUserInfo().getLoseNum());

        return pObjectResult;
    }


    public static Comparator<GameUser> PlayerPointCardComparator_DESC = new Comparator<GameUser>() {

        public int compare(GameUser player1, GameUser player2) {

            return player2.getPoint() - player1.getPoint();
        }
    };

    public static Comparator<GameUser> PlayerRankCardComparator_ASC = new Comparator<GameUser>() {

        public int compare(GameUser player1, GameUser player2) {

            return player1.getRank() - player2.getRank();
        }
    };
}
