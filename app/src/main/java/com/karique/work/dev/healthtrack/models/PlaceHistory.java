package com.karique.work.dev.healthtrack.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaceHistory {
    private int id;
    private String lat;
    private String lng;
    private String recordDate;
    private int userId;
    private boolean active;

    public static List<PlaceHistory> from(JSONArray jsonArray){
        List<PlaceHistory> placeHistories = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                placeHistories.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return placeHistories;
    }

    public static PlaceHistory from(JSONObject jsonObject){
        PlaceHistory placeHistory = new PlaceHistory();
        try {
            placeHistory.setId(jsonObject.getInt("id"));
            placeHistory.setLat(jsonObject.getString("lat"));
            placeHistory.setLng(jsonObject.getString("lng"));
            placeHistory.setRecordDate(jsonObject.getString("recordDate"));
            placeHistory.setUserId(jsonObject.getInt("userId"));
            placeHistory.setActive(jsonObject.getBoolean("active"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return placeHistory;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("lat", lat);
            jsonObject.put("lng", lng);
            jsonObject.put("recordDate", recordDate);
            jsonObject.put("userId", userId);
            jsonObject.put("active", active);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public PlaceHistory(String lat, String lng, int userId) {
        this.lat = lat;
        this.lng = lng;
        this.userId = userId;
    }

    public PlaceHistory() {
    }

    public PlaceHistory(int id, String lat, String lng, String recordDate, int userId, boolean active) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
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
