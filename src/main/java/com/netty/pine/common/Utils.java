package com.netty.pine.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class Utils {

    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }

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
}
