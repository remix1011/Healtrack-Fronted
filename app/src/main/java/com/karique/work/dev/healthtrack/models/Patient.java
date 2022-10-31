package com.karique.work.dev.healthtrack.models;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Patient {
    public static String SIN_DATOS = "SIN DATOS";

    private int id;
    private String email;
    private String username;
    private String fullName;
    private String dni;
    private String birthday;
    private int userTypeId;
    private String clinicName;
    private String enviromentName;
    private String diseaseCategoryDescription;
    private String treatmentDescription;
    private String demeanor;
    private int edad;

    public static Patient from(Bundle bundle){
        Patient patient = new Patient();
        patient.setId(bundle.getInt("id"));
        patient.setEmail(bundle.getString("email"));
        patient.setUsername(bundle.getString("username"));
        patient.setFullName(bundle.getString("fullName"));
        patient.setDni(bundle.getString("dni"));
        patient.setBirthday(bundle.getString("birthday"));
        patient.setUserTypeId(bundle.getInt("userTypeId"));
        patient.setClinicName(bundle.getString("clinicName"));
        patient.setEnviromentName(bundle.getString("enviromentName"));
        patient.setDiseaseCategoryDescription(bundle.getString("diseaseCategoryDescription"));
        patient.setTreatmentDescription(bundle.getString("treatmentDescription"));
        patient.setDemeanor(bundle.getString("demeanor"));
        patient.setEdad(bundle.getInt("edad"));

        return patient;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("email", email);
        bundle.putString("username", username);
        bundle.putString("fullName", fullName);
        bundle.putString("dni", dni);
        bundle.putString("birthday", birthday);
        bundle.putInt("userTypeId", userTypeId);
        bundle.putString("clinicName", clinicName);
        bundle.putString("enviromentName", enviromentName);
        bundle.putString("diseaseCategoryDescription", diseaseCategoryDescription);
        bundle.putString("treatmentDescription", treatmentDescription);
        bundle.putString("demeanor", demeanor);
        bundle.putInt("edad", edad);

        return bundle;
    }

    public static List<Patient> from(JSONArray jsonArray){
        List<Patient> patients = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                patients.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return patients;
    }

    public static Patient from(JSONObject jsonObject){
        Patient patient = new Patient();
        try {
            patient.setId(jsonObject.getInt("id"));
            patient.setEmail(jsonObject.getString("email"));
            patient.setUsername(jsonObject.getString("username"));
            patient.setFullName(jsonObject.getString("fullName"));
            patient.setDni(jsonObject.getString("dni"));
            patient.setBirthday(jsonObject.getString("birthday"));
            patient.setUserTypeId(jsonObject.getInt("userTypeId"));
            patient.setClinicName(jsonObject.getString("clinicName"));
            patient.setEnviromentName(jsonObject.getString("enviromentName"));
            patient.setDiseaseCategoryDescription(jsonObject.getString("diseaseCategoryDescription"));
            patient.setTreatmentDescription(jsonObject.getString("treatmentDescription"));
            patient.setDemeanor(jsonObject.getString("demeanor"));
            patient.setEdad(jsonObject.getInt("edad"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return patient;
    }

    public Patient() {
    }

    public Patient(int id, String email, String username, String fullName, String dni, String birthday, int userTypeId, String clinicName, String enviromentName, String diseaseCategoryDescription, String treatmentDescription, String demeanor) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.fullName = fullName;
        this.dni = dni;
        this.birthday = birthday;
        this.userTypeId = userTypeId;
        this.clinicName = clinicName;
        this.enviromentName = enviromentName;
        this.diseaseCategoryDescription = diseaseCategoryDescription;
        this.treatmentDescription = treatmentDescription;
        this.demeanor = demeanor;
    }

    public static String getSinDatos() {
        return SIN_DATOS;
    }

    public static void setSinDatos(String sinDatos) {
        SIN_DATOS = sinDatos;
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

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getEnviromentName() {
        return enviromentName;
    }

    public void setEnviromentName(String enviromentName) {
        this.enviromentName = enviromentName;
    }

    public String getDiseaseCategoryDescription() {
        return diseaseCategoryDescription;
    }

    public void setDiseaseCategoryDescription(String diseaseCategoryDescription) {
        this.diseaseCategoryDescription = diseaseCategoryDescription;
    }

    public String getTreatmentDescription() {
        return treatmentDescription;
    }

    public void setTreatmentDescription(String treatmentDescription) {
        this.treatmentDescription = treatmentDescription;
    }

    public String getDemeanor() {
        return demeanor;
    }

    public void setDemeanor(String demeanor) {
        this.demeanor = demeanor;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public User getUser() {
        User user = new User();
        user.setId(getId());
        user.setEmail(getEmail());
        user.setUsername(getUsername());
        user.setPassword("");
        user.setFullName(getFullName());
        user.setDni(getDni());
        user.setBirthday(getBirthday());
        user.setUserTypeId(getUserTypeId());
        user.setActive(true);

        return user;
    }
}
