package com.karique.work.dev.healthtrack.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.models.NewPasword;
import com.karique.work.dev.healthtrack.network.HealthTrackApi;
import com.karique.work.dev.healthtrack.session.SessionManager;
import com.karique.work.dev.healthtrack.util.FuncionesUtiles;
import com.libizo.CustomEditText;

import org.json.JSONObject;

public class ResetPasswordInAppActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 148;

    private static int MODO_VALIDACION = 1;
    private static int MODO_CAMBIO = 2;
    int modo = MODO_VALIDACION;

    TextView toolbarTitleTextView;
    ProgressBar progressBar;
    AppCompatImageButton backAppCompatImageButton;

    TextView titleTextView;
    CustomEditText passwordEditText;
    CardView cambiarPasswordCardView;
    TextView cambiarPasswordTextView;
    ProgressBar cambiarPasswordProgressBar;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_in_app);
        hide();

        inicializarControles();
        inicializarEventos();
        inicializarDatos();
    }

    private void inicializarControles() {
        sessionManager = SessionManager.getInstance(this);
        toolbarTitleTextView = findViewById(R.id.toolbarTitleTextView);
        progressBar = findViewById(R.id.progressBar);
        backAppCompatImageButton = findViewById(R.id.backAppCompatImageButton);

        titleTextView = findViewById(R.id.titleTextView);
        passwordEditText = findViewById(R.id.passwordEditText);
        cambiarPasswordCardView = findViewById(R.id.cambiarPasswordCardView);
        cambiarPasswordTextView = findViewById(R.id.cambiarPasswordTextView);
        cambiarPasswordProgressBar = findViewById(R.id.cambiarPasswordProgressBar);
    }
    private void inicializarEventos() {
        backAppCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cambiarPasswordCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                procesarSolicitud();
            }
        });
    }
    private void inicializarDatos() {

    }
    private void procesarSolicitud() {
        if (modo == MODO_VALIDACION){
            if (passwordEditText.getText().toString().equals(sessionManager.getpassword())){
                titleTextView.setText("Ingrese su nueva contraseña");
                passwordEditText.setText("");
                cambiarPasswordTextView.setText("Cambiar contraseña");
                modo = MODO_CAMBIO;
            }
            else {
                Toast.makeText(this, "La contraseña ingresada no es correcta", Toast.LENGTH_LONG).show();
            }
        }
        else if (modo == MODO_CAMBIO){
            cambiarPassword();
        }
    }
    private void cambiarPassword() {
        if (passwordEditText.getText().toString().length() <= 7 || !FuncionesUtiles.checkPassword(passwordEditText.getText().toString())) {
            Toast.makeText(this, "Ingrese una contraseña valida (Debería tener 8 caracteres como mínimo, una mayuscula, una minuscula y un numero)", Toast.LENGTH_LONG).show();
            return;
        }

        NewPasword newPasword = new NewPasword();
        newPasword.setUserId(sessionManager.getiduser());
        newPasword.setNewPassword(passwordEditText.getText().toString());

        disableRegisterButton();
        AndroidNetworking.put(HealthTrackApi.RESET_PASSWORD_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(newPasword.toJsonObject())
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        sessionManager.setpassword(passwordEditText.getText().toString());
                        Toast.makeText(ResetPasswordInAppActivity.this, "Contraseña cambiada correctamente", Toast.LENGTH_LONG).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ResetPasswordInAppActivity.this, "Error al hacer el cambio", Toast.LENGTH_LONG).show();
                        enableRegisterButton();
                    }
                });
    }
    private void disableRegisterButton(){
        cambiarPasswordCardView.setEnabled(false);
        cambiarPasswordTextView.setVisibility(View.INVISIBLE);
        cambiarPasswordProgressBar.setVisibility(View.VISIBLE);
    }
    private void enableRegisterButton(){
        cambiarPasswordCardView.setEnabled(true);
        cambiarPasswordTextView.setVisibility(View.VISIBLE);
        cambiarPasswordProgressBar.setVisibility(View.GONE);
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_top,R.anim.slide_in_top);
    }
}