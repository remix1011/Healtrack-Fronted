package com.karique.work.dev.healthtrack.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserCaringDetails {
    private CaringEnvironmentHistory caringEnvironment;
    private DiseaseCategoryHistory diseaseCategory;
    private TreatmentHistory treatment;
    private DemeanorHistory demeanor;
    private RecommendationsHistory recommendations;

    public static List<UserCaringDetails> from(JSONArray jsonArray){
        List<UserCaringDetails> patients = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                patients.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return patients;
    }

    public static UserCaringDetails from(JSONObject jsonObject){
        UserCaringDetails userCaringDetails = new UserCaringDetails();
        try {
            CaringEnvironmentHistory caringEnvironmentHistory = CaringEnvironmentHistory.from(jsonObject.getJSONObject("caringEnvironment"));
            userCaringDetails.setCaringEnvironment(caringEnvironmentHistory);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            DiseaseCategoryHistory diseaseCategoryHistory = DiseaseCategoryHistory.from(jsonObject.getJSONObject("diseaseCategory"));
            userCaringDetails.setDiseaseCategory(diseaseCategoryHistory);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            TreatmentHistory treatmentHistory = TreatmentHistory.from(jsonObject.getJSONObject("treatment"));
            userCaringDetails.setTreatment(treatmentHistory);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            DemeanorHistory demeanorHistory = DemeanorHistory.from(jsonObject.getJSONObject("demeanor"));
            userCaringDetails.setDemeanor(demeanorHistory);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            RecommendationsHistory recommendationsHistory = RecommendationsHistory.from(jsonObject.getJSONObject("recommendations"));
            userCaringDetails.setRecommendations(recommendationsHistory);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userCaringDetails;
    }

    public UserCaringDetails() {
    }

    public UserCaringDetails(CaringEnvironmentHistory caringEnvironment, DiseaseCategoryHistory diseaseCategory, TreatmentHistory treatment, DemeanorHistory demeanor, RecommendationsHistory recommendations) {
        this.caringEnvironment = caringEnvironment;
        this.diseaseCategory = diseaseCategory;
        this.treatment = treatment;
        this.demeanor = demeanor;
        this.recommendations = recommendations;
    }

    public CaringEnvironmentHistory getCaringEnvironment() {
        return caringEnvironment;
    }

    public void setCaringEnvironment(CaringEnvironmentHistory caringEnvironment) {
        this.caringEnvironment = caringEnvironment;
    }

    public DiseaseCategoryHistory getDiseaseCategory() {
        return diseaseCategory;
    }

    public void setDiseaseCategory(DiseaseCategoryHistory diseaseCategory) {
        this.diseaseCategory = diseaseCategory;
    }

    public TreatmentHistory getTreatment() {
        return treatment;
    }

    public void setTreatment(TreatmentHistory treatment) {
        this.treatment = treatment;
    }

    public DemeanorHistory getDemeanor() {
        return demeanor;
    }

    public void setDemeanor(DemeanorHistory demeanor) {
        this.demeanor = demeanor;
    }

    public RecommendationsHistory getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(RecommendationsHistory recommendations) {
        this.recommendations = recommendations;
    }
}
