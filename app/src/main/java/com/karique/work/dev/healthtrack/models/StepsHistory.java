package com.karique.work.dev.healthtrack.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StepsHistory {
    private int id;
    private int steps;
    private String recordDate;
    private int userId;
    private boolean active;

    public static List<StepsHistory> from(JSONArray jsonArray){
        List<StepsHistory> histories = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                histories.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return histories;
    }

    public static StepsHistory from(JSONObject jsonObject){
        StepsHistory history = new StepsHistory();
        try {
            history.setId(jsonObject.getInt("id"));
            history.setSteps(jsonObject.getInt("steps"));
            history.setRecordDate(jsonObject.getString("recordDate"));
            history.setUserId(jsonObject.getInt("userId"));
            history.setActive(jsonObject.getBoolean("active"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return history;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("steps", steps);
            jsonObject.put("recordDate", recordDate);
            jsonObject.put("userId", userId);
            jsonObject.put("active", active);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public StepsHistory() {
    }


    public StepsHistory(int steps, int userId) {
        this.steps = steps;
        this.userId = userId;
    }

    public StepsHistory(int id, int steps, String recordDate, int userId, boolean active) {
        this.id = id;
        this.steps = steps;
        this.recordDate = recordDate;
        this.userId = userId;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
