package com.karique.work.dev.healthtrack.models;

import org.json.JSONException;
import org.json.JSONObject;

public class NewPasswordEmail {
    private String email;
    private String dni;

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("dni", dni);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public NewPasswordEmail() {
    }

    public NewPasswordEmail(String email, String dni) {
        this.email = email;
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

}
