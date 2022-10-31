package com.karique.work.dev.healthtrack.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.adapters.CaringEnvironmentHistoryAdapter;
import com.karique.work.dev.healthtrack.models.CaringEnvironmentHistory;
import com.karique.work.dev.healthtrack.models.Patient;
import com.karique.work.dev.healthtrack.network.HealthTrackApi;
import com.karique.work.dev.healthtrack.session.SessionManager;
import com.karique.work.dev.healthtrack.util.FuncionesFecha;
import com.libizo.CustomEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CaringEnvironmentActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 152;

    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;
    private AppCompatButton reintentarAppCompatButton;

    private RecyclerView caringEnvironmentRecyclerView;
    private CaringEnvironmentHistoryAdapter caringEnvironmentHistoryAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<CaringEnvironmentHistory> caringEnvironmentHistories;

    private AppCompatImageButton backAppCompatImageButton;
    private ProgressBar progressBar;

    private SessionManager sessionManager;

    private Patient patient;

    private TextView clinicNameTextView;
    private TextView enviromentNameTextView;
    private TextView recordDateTextView;

    private CustomEditText nombreClinicaCustomEditText;
    private CustomEditText numeroAmbienteCustomEditText;
    private CustomEditText nombrePacienteCustomEditText;

    private CardView agregarCardView;
    private ProgressBar agregarProgressBar;
    private TextView agregarTextView;
    private ConstraintLayout inputConstraintLayout;
    private boolean nuevoAgregado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caring_environment);
        hide();

        inicializarControles();
        inicializarEventos();
        inicializarDatos();
        getData();
    }

    private void inicializarControles() {
        backAppCompatImageButton = findViewById(R.id.backAppCompatImageButton);
        progressBar = findViewById(R.id.progressBar);

        caringEnvironmentRecyclerView = findViewById(R.id.caringEnvironmentRecyclerView);
        messageConstraintLayout = findViewById(R.id.messageConstraintLayout);
        messageTextView = findViewById(R.id.messageTextView);
        reintentarAppCompatButton = findViewById(R.id.reintentarAppCompatButton);

        clinicNameTextView = findViewById(R.id.clinicNameTextView);
        enviromentNameTextView = findViewById(R.id.enviromentNameTextView);
        recordDateTextView = findViewById(R.id.recordDateTextView);

        nombreClinicaCustomEditText = findViewById(R.id.nombreClinicaCustomEditText);
        numeroAmbienteCustomEditText = findViewById(R.id.numeroAmbienteCustomEditText);
        nombrePacienteCustomEditText = findViewById(R.id.nombrePacienteCustomEditText);
        agregarCardView = findViewById(R.id.agregarCardView);
        agregarProgressBar = findViewById(R.id.agregarProgressBar);
        agregarTextView = findViewById(R.id.agregarTextView);
        inputConstraintLayout = findViewById(R.id.inputConstraintLayout);
    }
    private void inicializarEventos() {
        reintentarAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
        backAppCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        agregarCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
    }
    private void inicializarDatos() {
        sessionManager = SessionManager.getInstance(this);
        if (getIntent().getExtras() != null){
            patient = Patient.from(getIntent().getExtras());
            nombrePacienteCustomEditText.setText(patient.getFullName());
        }

        caringEnvironmentHistories = new ArrayList<>();
        caringEnvironmentHistoryAdapter = new CaringEnvironmentHistoryAdapter(caringEnvironmentHistories, new CaringEnvironmentHistoryAdapter.ContextProvider() {
            @Override
            public Context getContext() {
                return CaringEnvironmentActivity.this;
            }
        });
        caringEnvironmentHistoryAdapter.setOnCaringEnvironmentHistoryClicked(new CaringEnvironmentHistoryAdapter.OnCaringEnvironmentHistoryClickedListener() {
            @Override
            public void OnCaringEnvironmentHistoryClicked(CaringEnvironmentHistory news) {

            }
        });
        layoutManager = new LinearLayoutManager(this);
        caringEnvironmentRecyclerView.setAdapter(caringEnvironmentHistoryAdapter);
        caringEnvironmentRecyclerView.setLayoutManager(layoutManager);

        if (patient == null)
            inputConstraintLayout.setVisibility(View.GONE);
    }

    private void getData(){
        progressBar.setVisibility(View.VISIBLE);

        int userId = patient == null ? sessionManager.getiduser() : patient.getId();
        String url = HealthTrackApi.CARING_HISTORY_BY_USER(userId);
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);
                        caringEnvironmentRecyclerView.setVisibility(View.VISIBLE);
                        messageConstraintLayout.setVisibility(View.GONE);

                        caringEnvironmentHistories.clear();
                        caringEnvironmentHistories.addAll(CaringEnvironmentHistory.from(response));

                        if (caringEnvironmentHistories.isEmpty()){
                            setActualNoData();
                            showNoDataMessage("Sin datos");
                        }
                        else {
                            setActual(caringEnvironmentHistories.get(0));
                            caringEnvironmentHistories.remove(0);
                        }

                        caringEnvironmentHistoryAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        caringEnvironmentRecyclerView.setVisibility(View.GONE);
                        showNoDataMessage("Error de carga: " + anError.getErrorDetail());
                    }
                });
    }
    private void setActual(CaringEnvironmentHistory demeanorHistory) {
        clinicNameTextView.setText(demeanorHistory.getClinicName());
        enviromentNameTextView.setText(demeanorHistory.getEnviromentName());
        recordDateTextView.setText(FuncionesFecha.formatDateWithHourSlashGuion(demeanorHistory.getRecordDate()));
    }
    private void setActualNoData() {
        clinicNameTextView.setText(getString(R.string.no_data));
        enviromentNameTextView.setText(getString(R.string.no_data));
        recordDateTextView.setText(getString(R.string.no_data));
    }
    private void showNoDataMessage(String message){
        messageTextView.setText(message);
        messageConstraintLayout.setVisibility(View.VISIBLE);
    }
    private void add() {
        if (nombreClinicaCustomEditText.getText().length() == 0
        || numeroAmbienteCustomEditText.getText().length() == 0) {
            mostrarToast("No deje campos vacíos");
            return;
        }

        disableLoginButton();

        CaringEnvironmentHistory demeanorHistory = new CaringEnvironmentHistory();
        demeanorHistory.setClinicName(nombreClinicaCustomEditText.getText().toString());
        demeanorHistory.setEnviromentName(numeroAmbienteCustomEditText.getText().toString());
        demeanorHistory.setUserId(patient.getId());

        AndroidNetworking.post(HealthTrackApi.CARING_ENVIRONMENT_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(demeanorHistory.toJsonObject())
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        nombreClinicaCustomEditText.setText("");
                        numeroAmbienteCustomEditText.setText("");

                        nuevoAgregado = true;
                        mostrarToast("Agregado correctamente");
                        enableLoginButton();
                        getData();
                    }

                    @Override
                    public void onError(ANError anError) {
                        mostrarDatosError(anError);
                        enableLoginButton();
                    }
                });
    }
    private void mostrarToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    private void mostrarDatosError(ANError anError) {
        if (anError.getErrorCode() == 404){
            mostrarToast("Error: datos incorrectos\n" + anError.getMessage());
        }
        else {
            mostrarToast("Error al realizar la petición\n" + anError.getMessage());
        }
    }
    private void enableLoginButton() {
        agregarCardView.setEnabled(true);
        agregarTextView.setVisibility(View.VISIBLE);
        agregarProgressBar.setVisibility(View.INVISIBLE);
    }
    private void disableLoginButton() {
        agregarCardView.setEnabled(false);
        agregarTextView.setVisibility(View.INVISIBLE);
        agregarProgressBar.setVisibility(View.VISIBLE);
    }
    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    public void finish() {
        if (nuevoAgregado)
            setResult(RESULT_OK);

        super.finish();
        overridePendingTransition(R.anim.slide_from_top,R.anim.slide_in_top);
    }
}