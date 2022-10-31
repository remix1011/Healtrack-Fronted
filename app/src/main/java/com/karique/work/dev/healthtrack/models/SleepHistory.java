package com.karique.work.dev.healthtrack.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SleepHistory {
    private int id;
    private int averageBpm;
    private String startDateTime;
    private String endDateTime;
    private String recordDate;
    private int userId;
    private boolean active;

    public static List<SleepHistory> from(JSONArray jsonArray){
        List<SleepHistory> histories = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                histories.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return histories;
    }

    public static SleepHistory from(JSONObject jsonObject){
        SleepHistory history = new SleepHistory();
        try {
            history.setId(jsonObject.getInt("id"));
            history.setAverageBpm(jsonObject.getInt("averageBpm"));
            history.setStartDateTime(jsonObject.getString("startDateTime"));
            history.setEndDateTime(jsonObject.getString("endDateTime"));
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
            jsonObject.put("startDateTime", startDateTime);
            jsonObject.put("endDateTime", endDateTime);
            jsonObject.put("recordDate", recordDate);
            jsonObject.put("userId", userId);
            jsonObject.put("active", active);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public SleepHistory() {
    }

    public SleepHistory(int averageBpm, String startDateTime, String endDateTime, int userId) {
        this.averageBpm = averageBpm;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.userId = userId;
    }

    public SleepHistory(int id, int averageBpm, String startDateTime, String endDateTime, String recordDate, int userId, boolean active) {
        this.id = id;
        this.averageBpm = averageBpm;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
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

    public int getAverageBpm() {
        return averageBpm;
    }

    public void setAverageBpm(int averageBpm) {
        this.averageBpm = averageBpm;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
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
