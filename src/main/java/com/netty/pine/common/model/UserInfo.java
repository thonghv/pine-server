package com.netty.pine.common.model;

public class UserInfo {

    private String gameId;
    private String userName;
    private String nickName;
    private String avatar;
    private int winNum;
    private int loseNum;
    private int worldRank;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getWinNum() {
        return winNum;
    }

    public void setWinNum(int winNum) {
        this.winNum = winNum;
    }

    public void setLoseNum(int loseNum) {
        this.loseNum = loseNum;
    }

    public int getLoseNum() {
        return loseNum;
    }

    public int getWorldRank() {
        return worldRank;
    }

    public void setWorldRank(int worldRank) {
        this.worldRank = worldRank;
    }

    public void increaseWinNum() {
        this.winNum ++;
        this.worldRank ++;
    }

    public void increaseLoseNum() {
        this.loseNum ++;
        this.worldRank --;
    }
}
