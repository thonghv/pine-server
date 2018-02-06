package com.netty.pine.common;

import com.netty.pine.common.protocol.PArray;
import com.netty.pine.common.protocol.PObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Convert {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(Convert.class);

    public static JSONObject toJson(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();

        for (String key : map.keySet()) {
            try {
                Object obj = map.get(key);
                if (obj instanceof Map) {
                    jsonObject.put(key, toJson((Map) obj));
                }
                else if (obj instanceof List) {
                    //TODO:
                }
                else if (obj instanceof PArray) {
                    jsonObject.put(key, toJson((PArray) obj));
                }
                else {
                    jsonObject.put(key, map.get(key));
                }
            }
            catch (JSONException jsone) {
                logger.error("RequestManager", "Failed to put value for " + key + " into JSONObject.", jsone);
            }
        }

        return jsonObject;
    }

    public static JSONObject toJson(PObject pObject) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        for(Map.Entry<String, Object> entry : pObject.getMapObject().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Integer) {
                jsonObject.put(entry.getKey(), (int) value);
                continue;
            }

            if (value instanceof String) {
                jsonObject.put(entry.getKey(), String.valueOf(value));
                continue;
            }
        }

        return jsonObject;
    }

    public static JSONArray toJson(PArray pArray) throws JSONException {
        JSONArray jsonArray = new JSONArray();

        List<Object> list = pArray.getListObject();
        for (Object obj : list) {
            if (obj instanceof Map) {
                jsonArray.put(toJson((Map) obj));
            }
            else if (obj instanceof List) {
                //jsonArray.put(toJson((List) obj));
            }
            else if (obj instanceof PObject) {
                jsonArray.put(toJson((PObject) obj));
            }
            else {
                jsonArray.put(obj);
            }
        }

        return jsonArray;
    }

    public static Map<String, Object> fromJson(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();

        Iterator<String> keyIterator = jsonObject.keys();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            try {
                Object obj = jsonObject.get(key);

                if (obj instanceof JSONObject) {
                    map.put(key, fromJson((JSONObject) obj));
                }
                else if (obj instanceof JSONArray) {
                    map.put(key, fromJson((JSONArray) obj));
                }
                else {
                    map.put(key, obj);
                }
            }
            catch (JSONException jsone) {
                logger.error("RequestManager", "Failed to get value for " + key + " from JSONObject.", jsone);
            }
        }

        return map;
    }

    public static List<Object> fromJson(JSONArray jsonArray) {
        List<Object> list = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Object obj = jsonArray.get(i);

                if (obj instanceof JSONObject) {
                    list.add(fromJson((JSONObject) obj));
                }
                else if (obj instanceof JSONArray) {
                    list.add(fromJson((JSONArray) obj));
                }
                else {
                    list.add(obj);
                }
            }
            catch (JSONException jsone) {
                logger.error("RequestManager", "Failed to get value at index " + i + " from JSONArray.", jsone);
            }
        }

        return list;
    }
}
