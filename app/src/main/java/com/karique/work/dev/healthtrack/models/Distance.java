package com.karique.work.dev.healthtrack.models;

import com.karique.work.dev.healthtrack.util.FuncionesFecha;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Distance {
    private int id;
    private double distancecm;
    private String registeredDate;
    private int userId;
    private double distanceGoal;
    private boolean achieved;
    private boolean active;

    public String getLabelChartDate() {
        SimpleDateFormat dayFormatter = new SimpleDateFormat("MM");
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MMM");

        Date date = FuncionesFecha.getDateWithHourFromString(registeredDate);
        Calendar _calendario = Calendar.getInstance();
        _calendario.setTime(date);

        return _calendario.get(Calendar.DAY_OF_MONTH) + "/" + monthFormatter.format(_calendario.getTime());
    }

    public static List<Distance> from(JSONArray jsonArray){
        List<Distance> distances = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                distances.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return distances;
    }

    public static Distance from(JSONObject jsonObject){
        Distance distance = new Distance();
        try {
            distance.setId(jsonObject.getInt("id"));
            distance.setDistancecm(jsonObject.getDouble("distancecm"));
            distance.setRegisteredDate(jsonObject.getString("registeredDate"));
            distance.setUserId(jsonObject.getInt("userId"));
            distance.setDistanceGoal(jsonObject.getDouble("distanceGoal"));
            distance.setAchieved(jsonObject.getBoolean("achieved"));
            distance.setActive(jsonObject.getBoolean("active"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return distance;
    }

    public Distance() {
    }

    public Distance(int id, double distancecm, String registeredDate, int userId, double distanceGoal, boolean achieved, boolean active) {
        this.id = id;
        this.distancecm = distancecm;
        this.registeredDate = registeredDate;
        this.userId = userId;
        this.distanceGoal = distanceGoal;
        this.achieved = achieved;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDistancecm() {
        return distancecm;
    }

    public void setDistancecm(double distancecm) {
        this.distancecm = distancecm;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getDistanceGoal() {
        return distanceGoal;
    }

    public void setDistanceGoal(double distanceGoal) {
        this.distanceGoal = distanceGoal;
    }

    public boolean isAchieved() {
        return achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
