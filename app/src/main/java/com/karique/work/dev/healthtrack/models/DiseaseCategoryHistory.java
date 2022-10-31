package com.karique.work.dev.healthtrack.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DiseaseCategoryHistory {
    private int id;
    private int userId;
    private int diseaseCategoryTypeId;
    private String recordDate;
    private boolean active;
    public DiseaseCategoryType diseaseCategoryType;

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("diseaseCategoryTypeId", diseaseCategoryTypeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static List<DiseaseCategoryHistory> from(JSONArray jsonArray){
        List<DiseaseCategoryHistory> diseaseCategoryHistories = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                diseaseCategoryHistories.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return diseaseCategoryHistories;
    }

    public static DiseaseCategoryHistory from(JSONObject jsonObject){
        DiseaseCategoryHistory diseaseCategoryHistory = new DiseaseCategoryHistory();
        try {
            diseaseCategoryHistory.setId(jsonObject.getInt("id"));
            diseaseCategoryHistory.setUserId(jsonObject.getInt("userId"));
            diseaseCategoryHistory.setDiseaseCategoryTypeId(jsonObject.getInt("diseaseCategoryTypeId"));
            diseaseCategoryHistory.setRecordDate(jsonObject.getString("recordDate"));
            diseaseCategoryHistory.setActive(jsonObject.getBoolean("active"));

            DiseaseCategoryType diseaseCategoryType = DiseaseCategoryType.from(jsonObject.getJSONObject("diseaseCategoryType"));
            diseaseCategoryHistory.setDiseaseCategoryType(diseaseCategoryType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return diseaseCategoryHistory;
    }

    public DiseaseCategoryHistory() {
    }

    public DiseaseCategoryHistory(int id, int userId, int diseaseCategoryTypeId, String recordDate, boolean active) {
        this.id = id;
        this.userId = userId;
        this.diseaseCategoryTypeId = diseaseCategoryTypeId;
        this.recordDate = recordDate;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDiseaseCategoryTypeId() {
        return diseaseCategoryTypeId;
    }

    public void setDiseaseCategoryTypeId(int diseaseCategoryTypeId) {
        this.diseaseCategoryTypeId = diseaseCategoryTypeId;
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

    public DiseaseCategoryType getDiseaseCategoryType() {
        return diseaseCategoryType;
    }

    public void setDiseaseCategoryType(DiseaseCategoryType diseaseCategoryType) {
        this.diseaseCategoryType = diseaseCategoryType;
    }
}
