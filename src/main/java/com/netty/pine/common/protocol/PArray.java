package com.netty.pine.common.protocol;

import com.netty.pine.common.Utils;

import java.util.ArrayList;
import java.util.List;

public class PArray {

    private List<Object> listObject = new ArrayList<>();

    public List<Object> getListObject() {
        return listObject;
    }

    public void addString(String value) {
        listObject.add(value);
    }

    public void addPObject(Object value) {
        listObject.add(value);
    }

    public String toJson() {
        return Utils.toJson(listObject);
    }

}
