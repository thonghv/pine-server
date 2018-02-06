package com.netty.pine.common.protocol;

import com.netty.pine.common.Convert;
import java.util.HashMap;

public class PObject {

    private HashMap<String, Object> mapObject = new HashMap<>();

    public HashMap<String, Object> getMapObject() {
        return mapObject;
    }

    public void putUtfString(String key, String value) {
        mapObject.put(key, value);
    }

    public void putInt(String key, int value) {
        mapObject.put(key, value);
    }

    public void putLong(String key, long value) {
        mapObject.put(key, value);
    }

    public void putPArray(String key, PArray value) {
        mapObject.put(key, value);
    }

    public String getUtfString(String key) {
        if(!mapObject.containsKey(key)) {
            return null;
        }
        return String.valueOf(mapObject.get(key));
    }

    public int getInt(String key) {
        if(!mapObject.containsKey(key)) {
            return -999;
        }

        return (int) mapObject.get(key);
    }

    public long getLong(String key) {
        if(!mapObject.containsKey(key)) {
            return -0L;
        }

        return (long) mapObject.get(key);
    }

    public PArray getPArray(String key) {
        if(!mapObject.containsKey(key)) {
            return null;
        }

        return (PArray) mapObject.get(key);
    }

    public String toJson() {

        return Convert.toJson(getMapObject()).toString();
    }

}
