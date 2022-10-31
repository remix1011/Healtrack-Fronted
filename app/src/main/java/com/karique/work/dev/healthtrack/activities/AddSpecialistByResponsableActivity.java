package com.karique.work.dev.healthtrack.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.adapters.AvailableUsersAdapter;
import com.karique.work.dev.healthtrack.adapters.MyPatientsAdapter;
import com.karique.work.dev.healthtrack.models.User;
import com.karique.work.dev.healthtrack.network.HealthTrackApi;
import com.karique.work.dev.healthtrack.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddSpecialistByResponsableActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 147;

    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;
    private AppCompatButton reintentarAppCompatButton;

    private RecyclerView myPatientsRecyclerView;
    private AvailableUsersAdapter availableUsersAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<User> patients;

    private AppCompatImageButton backAppCompatImageButton;
    private ProgressBar progressBar;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_specialist_by_responsable);
        hide();

        inicializarControles();
        inicializarEventos();
        inicializarDatos();
        getPatientsAvailables();
    }

    private void inicializarControles() {
        backAppCompatImageButton = findViewById(R.id.backAppCompatImageButton);
        progressBar = findViewById(R.id.progressBar);

        myPatientsRecyclerView = findViewById(R.id.myPatientsRecyclerView);
        messageConstraintLayout = findViewById(R.id.messageConstraintLayout);
        messageTextView = findViewById(R.id.messageTextView);
        reintentarAppCompatButton = findViewById(R.id.reintentarAppCompatButton);
    }
    private void inicializarEventos() {
        backAppCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        reintentarAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPatientsAvailables();
            }
        });
    }
    private void inicializarDatos() {
        sessionManager = SessionManager.getInstance(this);
        patients = new ArrayList<>();
        availableUsersAdapter = new AvailableUsersAdapter(patients, new AvailableUsersAdapter.ContextProvider() {
            @Override
            public Context getContext() {
                return AddSpecialistByResponsableActivity.this;
            }
        });
        availableUsersAdapter.setOnUserClicked(new AvailableUsersAdapter.OnUserClickedListener() {
            @Override
            public void OnUserClicked(User patient) {
                showSelectPatient(patient);
            }
        });
        layoutManager = new LinearLayoutManager(this);
        myPatientsRecyclerView.setAdapter(availableUsersAdapter);
        myPatientsRecyclerView.setLayoutManager(layoutManager);
    }

    private void showSelectPatient(User patient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Seguro que desea agregar este paciente como suyo?")
                .setCancelable(true)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        addSpecialistByResponsable(patient);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void addSpecialistByResponsable(User patient) {
        JSONObject loginJsonObject = new JSONObject();

        try {
            loginJsonObject.put("idUserSpecialist", sessionManager.getiduser());
            loginJsonObject.put("idUserResponsable", patient.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(HealthTrackApi.USERS_ADD_PATIENT)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(loginJsonObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mostrarToast("Agregado correctamente");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(ANError anError) {
                        handleError(anError);
                    }
                });
    }
    private void mostrarToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    private void handleError(ANError anError) {
        if (anError.getErrorCode() == 404){
            Toast.makeText(this, "Error: datos incorrectos" + anError.getMessage(), Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Error al realizar la petición" + anError.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void getPatientsAvailables(){
        progressBar.setVisibility(View.VISIBLE);

        AndroidNetworking.get(HealthTrackApi.RESPONSIBLES_AVAILABLES_BY_SPECIALIST(sessionManager.getiduser()))
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);
                        messageConstraintLayout.setVisibility(View.GONE);

                        patients.clear();
                        patients.addAll(User.from(response));
                        Collections.reverse(patients);
                        availableUsersAdapter.notifyDataSetChanged();

                        if (patients.isEmpty()){
                            showNoDataMessage("Sin datos");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        showNoDataMessage("Error de carga: " + anError.getErrorDetail());
                    }
                });
    }
    private void showNoDataMessage(String message){
        messageTextView.setText(message);
        messageConstraintLayout.setVisibility(View.VISIBLE);
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