package com.netty.pine.game;

import com.netty.pine.game.common.IPublic;
import com.netty.pine.game.common.PublicSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GamePublic extends PublicSender implements IPublic {

    private static Logger logger = LoggerFactory.getLogger(GamePublic.class);

    private static GamePublic instance = null;

    private GamePublic() {}

    public static GamePublic getInstance() {
        if(instance == null) {
            instance = new GamePublic();
        }

        return instance;
    }

    @Override
    public void init() {

    }

    @Override
    public boolean addPlayer(GameUser player) {

        if(getUsers().containsKey(player.getGameId())) {
            return false;
        }

        getUsers().put(player.getGameId(), player);

        return true;
    }

    @Override
    public boolean removePlayer(String gameId) {

        if(!getUsers().containsKey(gameId)) {
            logger.warn("removePlayer() Player is not exist.");
            return false;
        }

        getUsers().remove(gameId);

        return true;
    }

    @Override
    public GameUser findPlayer(String gameId) {
        return getUsers().get(gameId);
    }

    public List<GameUser> getPlayers() {
        return (List<GameUser>) getUsers().values();
    }

    protected boolean containPlayer(String gamdId) {
        return getUsers().containsKey(gamdId);
    }

    public GameUser findPlayer(int rank) {

        List<GameUser> players = new ArrayList<>();

        for (GameUser player : getUsers().values()) {
            if(player.getRank() == rank) {
                players.add(player);
            }
        }

        if(!players.isEmpty()) {
            return players.get(0);
        }

        return null;
    }
}
