package com.karique.work.dev.healthtrack.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User {
    public static int USER_TYPE_SPECIALIST = 1;
    public static int USER_TYPE_RESPONSIBLE = 2;

    private int id;
    private String email;
    private String username;
    private String password;
    private String fullName;
    private String dni;
    private String birthday;
    private int userTypeId;
    private boolean active;

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("email", email);
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("fullName", fullName);
            jsonObject.put("dni", dni);
            jsonObject.put("birthday", birthday);
            jsonObject.put("userTypeId", userTypeId);
            jsonObject.put("active", active);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static List<User> from(JSONArray jsonArray){
        List<User> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                users.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return users;
    }

    public static User from(JSONObject jsonObject){
        User user = new User();
        try {
            user.setId(jsonObject.getInt("id"));
            user.setEmail(jsonObject.getString("email"));
            user.setUsername(jsonObject.getString("username"));
            user.setPassword(jsonObject.getString("password"));
            user.setFullName(jsonObject.getString("fullName"));
            user.setDni(jsonObject.getString("dni"));
            user.setBirthday(jsonObject.getString("birthday"));
            user.setUserTypeId(jsonObject.getInt("userTypeId"));
            user.setActive(jsonObject.getBoolean("active"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public User() {
    }

    public User(int id, String email, String username, String password, String fullName, String dni, String birthday, int userTypeId, boolean active) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.dni = dni;
        this.birthday = birthday;
        this.userTypeId = userTypeId;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isResponsible() {
        return userTypeId == USER_TYPE_RESPONSIBLE;
    }

    public boolean isSpecialist() {
        return userTypeId == USER_TYPE_SPECIALIST;
    }
}
