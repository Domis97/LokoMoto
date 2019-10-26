package com.example.dominik.alkotest.firebase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreInfo implements Serializable {
    private String uID;
    private ArrayList<Double> score1;
    private String score2;


    public Map<String, Object> getScoreInfo() {
        Map<String, Object> scoreInfoMap = new HashMap<>();
        scoreInfoMap.put("uID",uID);
        scoreInfoMap.put("score1",score1);
        scoreInfoMap.put("score2",score2);
        return scoreInfoMap;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public ArrayList<Double> getScore1() {
        return score1;
    }

    public void setScore1(ArrayList<Double> score1) {
        this.score1 = score1;
    }

    public String getScore2() {
        return score2;
    }

    public void setScore2(String score2) {
        this.score2 = score2;
    }
}
