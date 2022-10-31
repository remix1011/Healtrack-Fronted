package com.karique.work.dev.healthtrack.network;

public class HealthTrackApi {
    //private static String BASE_URL = "http://192.168.18.9:2467/";  //local http
    //public static String BASE_URL = "https://192.168.18.9:44361/";  //local https
    public static String BASE_URL = "https://healthtrack.azurewebsites.net/";  //azure

    public static int ERROR_NETWORK_CONFLICT = 409;

    public static String LOGIN_URL = BASE_URL + "api/login";
    public static String USERS_URL = BASE_URL + "api/users";
    public static String ADD_USERS_URL = BASE_URL + "api/users/addUser";
    public static String USERS_ADD_PATIENT = BASE_URL + "api/users/addSpecialistByResponsable";
    public static String USERS_DELETE_PATIENT = BASE_URL + "api/users/deleteSpecialistByResponsable";
    public static String RESET_PASSWORD_URL = BASE_URL + "/api/users/resetPassword";
    public static String SEND_RESET_PASSWORD_EMAIL_URL = BASE_URL + "/api/users/sendResetPasswordEmail";
    public static String TOKEN_FCM_URL = BASE_URL + "/api/users/tokenFCM";
    public static String CARING_ENVIRONMENT_URL = BASE_URL + "api/caringEnvironmentHistories";
    public static String DISEASE_CATEGORY_URL = BASE_URL + "api/diseaseCategoryHistories";
    public static String TREATMENT_URL = BASE_URL + "api/treatmentHistories";
    public static String DEMEANOR_URL = BASE_URL + "api/demeanorHistories";
    public static String RECOMMENDATIONS_URL = BASE_URL + "api/recommendationsHistories";
    public static String PLACES_HISTORY_URL = BASE_URL + "api/placesHistories";
    public static String OXIMETRIES_URL = BASE_URL + "api/oximetries";
    public static String ADD_DAILY_REPORT = BASE_URL + "api/users/addDailyReport";

    public static String PLACES_HISTORY_BY_USER_IN_DATES(int userId, String dayStart, String dayEnd){
        return USERS_URL + "/" + String.valueOf(userId) + "/placesHistory/start/" + dayStart + "/end/" + dayEnd;
    }
    public static String STEPS_HISTORY_BY_USER_IN_DATES(int userId, String dayStart, String dayEnd){
        return USERS_URL + "/" + String.valueOf(userId) + "/stepsHistory/start/" + dayStart + "/end/" + dayEnd;
    }
    public static String DISTANCE_HISTORY_BY_USER_IN_DATES(int userId, String dayStart, String dayEnd){
        return USERS_URL + "/" + String.valueOf(userId) + "/distanceHistory/start/" + dayStart + "/end/" + dayEnd;
    }
    public static String CALORIES_HISTORY_BY_USER_IN_DATES(int userId, String dayStart, String dayEnd){
        return USERS_URL + "/" + String.valueOf(userId) + "/caloriesHistory/start/" + dayStart + "/end/" + dayEnd;
    }
    public static String HEARTBEAT_HISTORY_BY_USER_IN_DATES(int userId, String dayStart, String dayEnd){
        return USERS_URL + "/" + String.valueOf(userId) + "/heartbeatHistory/start/" + dayStart + "/end/" + dayEnd;
    }
    public static String SLEEP_HISTORY_BY_USER_IN_DATES(int userId, String dayStart, String dayEnd){
        return USERS_URL + "/" + String.valueOf(userId) + "/sleepHistory/start/" + dayStart + "/end/" + dayEnd;
    }
    public static String WEIGHT_HISTORY_BY_USER_IN_DATES(int userId, String dayStart, String dayEnd){
        return USERS_URL + "/" + String.valueOf(userId) + "/weightHistory/start/" + dayStart + "/end/" + dayEnd;
    }
    public static String OXIMETRY_HISTORY_BY_USER_IN_DATES(int userId, String dayStart, String dayEnd){
        return USERS_URL + "/" + String.valueOf(userId) + "/oximetries/start/" + dayStart + "/end/" + dayEnd;
    }
    public static String OXIMETRY_TODAY(int userId){
        return USERS_URL + "/" + String.valueOf(userId) + "/oximetries/today";
    }


    public static String CARING_HISTORY_BY_USER(int userId){
        return USERS_URL + "/" + String.valueOf(userId) + "/" + "caringEnvironmentHistory";
    }

    public static String DEMEANOR_BY_USER(int userId){
        return USERS_URL + "/" + String.valueOf(userId) + "/" + "demeanorHistory";
    }

    public static String TREATMENTS_BY_USER(int userId){
        return USERS_URL + "/" + String.valueOf(userId) + "/" + "treatmentHistory";
    }

    public static String RECOMMENDATIONS_BY_USER(int userId){
        return USERS_URL + "/" + String.valueOf(userId) + "/" + "recommendationsHistory";
    }

    public static String DISEASE_CATEGORY_HISTORY_BY_USER(int userId){
        return USERS_URL + "/" + String.valueOf(userId) + "/" + "diseaseCategoryHistory";
    }

    public static String USER_CARING_DETAILS_BY_USER(int userId){
        return USERS_URL + "/" + String.valueOf(userId) + "/" + "UserCaringDetails";
    }

    public static String USER_METRIC_DETAILS_TODAY_BY_USER(int userId){
        return USERS_URL + "/" + String.valueOf(userId) + "/" + "UserMetricsDetailsToday";
    }

    public static String PATIENTS_BY_USER(int userId){
        return USERS_URL + "/" + String.valueOf(userId) + "/" + "patients";
    }

    public static String RESPONSIBLES_AVAILABLES_BY_SPECIALIST(int userId){
        return USERS_URL + "/" + String.valueOf(userId) + "/" + "responsablesAvailablesBySpecialist";
    }

    public static String USER_SPECIALIST_ASIGNED(int userId){
        return USERS_URL + "/" + String.valueOf(userId) + "/" + "SpecialistAsigned";
    }
}
