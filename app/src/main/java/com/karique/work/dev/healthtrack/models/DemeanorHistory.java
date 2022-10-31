package com.karique.work.dev.healthtrack.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DemeanorHistory {
    private int id;
    private String demeanor;
    private String frecuency;
    private String intensity;
    private String recordDate;
    private int userId;
    private boolean active;

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("demeanor", demeanor);
            jsonObject.put("frecuency", frecuency);
            jsonObject.put("intensity", intensity);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static List<DemeanorHistory> from(JSONArray jsonArray){
        List<DemeanorHistory> demeanorHistories = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                demeanorHistories.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return demeanorHistories;
    }

    public static DemeanorHistory from(JSONObject jsonObject){
        DemeanorHistory demeanorHistory = new DemeanorHistory();
        try {
            demeanorHistory.setId(jsonObject.getInt("id"));
            demeanorHistory.setDemeanor(jsonObject.getString("demeanor"));
            demeanorHistory.setFrecuency(jsonObject.getString("frecuency"));
            demeanorHistory.setIntensity(jsonObject.getString("intensity"));
            demeanorHistory.setRecordDate(jsonObject.getString("recordDate"));
            demeanorHistory.setUserId(jsonObject.getInt("userId"));
            demeanorHistory.setActive(jsonObject.getBoolean("active"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return demeanorHistory;
    }

    public DemeanorHistory() {
    }

    public DemeanorHistory(int id, String demeanor, String frecuency, String intensity, String recordDate, int userId, boolean active) {
        this.id = id;
        this.demeanor = demeanor;
        this.frecuency = frecuency;
        this.intensity = intensity;
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

    public String getDemeanor() {
        return demeanor.isEmpty() ? "No hay datos" : demeanor;
    }

    public void setDemeanor(String demeanor) {
        this.demeanor = demeanor;
    }

    public String getFrecuency() {
        return frecuency.isEmpty() ? "No hay datos" : frecuency;
    }

    public void setFrecuency(String frecuency) {
        this.frecuency = frecuency;
    }

    public String getIntensity() {
        return intensity.isEmpty() ? "No hay datos" : intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
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
