package com.karique.work.dev.healthtrack.models;

import org.json.JSONException;
import org.json.JSONObject;

public class UserMetricsDetails {
    private StepsHistory stepsHistory;
    private DistanceHistory distanceHistory;
    private CaloriesHistory caloriesHistory;
    private HeartbeatHistory heartbeatHistory;
    private SleepHistory sleepHistory;
    private WeightHistory weightHistory;
    private Oximetry oximetry;

    public static UserMetricsDetails from(JSONObject jsonObject){
        UserMetricsDetails userMetricsDetails = new UserMetricsDetails();
        StepsHistory stepsHistory = null;
        DistanceHistory distanceHistory = null;
        CaloriesHistory caloriesHistory = null;
        HeartbeatHistory heartbeatHistory = null;
        SleepHistory sleepHistory = null;
        WeightHistory weightHistory = null;
        Oximetry oximetry = null;

        try {
            stepsHistory = StepsHistory.from(jsonObject.getJSONObject("stepsHistory"));
            userMetricsDetails.setStepsHistory(stepsHistory);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            distanceHistory = DistanceHistory.from(jsonObject.getJSONObject("distanceHistory"));
            userMetricsDetails.setDistanceHistory(distanceHistory);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            caloriesHistory = CaloriesHistory.from(jsonObject.getJSONObject("caloriesHistory"));
            userMetricsDetails.setCaloriesHistory(caloriesHistory);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            heartbeatHistory = HeartbeatHistory.from(jsonObject.getJSONObject("heartbeatHistory"));
            userMetricsDetails.setHeartbeatHistory(heartbeatHistory);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            sleepHistory = SleepHistory.from(jsonObject.getJSONObject("sleepHistory"));
            userMetricsDetails.setSleepHistory(sleepHistory);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            weightHistory = WeightHistory.from(jsonObject.getJSONObject("weightHistory"));
            userMetricsDetails.setWeightHistory(weightHistory);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            oximetry = Oximetry.from(jsonObject.getJSONObject("oximetry"));
            userMetricsDetails.setOximetry(oximetry);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userMetricsDetails;
    }

    public UserMetricsDetails() {
    }

    public UserMetricsDetails(StepsHistory stepsHistory, DistanceHistory distanceHistory, CaloriesHistory caloriesHistory, HeartbeatHistory heartbeatHistory, SleepHistory sleepHistory, WeightHistory weightHistory, Oximetry oximetry) {
        this.stepsHistory = stepsHistory;
        this.distanceHistory = distanceHistory;
        this.caloriesHistory = caloriesHistory;
        this.heartbeatHistory = heartbeatHistory;
        this.sleepHistory = sleepHistory;
        this.weightHistory = weightHistory;
        this.oximetry = oximetry;
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

    public Oximetry getOximetry() {
        return oximetry;
    }

    public void setOximetry(Oximetry oximetry) {
        this.oximetry = oximetry;
    }
}
