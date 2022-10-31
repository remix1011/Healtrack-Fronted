package com.karique.work.dev.healthtrack.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.models.User;
import com.karique.work.dev.healthtrack.network.HealthTrackApi;
import com.karique.work.dev.healthtrack.session.SessionManager;
import com.karique.work.dev.healthtrack.util.FuncionesUtiles;
import com.libizo.CustomEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 149;

    AppCompatImageButton backAppCompatImageButton;

    CustomEditText usernameEditText;
    CustomEditText fullnameCustomEditText;
    CustomEditText emailCustomEditText;
    CustomEditText dniCustomEditText;
    CustomEditText birthdayCustomEditText;
    CustomEditText passwordCustomEditText;
    CustomEditText repeatPasswordCustomEditText;
    AppCompatRadioButton userResponsableAppCompatRadioButton;

    CardView registrarCardView;
    ProgressBar registrarProgressBar;
    TextView registrarTextView;
    TextView errorTextView;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        hide();

        inicializarControles();
        inicializarEventos();
        inicializarDatos();
    }

    private void inicializarControles() {
        backAppCompatImageButton = findViewById(R.id.backAppCompatImageButton);

        usernameEditText = findViewById(R.id.usernameEditText);
        fullnameCustomEditText = findViewById(R.id.fullnameCustomEditText);
        emailCustomEditText = findViewById(R.id.emailCustomEditText);
        dniCustomEditText = findViewById(R.id.dniCustomEditText);
        birthdayCustomEditText = findViewById(R.id.birthdayCustomEditText);
        passwordCustomEditText = findViewById(R.id.passwordCustomEditText);
        repeatPasswordCustomEditText = findViewById(R.id.repeatPasswordCustomEditText);
        userResponsableAppCompatRadioButton = findViewById(R.id.userResponsableAppCompatRadioButton);

        registrarCardView = findViewById(R.id.registrarCardView);
        registrarProgressBar = findViewById(R.id.registrarProgressBar);
        registrarTextView = findViewById(R.id.registrarTextView);
        errorTextView = findViewById(R.id.errorTextView);
    }
    private void inicializarEventos() {
        backAppCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        registrarCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUser();
            }
        });
        birthdayCustomEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarFechaDeNacimiento();
            }
        });
    }
    private void inicializarDatos() {
        sessionManager = SessionManager.getInstance(this);
    }

    private void seleccionarFechaDeNacimiento(){
        DatePickerDialog dpdFecha;
        final Calendar _calendario = Calendar.getInstance();
        _calendario.setTime(_calendario.getTime());

        dpdFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                birthdayCustomEditText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        },
                _calendario.get(Calendar.YEAR),
                _calendario.get(Calendar.MONTH),
                _calendario.get(Calendar.DAY_OF_MONTH)
        );
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            dpdFecha.getDatePicker().getTouchables().get(1).performClick();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dpdFecha.getDatePicker().getTouchables().get(0).performClick();
        }
        Calendar calendarioMin = Calendar.getInstance();
        calendarioMin.add(Calendar.YEAR, -100);
        dpdFecha.getDatePicker().setMinDate(calendarioMin.getTimeInMillis());
        Calendar calendarioMax = Calendar.getInstance();
        calendarioMax.add(Calendar.YEAR, -17);
        dpdFecha.getDatePicker().setMaxDate(calendarioMax.getTimeInMillis());
        dpdFecha.setButton(DatePickerDialog.BUTTON_POSITIVE, "Listo", dpdFecha);
        dpdFecha.setTitle("Ingrese su fecha de nacimiento");
        dpdFecha.show();
    }
    private boolean validacionCampos(){
        if (fullnameCustomEditText.getText().length() == 0){
            colocarMensajeDeError("El campo nombre está vacío");
            return false;
        }
        if (dniCustomEditText.getText().length() == 0){
            colocarMensajeDeError("El campo dni está vacío");
            return false;
        }
        if (dniCustomEditText.getText().length() != 8){
            colocarMensajeDeError("El campo dni no tiene 8 caracteres");
            return false;
        }
        if (emailCustomEditText.getText().length() == 0){
            colocarMensajeDeError("El campo correo está vacío");
            return false;
        }
        if (!FuncionesUtiles.isValidEmail(emailCustomEditText.getText().toString())) {
            Toast.makeText(this, "Ingrese un correo valido", Toast.LENGTH_LONG).show();
            return false;
        }
        if (birthdayCustomEditText.getText().length() == 0){
            colocarMensajeDeError("El campo dirección está vacío");
            return false;
        }
        if (passwordCustomEditText.getText().length() == 0){
            colocarMensajeDeError("El campo contraseña está vacío");
            return false;
        }
        if (repeatPasswordCustomEditText.getText().length() == 0){
            colocarMensajeDeError("El campo repetir contraseña está vacío");
            return false;
        }
        if (passwordCustomEditText.getText().toString().length() <= 7 ||
                !FuncionesUtiles.checkPassword(passwordCustomEditText.getText().toString())) {
            colocarMensajeDeError("Ingrese una contraseña valida (Debería tener 8 caracteres como mínimo, una mayuscula, una minuscula y un numero)");
            return false;
        }
        if (!passwordCustomEditText.getText().toString().equals(repeatPasswordCustomEditText.getText().toString())) {
            colocarMensajeDeError("Las contraseñas ingresadas no coinciden");
            return false;
        }

        return true;
    }
    private void colocarMensajeDeError(String mensaje) {
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(mensaje);
    }
    private void registrarUser() {
        if (!validacionCampos()) return;
        errorTextView.setVisibility(View.GONE);

        User user = new User();
        user.setEmail(emailCustomEditText.getText().toString().trim());
        user.setUsername(usernameEditText.getText().toString().trim());
        user.setPassword(passwordCustomEditText.getText().toString());
        user.setFullName(fullnameCustomEditText.getText().toString());
        user.setDni(dniCustomEditText.getText().toString().trim());
        user.setBirthday(birthdayCustomEditText.getText().toString().trim());
        user.setUserTypeId(userResponsableAppCompatRadioButton.isChecked() ? 2 : 1);
        user.setActive(true);

        disableButton();
        AndroidNetworking.post(HealthTrackApi.ADD_USERS_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(user.toJsonObject())
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(RegisterActivity.this, "Registro correcto", Toast.LENGTH_LONG).show();
                        login();
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == HealthTrackApi.ERROR_NETWORK_CONFLICT){
                            Toast.makeText(RegisterActivity.this, "Error: el nombre de usuario, correo o dni ya ha sido usado antes", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Error al registrar", Toast.LENGTH_LONG).show();
                        }

                        enableButton();
                    }
                });
    }
    private void login(){
        disableButton();
        JSONObject loginJsonObject = new JSONObject();

        try {
            loginJsonObject.put("email", emailCustomEditText.getText().toString());
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

                        if (userLogin.isResponsible())
                            mostrarResponsibleMainActivity(); //<- quitar cuando se implemente updateToken
                        else
                            mostrarSpecialistMainActivity();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(RegisterActivity.this, "Error de ingreso", Toast.LENGTH_LONG).show();
                        enableButton();
                    }
                });
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
    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void enableButton() {
        registrarCardView.setEnabled(true);
        registrarTextView.setVisibility(View.VISIBLE);
        registrarProgressBar.setVisibility(View.INVISIBLE);
    }
    private void disableButton() {
        registrarCardView.setEnabled(false);
        registrarTextView.setVisibility(View.INVISIBLE);
        registrarProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}