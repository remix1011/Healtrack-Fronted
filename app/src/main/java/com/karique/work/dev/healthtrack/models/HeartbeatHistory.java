package com.karique.work.dev.healthtrack.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HeartbeatHistory {
    private int id;
    private int averageBpm;
    private int userId;
    private String recordDate;
    private boolean active;

    public static List<HeartbeatHistory> from(JSONArray jsonArray){
        List<HeartbeatHistory> histories = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                histories.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return histories;
    }

    public static HeartbeatHistory from(JSONObject jsonObject){
        HeartbeatHistory history = new HeartbeatHistory();
        try {
            history.setId(jsonObject.getInt("id"));
            history.setAverageBpm(jsonObject.getInt("averageBpm"));
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
            jsonObject.put("averageBpm", averageBpm);
            jsonObject.put("recordDate", recordDate);
            jsonObject.put("userId", userId);
            jsonObject.put("active", active);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public HeartbeatHistory() {
    }

    public HeartbeatHistory(int averageBpm, int userId) {
        this.averageBpm = averageBpm;
        this.userId = userId;
    }

    public HeartbeatHistory(int id, int averageBpm, int userId, String recordDate, boolean active) {
        this.id = id;
        this.averageBpm = averageBpm;
        this.userId = userId;
        this.recordDate = recordDate;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAverageBpm() {
        return averageBpm;
    }

    public void setAverageBpm(int averageBpm) {
        this.averageBpm = averageBpm;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
