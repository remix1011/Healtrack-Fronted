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

public class Step {
    private int id;
    private int countedSteps;
    private String registeredDate;
    private int userId;
    private int stepsGoal;
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

    public static List<Step> from(JSONArray jsonArray){
        List<Step> steps = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                steps.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return steps;
    }

    public static Step from(JSONObject jsonObject){
        Step step = new Step();
        try {
            step.setId(jsonObject.getInt("id"));
            step.setCountedSteps(jsonObject.getInt("countedSteps"));
            step.setRegisteredDate(jsonObject.getString("registeredDate"));
            step.setUserId(jsonObject.getInt("userId"));
            step.setStepsGoal(jsonObject.getInt("stepsGoal"));
            step.setAchieved(jsonObject.getBoolean("achieved"));
            step.setActive(jsonObject.getBoolean("active"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return step;
    }

    public Step() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountedSteps() {
        return countedSteps;
    }

    public void setCountedSteps(int countedSteps) {
        this.countedSteps = countedSteps;
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

    public int getStepsGoal() {
        return stepsGoal;
    }

    public void setStepsGoal(int stepsGoal) {
        this.stepsGoal = stepsGoal;
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
