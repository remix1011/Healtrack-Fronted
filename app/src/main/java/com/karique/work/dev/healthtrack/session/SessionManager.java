package com.karique.work.dev.healthtrack.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.karique.work.dev.healthtrack.models.User;

public class SessionManager {
    private static final String PREFERENCE_NAME = "com.karique.work.dev.healthtrack";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    public static int NO_EXISTE_VALOR = -1;

    private static final String USER_LOGIN = "userLogin";

    private static final String ID = "id";
    private static final String EMAIL = "email";//
    private static final String USERNAME = "username";//
    private static final String PASSWORD = "password";//
    private static final String FULLNAME = "fullname";//
    private static final String DNI = "dni";//
    private static final String BIRTHDAY = "birthday";//
    private static final String USERTYPEID = "usertypeid";//
    private static final String ACTIVE = "active";
    private static final String FROM_NOTIFICATION = "fromNotification";

    private static SessionManager sessionManager;
    public static SessionManager getInstance(Context context){
        if (sessionManager == null){
            sessionManager = new SessionManager(context);
        }
        return sessionManager;
    }

    private SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    private void setUserIsLogged(boolean userLogin) { editor.putBoolean(USER_LOGIN, userLogin).commit(); }
    public boolean getUserIsLogged() {
        return preferences.getBoolean(USER_LOGIN, false);
    }

    private void setiduser(int id) { editor.putInt(ID, id).commit(); }
    public int getiduser() { return preferences.getInt(ID, NO_EXISTE_VALOR); }

    private void setemail(String email) { editor.putString(EMAIL, email).commit(); }
    public String getemail() { return preferences.getString(EMAIL, ""); }

    private void setusername(String username) { editor.putString(USERNAME, username).commit(); }
    public String getusername() { return preferences.getString(USERNAME, ""); }

    public void setpassword(String password) { editor.putString(PASSWORD, password).commit(); }
    public String getpassword() { return preferences.getString(PASSWORD, ""); }

    public void setfullname(String fullname) { editor.putString(FULLNAME, fullname).commit(); }
    public String getfullname() { return preferences.getString(FULLNAME, ""); }

    public void setdni(String dni) { editor.putString(DNI, dni).commit(); }
    public String getdni() { return preferences.getString(DNI, ""); }

    public void setbirthday(String birthday) { editor.putString(BIRTHDAY, birthday).commit(); }
    public String getbirthday() { return preferences.getString(BIRTHDAY, ""); }

    public void setusertypeid(int usertypeid) { editor.putInt(USERTYPEID, usertypeid).commit(); }
    public int getusertypeid() { return preferences.getInt(USERTYPEID, NO_EXISTE_VALOR); }

    private void setactive(boolean active) { editor.putBoolean(ACTIVE, active).commit(); }
    public boolean getactive() { return preferences.getBoolean(ACTIVE, false); }

    public void setfromnotification(boolean fromnotification) { editor.putBoolean(FROM_NOTIFICATION, fromnotification).commit(); }
    public boolean getfromnotification() { return preferences.getBoolean(FROM_NOTIFICATION, false); }

    public User getUser() {
        User user = new User();
        user.setId(getiduser());
        user.setEmail(getemail());
        user.setUsername(getusername());
        user.setPassword(getpassword());
        user.setFullName(getfullname());
        user.setDni(getdni());
        user.setBirthday(getbirthday());
        user.setUserTypeId(getusertypeid());
        user.setActive(getactive());

        return user;
    }

    public void saveUser(User userLogin) {
        setUserIsLogged(true);
        setiduser(userLogin.getId());
        setemail(userLogin.getEmail());
        setusername(userLogin.getUsername());
        setpassword(userLogin.getPassword());
        setfullname(userLogin.getFullName());
        setdni(userLogin.getDni());
        setbirthday(userLogin.getBirthday());
        setusertypeid(userLogin.getUserTypeId());
        setactive(userLogin.isActive());
    }

    public void deleteUser() {
        setUserIsLogged(false);
        setiduser(NO_EXISTE_VALOR);
        setemail("");
        setusername("");
        setpassword("");
        setfullname("");
        setdni("");
        setbirthday("");
        setusertypeid(NO_EXISTE_VALOR);
        setactive(false);
    }
}
