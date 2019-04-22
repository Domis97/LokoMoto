package com.example.dominik.alkotest;

import java.util.HashMap;
import java.util.Map;

public class VariableInfo {

    private String name;
    private String low;
    private String mid;
    private String high;


    @Override
    public String toString() {
        String info = name+low+mid+high;
        return info;
    }

    public Map<String, Object> getVarInfo() {
        Map<String, Object> vareInfoMap = new HashMap<>();
        vareInfoMap.put("name",name);
        vareInfoMap.put("low",low);
        vareInfoMap.put("mid",mid);
        vareInfoMap.put("high",high);
        return vareInfoMap;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }


}
