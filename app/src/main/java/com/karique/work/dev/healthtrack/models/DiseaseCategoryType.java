package com.karique.work.dev.healthtrack.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DiseaseCategoryType {
    private int id;
    private String description;
    private boolean active;

    public static List<DiseaseCategoryType> from(JSONArray jsonArray){
        List<DiseaseCategoryType> diseaseCategoryTypes = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                diseaseCategoryTypes.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return diseaseCategoryTypes;
    }

    public static DiseaseCategoryType from(JSONObject jsonObject){
        DiseaseCategoryType diseaseCategoryType = new DiseaseCategoryType();
        try {
            diseaseCategoryType.setId(jsonObject.getInt("id"));
            diseaseCategoryType.setDescription(jsonObject.getString("description"));
            diseaseCategoryType.setActive(jsonObject.getBoolean("active"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return diseaseCategoryType;
    }

    public DiseaseCategoryType() {
    }

    public DiseaseCategoryType(int id, String description, boolean active) {
        this.id = id;
        this.description = description;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return description;
    }
}
