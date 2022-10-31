package com.karique.work.dev.healthtrack.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DistanceHistory {
    private int id;
    private int distance;
    private String recordDate;
    private int userId;
    private boolean active;

    public static List<DistanceHistory> from(JSONArray jsonArray){
        List<DistanceHistory> histories = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                histories.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return histories;
    }

    public static DistanceHistory from(JSONObject jsonObject){
        DistanceHistory history = new DistanceHistory();
        try {
            history.setId(jsonObject.getInt("id"));
            history.setDistance(jsonObject.getInt("distance"));
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
            jsonObject.put("distance", distance);
            jsonObject.put("recordDate", recordDate);
            jsonObject.put("userId", userId);
            jsonObject.put("active", active);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public DistanceHistory() {
    }

    public DistanceHistory(int distance, int userId) {
        this.distance = distance;
        this.userId = userId;
    }

    public DistanceHistory(int id, int distance, String recordDate, int userId, boolean active) {
        this.id = id;
        this.distance = distance;
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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
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
