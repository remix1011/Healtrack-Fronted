package com.karique.work.dev.healthtrack.models;

import org.json.JSONException;
import org.json.JSONObject;

public class DailyReport {
    private StepsHistory stepsHistory;
    private DistanceHistory distanceHistory;
    private CaloriesHistory caloriesHistory;
    private HeartbeatHistory heartbeatHistory;
    private SleepHistory sleepHistory;
    private WeightHistory weightHistory;

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("stepsHistory", stepsHistory.toJsonObject());
            jsonObject.put("distanceHistory", distanceHistory.toJsonObject());
            jsonObject.put("caloriesHistory", caloriesHistory.toJsonObject());
            jsonObject.put("heartbeatHistory", heartbeatHistory.toJsonObject());
            jsonObject.put("sleepHistory", sleepHistory.toJsonObject());
            jsonObject.put("weightHistory", weightHistory.toJsonObject());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public DailyReport() {
    }

    public DailyReport(StepsHistory stepsHistory, DistanceHistory distanceHistory, CaloriesHistory caloriesHistory, HeartbeatHistory heartbeatHistory, SleepHistory sleepHistory, WeightHistory weightHistory) {
        this.stepsHistory = stepsHistory;
        this.distanceHistory = distanceHistory;
        this.caloriesHistory = caloriesHistory;
        this.heartbeatHistory = heartbeatHistory;
        this.sleepHistory = sleepHistory;
        this.weightHistory = weightHistory;
    }

    public StepsHistory getStepsHistory() {
        return stepsHistory;
    }

    public void setStepsHistory(StepsHistory stepsHistory) {
        this.stepsHistory = stepsHistory;
    }

    public DistanceHistory getDistanceHistory() {
        return distanceHistory;
    }

    public void setDistanceHistory(DistanceHistory distanceHistory) {
        this.distanceHistory = distanceHistory;
    }

    public CaloriesHistory getCaloriesHistory() {
        return caloriesHistory;
    }

    public void setCaloriesHistory(CaloriesHistory caloriesHistory) {
        this.caloriesHistory = caloriesHistory;
    }

    public HeartbeatHistory getHeartbeatHistory() {
        return heartbeatHistory;
    }

    public void setHeartbeatHistory(HeartbeatHistory heartbeatHistory) {
        this.heartbeatHistory = heartbeatHistory;
    }

    public SleepHistory getSleepHistory() {
        return sleepHistory;
    }

    public void setSleepHistory(SleepHistory sleepHistory) {
        this.sleepHistory = sleepHistory;
    }

    public WeightHistory getWeightHistory() {
        return weightHistory;
    }

    public void setWeightHistory(WeightHistory weightHistory) {
        this.weightHistory = weightHistory;
    }
}
