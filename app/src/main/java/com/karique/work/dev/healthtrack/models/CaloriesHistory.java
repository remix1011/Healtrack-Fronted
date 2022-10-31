package com.karique.work.dev.healthtrack.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CaloriesHistory {
    private int id;
    private int calories;
    private String recordDate;
    private int userId;
    private boolean active;

    public static List<CaloriesHistory> from(JSONArray jsonArray){
        List<CaloriesHistory> histories = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                histories.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return histories;
    }

    public static CaloriesHistory from(JSONObject jsonObject){
        CaloriesHistory history = new CaloriesHistory();
        try {
            history.setId(jsonObject.getInt("id"));
            history.setCalories(jsonObject.getInt("calories"));
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
            jsonObject.put("calories", calories);
            jsonObject.put("recordDate", recordDate);
            jsonObject.put("userId", userId);
            jsonObject.put("active", active);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public CaloriesHistory() {
    }

    public CaloriesHistory(int calories, int userId) {
        this.calories = calories;
        this.userId = userId;
    }

    public CaloriesHistory(int id, int calories, String recordDate, int userId, boolean active) {
        this.id = id;
        this.calories = calories;
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

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
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
