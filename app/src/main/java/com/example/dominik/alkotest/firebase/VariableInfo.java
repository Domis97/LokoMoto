package com.example.dominik.alkotest.firebase;

import java.util.HashMap;
import java.util.Map;

public class VariableInfo {

    private String name;
    private String lowT1;
    private String midT1;
    private String highT1;
    private String lowT2;
    private String midT2;
    private String highT2;

    @Override
    public String toString() {
        return name + lowT1 + midT1 + highT1 + lowT2 + midT2 + highT2;
    }

    public Map<String, Object> getVarInfo() {
        Map<String, Object> vareInfoMap = new HashMap<>();
        vareInfoMap.put("name", name);
        vareInfoMap.put("lowT1", lowT1);
        vareInfoMap.put("midT1", midT1);
        vareInfoMap.put("highT1", highT1);
        vareInfoMap.put("lowT2", lowT2);
        vareInfoMap.put("midT2", midT2);
        vareInfoMap.put("highT2", highT2);
        return vareInfoMap;
    }

    public String getName() {
        return name;
    }

    public String getLowT1() {
        return lowT1;
    }

    public String getMidT1() {
        return midT1;
    }

    public String getHighT1() {
        return highT1;
    }

    public String getLowT2() {
        return lowT2;
    }

    public String getMidT2() {
        return midT2;
    }

    public String getHighT2() {
        return highT2;
    }


}
