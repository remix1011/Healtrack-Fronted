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
import com.karique.work.dev.healthtrack.adapters.DemeanorHistoryAdapter;
import com.karique.work.dev.healthtrack.models.DemeanorHistory;
import com.karique.work.dev.healthtrack.models.Patient;
import com.karique.work.dev.healthtrack.models.User;
import com.karique.work.dev.healthtrack.network.HealthTrackApi;
import com.karique.work.dev.healthtrack.session.SessionManager;
import com.karique.work.dev.healthtrack.util.FuncionesFecha;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DemeanorActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 151;

    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;
    private AppCompatButton reintentarAppCompatButton;

    private RecyclerView demeanorRecyclerView;
    private DemeanorHistoryAdapter demeanorHistoryAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<DemeanorHistory> demeanorHistories;

    private AppCompatImageButton backAppCompatImageButton;
    private ProgressBar progressBar;

    private SessionManager sessionManager;

    private Patient patient;

    private TextView demeanorTitleTextView;
    private TextView frecuencyTextView;
    private TextView intensityTextView;
    private TextView recordDateTextView;

    private MaterialSpinner demeanorMaterialSpinner;
    private MaterialSpinner frecuencyMaterialSpinner;
    private MaterialSpinner intensityMaterialSpinner;
    private CardView agregarCardView;
    private ProgressBar agregarProgressBar;
    private TextView agregarTextView;
    private ConstraintLayout inputConstraintLayout;
    private boolean nuevoAgregado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demeanor);
        hide();

        inicializarControles();
        inicializarEventos();
        inicializarDatos();
        getData();
    }

    private void inicializarControles() {
        backAppCompatImageButton = findViewById(R.id.backAppCompatImageButton);
        progressBar = findViewById(R.id.progressBar);

        demeanorRecyclerView = findViewById(R.id.demeanorRecyclerView);
        messageConstraintLayout = findViewById(R.id.messageConstraintLayout);
        messageTextView = findViewById(R.id.messageTextView);
        reintentarAppCompatButton = findViewById(R.id.reintentarAppCompatButton);

        demeanorTitleTextView = findViewById(R.id.demeanorTitleTextView);
        frecuencyTextView = findViewById(R.id.frecuencyTextView);
        intensityTextView = findViewById(R.id.intensityTextView);
        recordDateTextView = findViewById(R.id.recordDateTextView);

        demeanorMaterialSpinner = findViewById(R.id.demeanorMaterialSpinner);
        frecuencyMaterialSpinner = findViewById(R.id.frecuencyMaterialSpinner);
        intensityMaterialSpinner = findViewById(R.id.intensityMaterialSpinner);
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

        demeanorHistories = new ArrayList<>();
        demeanorHistoryAdapter = new DemeanorHistoryAdapter(demeanorHistories, new DemeanorHistoryAdapter.ContextProvider() {
            @Override
            public Context getContext() {
                return DemeanorActivity.this;
            }
        });
        demeanorHistoryAdapter.setOnDemeanorHistoryClicked(new DemeanorHistoryAdapter.OnDemeanorHistoryClickedListener() {
            @Override
            public void OnDemeanorHistoryClicked(DemeanorHistory news) {

            }
        });
        layoutManager = new LinearLayoutManager(this);
        demeanorRecyclerView.setAdapter(demeanorHistoryAdapter);
        demeanorRecyclerView.setLayoutManager(layoutManager);

        demeanorMaterialSpinner.setItems(
                "Agresivas",
                "Pasivas",
                "Asertivas"
        );
        frecuencyMaterialSpinner.setItems(
                "Baja",
                "Moderada",
                "Alta"
        );
        intensityMaterialSpinner.setItems(
                "Escala (1:1)",
                "Escala (1:2)",
                "Escala (1:3)",
                "Escala (1:4)",
                "Escala (1:5)",
                "Escala (1:6)",
                "Escala (1:7)",
                "Escala (1:8)",
                "Escala (1:9)",
                "Escala (1:10)"
        );

        if (patient == null)
            inputConstraintLayout.setVisibility(View.GONE);
    }

    private void getData(){
        progressBar.setVisibility(View.VISIBLE);

        int userId = patient == null ? sessionManager.getiduser() : patient.getId();
        String url = HealthTrackApi.DEMEANOR_BY_USER(userId);
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);
                        demeanorRecyclerView.setVisibility(View.VISIBLE);
                        messageConstraintLayout.setVisibility(View.GONE);

                        demeanorHistories.clear();
                        demeanorHistories.addAll(DemeanorHistory.from(response));

                        if (demeanorHistories.isEmpty()){
                            setActualNoData();
                            showNoDataMessage("Sin datos");
                        }
                        else {
                            setActual(demeanorHistories.get(0));
                            demeanorHistories.remove(0);
                        }

                        demeanorHistoryAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        demeanorRecyclerView.setVisibility(View.GONE);
                        showNoDataMessage("Error de carga: " + anError.getErrorDetail());
                    }
                });
    }
    private void setActual(DemeanorHistory demeanorHistory) {
        demeanorTitleTextView.setText(demeanorHistory.getDemeanor());
        frecuencyTextView.setText(demeanorHistory.getFrecuency());
        intensityTextView.setText(demeanorHistory.getIntensity());
        recordDateTextView.setText(FuncionesFecha.formatDateWithHourSlashGuion(demeanorHistory.getRecordDate()));
    }
    private void setActualNoData() {
        demeanorTitleTextView.setText(getString(R.string.no_data));
        frecuencyTextView.setText(getString(R.string.no_data));
        intensityTextView.setText(getString(R.string.no_data));
        recordDateTextView.setText(getString(R.string.no_data));
    }
    private void showNoDataMessage(String message){
        messageTextView.setText(message);
        messageConstraintLayout.setVisibility(View.VISIBLE);
    }
    private void add() {
        disableLoginButton();

        DemeanorHistory demeanorHistory = new DemeanorHistory();
        demeanorHistory.setDemeanor((String)demeanorMaterialSpinner.getItems().get(demeanorMaterialSpinner.getSelectedIndex()));
        demeanorHistory.setFrecuency((String)frecuencyMaterialSpinner.getItems().get(frecuencyMaterialSpinner.getSelectedIndex()));
        demeanorHistory.setIntensity((String)intensityMaterialSpinner.getItems().get(intensityMaterialSpinner.getSelectedIndex()));
        demeanorHistory.setUserId(patient.getId());

        AndroidNetworking.post(HealthTrackApi.DEMEANOR_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(demeanorHistory.toJsonObject())
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