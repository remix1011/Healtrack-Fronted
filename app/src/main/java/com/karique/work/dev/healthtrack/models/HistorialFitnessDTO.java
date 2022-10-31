package com.karique.work.dev.healthtrack.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistorialFitnessDTO {
    public List<Step> steps = new ArrayList<>();
    public List<Distance> distances = new ArrayList<>();
    public List<Calorie> calories = new ArrayList<>();

    public static HistorialFitnessDTO from(JSONObject jsonObject){
        HistorialFitnessDTO historialFitnessDTO = new HistorialFitnessDTO();
        try {
            historialFitnessDTO.setSteps(Step.from(jsonObject.getJSONArray("steps")));
            historialFitnessDTO.setCalories(Calorie.from(jsonObject.getJSONArray("calories")));
            historialFitnessDTO.setDistances(Distance.from(jsonObject.getJSONArray("distances")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return historialFitnessDTO;
    }

    public HistorialFitnessDTO() {
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<Distance> getDistances() {
        return distances;
    }

    public void setDistances(List<Distance> distances) {
        this.distances = distances;
    }

    public List<Calorie> getCalories() {
        return calories;
    }

    public void setCalories(List<Calorie> calories) {
        this.calories = calories;
    }
}
