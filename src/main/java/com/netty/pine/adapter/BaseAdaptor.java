package com.netty.pine.adapter;

import com.netty.pine.server.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BaseAdaptor {

    protected static Logger logger = LoggerFactory.getLogger(BaseAdaptor.class);

    private static final String PERSISTENCE_UNIT_NAME = "PERSISTENCE";
    private static EntityManagerFactory factory;

    public static EntityManagerFactory getEntityManagerFactory() {
        try {
            if (factory == null) {
                factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            }
        }catch (Exception e) {
            logger.error("getEntityManagerFactory() Exception {}", e.getMessage());
        }

        return factory;
    }

    public static void shutdown() {
        if (factory != null) {
            factory.close();
        }
    }
}
