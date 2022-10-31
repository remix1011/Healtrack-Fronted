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
import com.karique.work.dev.healthtrack.adapters.DiseaseCategoryHistoryAdapter;
import com.karique.work.dev.healthtrack.models.DemeanorHistory;
import com.karique.work.dev.healthtrack.models.DiseaseCategoryHistory;
import com.karique.work.dev.healthtrack.models.DiseaseCategoryType;
import com.karique.work.dev.healthtrack.models.Patient;
import com.karique.work.dev.healthtrack.network.HealthTrackApi;
import com.karique.work.dev.healthtrack.session.SessionManager;
import com.karique.work.dev.healthtrack.util.FuncionesFecha;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DiseaseCategoryActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 153;

    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;
    private AppCompatButton reintentarAppCompatButton;

    private RecyclerView diseaseCategoryHistoryRecyclerView;
    private DiseaseCategoryHistoryAdapter diseaseCategoryHistoryAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<DiseaseCategoryHistory> diseaseCategoryHistories;

    private AppCompatImageButton backAppCompatImageButton;
    private ProgressBar progressBar;

    private SessionManager sessionManager;

    private Patient patient;

    private TextView diseaseCategoryTypeDescriptionTextView;
    private TextView recordDateTextView;
    private List<DiseaseCategoryType> diseaseCategoryTypes;

    private MaterialSpinner categoryMaterialSpinner;
    private CardView agregarCardView;
    private ProgressBar agregarProgressBar;
    private TextView agregarTextView;
    private ConstraintLayout inputConstraintLayout;
    private boolean nuevoAgregado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_category);
        hide();

        inicializarControles();
        inicializarEventos();
        inicializarDatos();
        getData();
    }

    private void inicializarControles() {
        backAppCompatImageButton = findViewById(R.id.backAppCompatImageButton);
        progressBar = findViewById(R.id.progressBar);

        diseaseCategoryHistoryRecyclerView = findViewById(R.id.diseaseCategoryHistoryRecyclerView);
        messageConstraintLayout = findViewById(R.id.messageConstraintLayout);
        messageTextView = findViewById(R.id.messageTextView);
        reintentarAppCompatButton = findViewById(R.id.reintentarAppCompatButton);

        diseaseCategoryTypeDescriptionTextView = findViewById(R.id.diseaseCategoryTypeDescriptionTextView);
        recordDateTextView = findViewById(R.id.recordDateTextView);

        categoryMaterialSpinner = findViewById(R.id.categoryMaterialSpinner);
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
        if (getIntent().getExtras() != null)
            patient = Patient.from(getIntent().getExtras());

        diseaseCategoryHistories = new ArrayList<>();
        diseaseCategoryHistoryAdapter = new DiseaseCategoryHistoryAdapter(diseaseCategoryHistories, new DiseaseCategoryHistoryAdapter.ContextProvider() {
            @Override
            public Context getContext() {
                return DiseaseCategoryActivity.this;
            }
        });
        diseaseCategoryHistoryAdapter.setOnDiseaseCategoryHistoryClicked(new DiseaseCategoryHistoryAdapter.OnDiseaseCategoryHistoryClickedListener() {
            @Override
            public void OnDiseaseCategoryHistoryClicked(DiseaseCategoryHistory diseaseCategoryHistory) {

            }
        });
        layoutManager = new LinearLayoutManager(this);
        diseaseCategoryHistoryRecyclerView.setAdapter(diseaseCategoryHistoryAdapter);
        diseaseCategoryHistoryRecyclerView.setLayoutManager(layoutManager);

        diseaseCategoryTypes = Arrays.asList(
                new DiseaseCategoryType(1, "Leve", true),
                new DiseaseCategoryType(2, "Moderado", true),
                new DiseaseCategoryType(3, "Grave", true)
        );
        categoryMaterialSpinner.setItems(diseaseCategoryTypes);

        if (patient == null)
            inputConstraintLayout.setVisibility(View.GONE);
    }

    private void getData(){
        progressBar.setVisibility(View.VISIBLE);

        int userId = patient == null ? sessionManager.getiduser() : patient.getId();
        String url = HealthTrackApi.DISEASE_CATEGORY_HISTORY_BY_USER(userId);
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);
                        diseaseCategoryHistoryRecyclerView.setVisibility(View.VISIBLE);
                        messageConstraintLayout.setVisibility(View.GONE);

                        diseaseCategoryHistories.clear();
                        diseaseCategoryHistories.addAll(DiseaseCategoryHistory.from(response));

                        if (diseaseCategoryHistories.isEmpty()){
                            setActualNoData();
                            showNoDataMessage("Sin datos");
                        }
                        else {
                            setActual(diseaseCategoryHistories.get(0));
                            diseaseCategoryHistories.remove(0);
                        }

                        diseaseCategoryHistoryAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        diseaseCategoryHistoryRecyclerView.setVisibility(View.GONE);
                        showNoDataMessage("Error de carga: " + anError.getErrorDetail());
                    }
                });
    }
    private void setActual(DiseaseCategoryHistory diseaseCategoryHistory) {
        diseaseCategoryTypeDescriptionTextView.setText(diseaseCategoryHistory.getDiseaseCategoryType().getDescription());
        recordDateTextView.setText(FuncionesFecha.formatDateWithHourSlashGuion(diseaseCategoryHistory.getRecordDate()));
    }
    private void setActualNoData() {
        diseaseCategoryTypeDescriptionTextView.setText(getString(R.string.no_data));
        recordDateTextView.setText(getString(R.string.no_data));
    }
    private void showNoDataMessage(String message){
        messageTextView.setText(message);
        messageConstraintLayout.setVisibility(View.VISIBLE);
    }
    private void add() {
        disableLoginButton();

        DiseaseCategoryHistory diseaseCategoryHistory = new DiseaseCategoryHistory();
        diseaseCategoryHistory.setDiseaseCategoryTypeId(diseaseCategoryTypes.get(categoryMaterialSpinner.getSelectedIndex()).getId());
        diseaseCategoryHistory.setUserId(patient.getId());

        AndroidNetworking.post(HealthTrackApi.DISEASE_CATEGORY_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(diseaseCategoryHistory.toJsonObject())
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
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