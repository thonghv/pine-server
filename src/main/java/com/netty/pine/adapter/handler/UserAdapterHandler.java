package com.netty.pine.adapter.handler;

import com.netty.pine.adapter.BaseAdaptor;
import com.netty.pine.adapter.entity.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

public class UserAdapterHandler extends BaseAdaptor{

    private static UserAdapterHandler instante;
    private UserAdapterHandler() {

    }

    public static UserAdapterHandler getInstante() {
        if(instante == null) {
            instante = new UserAdapterHandler();
        }

        return instante;
    }

    public UserEntity getUserInfoEntity(String username, String password) {

        EntityManager em = getEntityManagerFactory().createEntityManager();

        try {
            Query queryUser = em.createQuery("SELECT u FROM UserEntity u WHERE u.username = :username AND u.password = :password")
                    .setParameter("username", username)
                    .setParameter("password", password);
            List<UserEntity> userEntityList = queryUser.getResultList();
            if(userEntityList.isEmpty() || userEntityList.size() > 1) {
                logger.error("getUserInfoEntity() Error! have one more user");
                return null;
            }

            return userEntityList.get(0);

        } catch (Exception ex) {
            logger.error("getUserInfoEntity() Exception {}", ex.getMessage());
        } finally {
            em.close();
        }

        return  null;
    }

    public UserEntity getUserInfoByGameId(String gameId) {

        EntityManager em = getEntityManagerFactory().createEntityManager();

        try {
            Query queryUser = em.createQuery("SELECT u FROM UserEntity u WHERE u.gameId = :gameId")
                    .setParameter("gameId", gameId);
            List<UserEntity> userEntityList = queryUser.getResultList();
            if(userEntityList.isEmpty() || userEntityList.size() > 1) {
                logger.error("getUserInfoEntity() Error! have one more user");
                return null;
            }

            return userEntityList.get(0);

        } catch (Exception ex) {
            logger.error("getUserInfoEntity() Exception {}", ex.getMessage());
        } finally {
            em.close();
        }

        return  null;
    }

    public UserEntity createUser(UserEntity userEntity) {

        EntityManager em = getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            em.persist(userEntity);
            transaction.commit();

        } catch (Exception ex) {
            logger.error("getUserInfoEntity() Exception {}", ex.getMessage());
            transaction.rollback();
            return null;
        } finally {
            em.close();
        }

        return userEntity;
    }

    public boolean updateUserPassword(String gameId, String password) {

        EntityManager em = getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Query query = em.createQuery("UPDATE UserEntity SET password = :password WHERE gameId = :gameId")
                    .setParameter("password", password)
                    .setParameter("gameId", gameId);
            int updateCount = query.executeUpdate();

            if(updateCount > 0) {
                transaction.commit();
                return true;
            }

        } catch (Exception ex) {
            logger.error("updateUserPassword() Exception {}", ex.getMessage());
            transaction.rollback();
            return false;
        } finally {
            em.close();
        }

        return true;
    }

    public boolean updateUserWinNum(String gameId, int winNum, int worldRank) {

        EntityManager em = getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Query query = em.createQuery("UPDATE UserEntity SET winNum = :winNum, worldRank =:worldRank WHERE gameId = :gameId")
                    .setParameter("winNum", winNum)
                    .setParameter("worldRank", worldRank)
                    .setParameter("gameId", gameId);
            int updateCount = query.executeUpdate();

            if(updateCount > 0) {
                transaction.commit();
                return true;
            }

        } catch (Exception ex) {
            logger.error("updateUserWinNum() Exception {}", ex.getMessage());
            transaction.rollback();
            return false;
        } finally {
            em.close();
        }

        return true;
    }

    public boolean updateUserLoseNum(String gameId, int loseNum, int worldRank) {

        EntityManager em = getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Query query = em.createQuery("UPDATE UserEntity SET loseNum = :loseNum, worldRank =:worldRank WHERE gameId = :gameId")
                    .setParameter("loseNum", loseNum)
                    .setParameter("worldRank", worldRank)
                    .setParameter("gameId", gameId);
            int updateCount = query.executeUpdate();

            if(updateCount > 0) {
                transaction.commit();
                return true;
            }

        } catch (Exception ex) {
            logger.error("updateUserLoseNum() Exception {}", ex.getMessage());
            transaction.rollback();
            return false;
        } finally {
            em.close();
        }

        return true;
    }

    public boolean removeUser(String gameId) {

        EntityManager em = getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Query query = em.createQuery("DELETE FROM UserEntity WHERE gameId = :gameId")
                    .setParameter("gameId", gameId);
            int updateCount = query.executeUpdate();

            if(updateCount > 0) {
                transaction.commit();
                return true;
            }

        } catch (Exception ex) {
            logger.error("removeUser() Exception {}", ex.getMessage());
            transaction.rollback();
            return false;
        } finally {
            em.close();
        }

        return true;
    }

    public List<UserEntity> getLeaderBoard(int limit) {

        EntityManager em = getEntityManagerFactory().createEntityManager();

        try {
            Query queryUser = em.createQuery("SELECT u FROM UserEntity u ORDER BY u.worldRank DESC LIMIT :limit")
                    .setParameter("limit", limit);
            List<UserEntity> userEntityList = queryUser.getResultList();
            if(userEntityList.isEmpty()) {
                logger.warn("getLeaderBoard() No one more user");
                return null;
            }

            return userEntityList;

        } catch (Exception ex) {
            logger.error("getUserInfoEntity() Exception {}", ex.getMessage());
        } finally {
            em.close();
        }

        return  null;
    }
}
