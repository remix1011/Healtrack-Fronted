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

public class Calorie {
    private int id;
    private double calories;
    private String registeredDate;
    private int userId;
    private double caloriesGoal;
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

    public static List<Calorie> from(JSONArray jsonArray){
        List<Calorie> calories = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                calories.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return calories;
    }

    public static Calorie from(JSONObject jsonObject){
        Calorie calorie = new Calorie();
        try {
            calorie.setId(jsonObject.getInt("id"));
            calorie.setCalories(jsonObject.getDouble("calories"));
            calorie.setRegisteredDate(jsonObject.getString("registeredDate"));
            calorie.setUserId(jsonObject.getInt("userId"));
            calorie.setCaloriesGoal(jsonObject.getDouble("caloriesGoal"));
            calorie.setAchieved(jsonObject.getBoolean("achieved"));
            calorie.setActive(jsonObject.getBoolean("active"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return calorie;
    }

    public Calorie() {
    }

    public Calorie(int id, double calories, String registeredDate, int userId, double caloriesGoal, boolean achieved, boolean active) {
        this.id = id;
        this.calories = calories;
        this.registeredDate = registeredDate;
        this.userId = userId;
        this.caloriesGoal = caloriesGoal;
        this.achieved = achieved;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
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

    public double getCaloriesGoal() {
        return caloriesGoal;
    }

    public void setCaloriesGoal(double caloriesGoal) {
        this.caloriesGoal = caloriesGoal;
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
