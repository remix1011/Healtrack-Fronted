package com.karique.work.dev.healthtrack.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecommendationsHistory {
    private int id;
    private String description;
    private int userId;
    private String recordDate;
    private boolean active;

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("description", description);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static List<RecommendationsHistory> from(JSONArray jsonArray){
        List<RecommendationsHistory> treatmentHistories = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                treatmentHistories.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return treatmentHistories;
    }

    public static RecommendationsHistory from(JSONObject jsonObject){
        RecommendationsHistory recommendationsHistory = new RecommendationsHistory();
        try {
            recommendationsHistory.setId(jsonObject.getInt("id"));
            recommendationsHistory.setDescription(jsonObject.getString("description"));
            recommendationsHistory.setUserId(jsonObject.getInt("userId"));
            recommendationsHistory.setRecordDate(jsonObject.getString("recordDate"));
            recommendationsHistory.setActive(jsonObject.getBoolean("active"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recommendationsHistory;
    }
    
    public RecommendationsHistory() {
    }

    public RecommendationsHistory(int id, String description, int userId, String recordDate, boolean active) {
        this.id = id;
        this.description = description;
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

    public String getDescription() {
        return description.isEmpty() ? "No hay datos" : description;
    }

    public void setDescription(String description) {
        this.description = description;
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
