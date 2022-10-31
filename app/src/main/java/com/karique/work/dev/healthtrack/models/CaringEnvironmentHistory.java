package com.karique.work.dev.healthtrack.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CaringEnvironmentHistory {
    private int id;
    private String clinicName;
    private String enviromentName;
    private int userId;
    private String recordDate;
    private boolean active;

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("clinicName", clinicName);
            jsonObject.put("enviromentName", enviromentName);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static List<CaringEnvironmentHistory> from(JSONArray jsonArray){
        List<CaringEnvironmentHistory> caringEnvironmentHistories = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                caringEnvironmentHistories.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return caringEnvironmentHistories;
    }

    public static CaringEnvironmentHistory from(JSONObject jsonObject){
        CaringEnvironmentHistory caringEnvironmentHistory = new CaringEnvironmentHistory();
        try {
            caringEnvironmentHistory.setId(jsonObject.getInt("id"));
            caringEnvironmentHistory.setClinicName(jsonObject.getString("clinicName"));
            caringEnvironmentHistory.setEnviromentName(jsonObject.getString("enviromentName"));
            caringEnvironmentHistory.setUserId(jsonObject.getInt("userId"));
            caringEnvironmentHistory.setRecordDate(jsonObject.getString("recordDate"));
            caringEnvironmentHistory.setActive(jsonObject.getBoolean("active"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return caringEnvironmentHistory;
    }

    public CaringEnvironmentHistory() {
    }

    public CaringEnvironmentHistory(int id, String clinicName, String enviromentName, int userId, String recordDate, boolean active) {
        this.id = id;
        this.clinicName = clinicName;
        this.enviromentName = enviromentName;
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

    public String getClinicName() {
        return clinicName.isEmpty() ? "No hay datos" : clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getEnviromentName() {
        return enviromentName.isEmpty() ? "No hay datos" : enviromentName;
    }

    public void setEnviromentName(String enviromentName) {
        this.enviromentName = enviromentName;
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
