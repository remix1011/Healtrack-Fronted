package com.karique.work.dev.healthtrack.models;

import org.json.JSONException;
import org.json.JSONObject;

public class NewPasword {
    private int userId;
    private String newPassword;

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("newPassword", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public NewPasword() {
    }

    public NewPasword(int userId, String newPassword) {
        this.userId = userId;
        this.newPassword = newPassword;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
