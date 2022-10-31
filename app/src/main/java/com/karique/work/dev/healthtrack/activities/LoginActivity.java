package com.karique.work.dev.healthtrack.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.models.User;
import com.karique.work.dev.healthtrack.network.HealthTrackApi;
import com.karique.work.dev.healthtrack.session.SessionManager;
import com.libizo.CustomEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    String TAG = "kariqueFCM";

    CustomEditText usernameCustomEditText;
    CustomEditText passwordCustomEditText;
    CardView loginCardView;
    TextView loginTextView;
    ProgressBar loginProgressBar;
    CardView loginGoogleCardView;
    TextView loginGoogleTextView;
    ProgressBar loginGoogleProgressBar;
    TextView registerTextView;
    TextView resetPasswordTextView;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        hide();

        inicializarControles();
        inicializarEventos();
        inicializarDatos();
    }

    private void inicializarControles() {
        usernameCustomEditText = findViewById(R.id.usernameCustomEditText);
        passwordCustomEditText = findViewById(R.id.passwordCustomEditText);
        loginCardView = findViewById(R.id.loginCardView);
        loginTextView = findViewById(R.id.loginTextView);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        loginGoogleCardView = findViewById(R.id.loginGoogleCardView);
        loginGoogleTextView = findViewById(R.id.loginGoogleTextView);
        loginGoogleProgressBar = findViewById(R.id.loginGoogleProgressBar);
        registerTextView = findViewById(R.id.registerTextView);
        resetPasswordTextView = findViewById(R.id.resetPasswordTextView);
    }
    private void inicializarEventos() {
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarRegister();
            }
        });
        loginCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        resetPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarResetPasswordActivity();
            }
        });
    }
    private void inicializarDatos() {
        sessionManager = SessionManager.getInstance(this);
        if (sessionManager.getUserIsLogged()){
            if (sessionManager.getUser().isResponsible())
                mostrarResponsibleMainActivity(); //<- quitar cuando se implemente updateToken
            else
                mostrarSpecialistMainActivity();
        }
    }

    private void login(){
        //validaciones
        if (usernameCustomEditText.getText().length() == 0){
            Toast.makeText(LoginActivity.this, "El campo de usuario está vacío", Toast.LENGTH_LONG).show();
            return;
        }
        if (passwordCustomEditText.getText().length() == 0){
            Toast.makeText(LoginActivity.this, "El campo contraseña está vacío", Toast.LENGTH_LONG).show();
            return;
        }
        //mostrarMainActivity();

        disableLoginButton();
        JSONObject loginJsonObject = new JSONObject();

        try {
            loginJsonObject.put("email", usernameCustomEditText.getText().toString().trim());
            loginJsonObject.put("password", passwordCustomEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(HealthTrackApi.LOGIN_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(loginJsonObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        User userLogin = User.from(response);
                        sessionManager.saveUser(userLogin);
                        //updateToken();

                        if (userLogin.isResponsible())
                            mostrarResponsibleMainActivity(); //<- quitar cuando se implemente updateToken
                        else
                            mostrarSpecialistMainActivity();
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 404){
                            Toast.makeText(LoginActivity.this, "Error: credenciales incorrectas", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Error al realizar la petición", Toast.LENGTH_LONG).show();
                        }
                        enableLoginButton();
                    }
                });
    }
    private void mostrarResetPasswordActivity(){
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivityForResult(intent, ResetPasswordActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    private void mostrarRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, RegisterActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    private void mostrarResponsibleMainActivity(){
        Intent intent = new Intent(this, ResponsibleMainActivity.class);
        startActivityForResult(intent, ResponsibleMainActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finishAffinity();
    }
    private void mostrarSpecialistMainActivity(){
        Intent intent = new Intent(this, SpecialistMainActivity.class);
        startActivityForResult(intent, SpecialistMainActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finishAffinity();
    }
    private void enableLoginButton() {
        loginCardView.setEnabled(true);
        loginTextView.setVisibility(View.VISIBLE);
        loginProgressBar.setVisibility(View.INVISIBLE);
    }
    private void disableLoginButton() {
        loginCardView.setEnabled(false);
        loginTextView.setVisibility(View.INVISIBLE);
        loginProgressBar.setVisibility(View.VISIBLE);
    }
    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}