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
import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.adapters.RecommendationsHistoryAdapter;
import com.karique.work.dev.healthtrack.models.Patient;
import com.karique.work.dev.healthtrack.models.RecommendationsHistory;
import com.karique.work.dev.healthtrack.models.TreatmentHistory;
import com.karique.work.dev.healthtrack.network.HealthTrackApi;
import com.karique.work.dev.healthtrack.session.SessionManager;
import com.karique.work.dev.healthtrack.util.FuncionesFecha;
import com.libizo.CustomEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecommendationsActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 154;

    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;
    private AppCompatButton reintentarAppCompatButton;

    private RecyclerView recommendationsHistoryRecyclerView;
    private RecommendationsHistoryAdapter recommendationsHistoryAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<RecommendationsHistory> recommendationsHistories;

    private AppCompatImageButton backAppCompatImageButton;
    private ProgressBar progressBar;

    private SessionManager sessionManager;

    private Patient patient;

    private TextView recommendationDescriptionTextView;
    private TextView recordDateTextView;

    private CustomEditText recommendationsCustomEditText;
    private CustomEditText nombrePacienteCustomEditText;

    private CardView agregarCardView;
    private ProgressBar agregarProgressBar;
    private TextView agregarTextView;
    private ConstraintLayout inputConstraintLayout;
    private boolean nuevoAgregado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);
        hide();

        inicializarControles();
        inicializarEventos();
        inicializarDatos();
        getData();
    }

    private void inicializarControles() {
        backAppCompatImageButton = findViewById(R.id.backAppCompatImageButton);
        progressBar = findViewById(R.id.progressBar);

        recommendationsHistoryRecyclerView = findViewById(R.id.recommendationsHistoryRecyclerView);
        messageConstraintLayout = findViewById(R.id.messageConstraintLayout);
        messageTextView = findViewById(R.id.messageTextView);
        reintentarAppCompatButton = findViewById(R.id.reintentarAppCompatButton);

        recommendationDescriptionTextView = findViewById(R.id.recommendationDescriptionTextView);
        recordDateTextView = findViewById(R.id.recordDateTextView);

        recommendationsCustomEditText = findViewById(R.id.recommendationsCustomEditText);
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

        recommendationsHistories = new ArrayList<>();
        recommendationsHistoryAdapter = new RecommendationsHistoryAdapter(recommendationsHistories, new RecommendationsHistoryAdapter.ContextProvider() {
            @Override
            public Context getContext() {
                return RecommendationsActivity.this;
            }
        });
        recommendationsHistoryAdapter.setOnRecommendationsHistoryClicked(new RecommendationsHistoryAdapter.OnRecommendationsHistoryClickedListener() {
            @Override
            public void OnRecommendationsHistoryClicked(RecommendationsHistory recommendationsHistory) {

            }
        });
        layoutManager = new LinearLayoutManager(this);
        recommendationsHistoryRecyclerView.setAdapter(recommendationsHistoryAdapter);
        recommendationsHistoryRecyclerView.setLayoutManager(layoutManager);

        if (patient == null)
            inputConstraintLayout.setVisibility(View.GONE);
    }

    private void getData(){
        progressBar.setVisibility(View.VISIBLE);

        int userId = patient == null ? sessionManager.getiduser() : patient.getId();
        String url = HealthTrackApi.RECOMMENDATIONS_BY_USER(userId);
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);
                        recommendationsHistoryRecyclerView.setVisibility(View.VISIBLE);
                        messageConstraintLayout.setVisibility(View.GONE);

                        recommendationsHistories.clear();
                        recommendationsHistories.addAll(RecommendationsHistory.from(response));

                        if (recommendationsHistories.isEmpty()){
                            setActualNoData();
                            showNoDataMessage("Sin datos");
                        }
                        else {
                            setActual(recommendationsHistories.get(0));
                            recommendationsHistories.remove(0);
                        }

                        recommendationsHistoryAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        recommendationsHistoryRecyclerView.setVisibility(View.GONE);
                        showNoDataMessage("Error de carga: " + anError.getErrorDetail());
                    }
                });
    }
    private void setActual(RecommendationsHistory recommendationsHistory) {
        recommendationDescriptionTextView.setText(recommendationsHistory.getDescription());
        recordDateTextView.setText(FuncionesFecha.formatDateWithHourSlashGuion(recommendationsHistory.getRecordDate()));
    }
    private void setActualNoData() {
        recommendationDescriptionTextView.setText(getString(R.string.no_data));
        recordDateTextView.setText(getString(R.string.no_data));
    }
    private void showNoDataMessage(String message){
        messageTextView.setText(message);
        messageConstraintLayout.setVisibility(View.VISIBLE);
    }
    private void add() {
        if (recommendationsCustomEditText.getText().length() == 0) {
            mostrarToast("No deje campos vacíos");
            return;
        }

        disableLoginButton();

        RecommendationsHistory recommendationsHistory = new RecommendationsHistory();
        recommendationsHistory.setDescription(recommendationsCustomEditText.getText().toString());
        recommendationsHistory.setUserId(patient.getId());

        AndroidNetworking.post(HealthTrackApi.RECOMMENDATIONS_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(recommendationsHistory.toJsonObject())
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        recommendationsCustomEditText.setText("");

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