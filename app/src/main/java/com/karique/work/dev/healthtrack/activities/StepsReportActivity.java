package com.karique.work.dev.healthtrack.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.models.StepsHistory;
import com.karique.work.dev.healthtrack.network.HealthTrackApi;
import com.karique.work.dev.healthtrack.session.SessionManager;
import com.karique.work.dev.healthtrack.util.FuncionesFecha;
import com.karique.work.dev.healthtrack.util.FuncionesUtiles;
import com.libizo.CustomEditText;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StepsReportActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 156;

    private AppCompatImageButton backAppCompatImageButton;
    private ProgressBar progressBar;

    private CardView searchFilterCardView;
    private CustomEditText startDateCustomEditText;
    private CustomEditText endDateCustomEditText;
    private int tipoFechaDesde = 1;
    private int tipoFechaHasta = 2;
    private Date fechaDesde = null;
    private Date fechaHasta = null;

    private SessionManager sessionManager;
    private int idUser;
    private List<StepsHistory> histories;
    private BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_report);
        hide();

        inicializarControles();
        inicializarDatos();
        inicializarEventos();
        getHistory();
    }

    private void inicializarControles() {
        backAppCompatImageButton = findViewById(R.id.backAppCompatImageButton);
        progressBar = findViewById(R.id.progressBar);

        searchFilterCardView = findViewById(R.id.searchFilterCardView);
        startDateCustomEditText = findViewById(R.id.startDateCustomEditText);
        endDateCustomEditText = findViewById(R.id.endDateCustomEditText);

        chart = findViewById(R.id.chart);
    }
    private void inicializarDatos() {
        sessionManager = SessionManager.getInstance(this);
        idUser = getIntent().getIntExtra("idUser", 0);
        histories = new ArrayList<>();

        //setea las fechas a hoy y hace 7 dias
        fechaHasta = FuncionesFecha.getCurrentDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaHasta);
        cal.add(Calendar.DATE, -7);
        fechaDesde = cal.getTime();
        startDateCustomEditText.setText(FuncionesFecha.formatDateForAPI(fechaDesde));
        endDateCustomEditText.setText(FuncionesFecha.formatDateForAPI(fechaHasta));
    }
    private void inicializarEventos() {
        backAppCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchFilterCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHistory();
            }
        });
        startDateCustomEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarFecha(tipoFechaDesde);
            }
        });
        endDateCustomEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarFecha(tipoFechaHasta);
            }
        });
    }

    private void seleccionarFecha(final int tipoFecha) {
        DatePickerDialog dpdFecha;
        final Calendar _calendario = Calendar.getInstance();
        _calendario.setTime(tipoFecha == tipoFechaDesde ? fechaDesde : fechaHasta);
        dpdFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                _calendario.set(year, monthOfYear, dayOfMonth);
                if (tipoFecha == tipoFechaDesde) {
                    fechaDesde = _calendario.getTime();
                    startDateCustomEditText.setText(FuncionesFecha.formatDateForAPI(fechaDesde));
                } else {
                    fechaHasta = _calendario.getTime();
                    endDateCustomEditText.setText(FuncionesFecha.formatDateForAPI(fechaHasta));
                }
            }
        },
                _calendario.get(Calendar.YEAR),
                _calendario.get(Calendar.MONTH),
                _calendario.get(Calendar.DAY_OF_MONTH)
        );
        dpdFecha.getDatePicker().setMaxDate(new Date().getTime());
        dpdFecha.setButton(DatePickerDialog.BUTTON_POSITIVE, "Listo", dpdFecha);
        dpdFecha.setTitle(tipoFecha == tipoFechaDesde ? "Seleccionar fecha desde" : "Seleccionar fecha hasta");
        dpdFecha.show();
    }
    private void getHistory() {
        progressBar.setVisibility(View.VISIBLE);

        String dayStart = startDateCustomEditText.getText().toString();
        String dayEnd = endDateCustomEditText.getText().toString();
        AndroidNetworking.get(HealthTrackApi.STEPS_HISTORY_BY_USER_IN_DATES(idUser, dayStart, dayEnd))
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);
                        histories.clear();
                        histories.addAll(StepsHistory.from(response));
                        if (histories.isEmpty())
                            histories.add(new StepsHistory(0, 1, "2022-09-20T09:30:45.163", 0, false));
                        cargarGrafico(histories);
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        mostrarMensaje("Error de carga: ", anError.getErrorDetail());
                    }
                });
    }
    private void mostrarMensaje(String title, String message) {
        FuncionesUtiles.mostrarMensaje(
                title,
                message,
                this
        );
    }
    private void cargarGrafico(List<StepsHistory> histories) {
        List<String> labels = getXAxisLabels(histories);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries = new ArrayList<>();
        for (int i = 0; i < histories.size(); i++) {
            StepsHistory history = histories.get(i);
            barEntries.add(new BarEntry(i, (float) history.getSteps()));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Pasos dados");
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        barDataSet.setColor(Color.parseColor("#F85F6A"));
        barDataSet.setValueTextSize(12);

        BarData barData = new BarData(barDataSet);
        barData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                return String.valueOf((int) barEntry.getY()) + " pasos";
            }
        });

        chart.getDescription().setTextSize(12);
        chart.getDescription().setText("");
        chart.setDrawMarkers(true);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.animateY(100);
        chart.getXAxis().setGranularityEnabled(true);
        chart.getXAxis().setGranularity(1.0f);
        chart.getXAxis().setLabelCount(barEntries.size());
        chart.getXAxis().setDrawLabels(true);
        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ((int) value) < labels.size() ? labels.get((int) value) : "";
            }
        });
        chart.setData(barData);

        if (histories.get(0).getId() == 0) chart.clear();
        else chart.setData(barData);

        XAxis xAxisDerecho = chart.getXAxis();
        xAxisDerecho.setLabelRotationAngle(-45);
        xAxisDerecho.setDrawGridLines(true);

        chart.setVisibleXRangeMaximum(5);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.notifyDataSetChanged();
        chart.invalidate();
    }
    private List<String> getXAxisLabels(List<StepsHistory> histories) {
        List<String> labels = new ArrayList<>();
        for (StepsHistory history : histories) {
            labels.add(history.getRecordDate().substring(0, 10));
        }
        return labels;
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