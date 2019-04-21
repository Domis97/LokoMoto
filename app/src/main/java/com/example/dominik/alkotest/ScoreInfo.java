package com.example.dominik.alkotest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ScoreInfo implements Serializable {
    private String uID;
    private String score1;
    private String score2;


    public Map<String, Object> getScoreInfo() {
        Map<String, Object> scoreInfoMap = new HashMap<>();
        scoreInfoMap.put("uID",uID);
        scoreInfoMap.put("test1",score1);
        scoreInfoMap.put("test2",score2);
        return scoreInfoMap;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getScore1() {
        return score1;
    }

    public void setScore1(String score1) {
        this.score1 = score1;
    }

    public String getScore2() {
        return score2;
    }

    public void setScore2(String score2) {
        this.score2 = score2;
    }
}
