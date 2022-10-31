package com.karique.work.dev.healthtrack.fragments;

import static com.karique.work.dev.healthtrack.activities.ResponsibleMainActivity.GOOGLE_FIT_PERMISSIONS_REQUEST_CODE;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.SessionsClient;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.result.SessionReadResponse;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.models.Calorie;
import com.karique.work.dev.healthtrack.models.CaloriesHistory;
import com.karique.work.dev.healthtrack.models.DailyReport;
import com.karique.work.dev.healthtrack.models.Distance;
import com.karique.work.dev.healthtrack.models.DistanceHistory;
import com.karique.work.dev.healthtrack.models.HeartbeatHistory;
import com.karique.work.dev.healthtrack.models.HistorialFitnessDTO;
import com.karique.work.dev.healthtrack.models.Oximetry;
import com.karique.work.dev.healthtrack.models.Patient;
import com.karique.work.dev.healthtrack.models.SleepHistory;
import com.karique.work.dev.healthtrack.models.Step;
import com.karique.work.dev.healthtrack.models.StepsHistory;
import com.karique.work.dev.healthtrack.models.UserCaringDetails;
import com.karique.work.dev.healthtrack.models.WeightHistory;
import com.karique.work.dev.healthtrack.network.HealthTrackApi;
import com.karique.work.dev.healthtrack.session.SessionManager;
import com.karique.work.dev.healthtrack.util.FuncionesFecha;
import com.karique.work.dev.healthtrack.util.FuncionesUtiles;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ReportsFragment extends Fragment {
    public static final String TAG = "Sleep";
    public static final int FRAGMENT_TYPE_REPORTS = 1;
    private Patient patient;

    private CircularProgressBar stepsCircularProgressBar;
    private TextView stepsPercentageTextView;
    private TextView stepsDetailsTextView;
    private ConstraintLayout stepsConstraintLayout;

    private CircularProgressBar distanceCircularProgressBar;
    private TextView distancePercentageTextView;
    private TextView distanceDetailsTextView;
    private ConstraintLayout distanceConstraintLayout;

    private CircularProgressBar caloriesCircularProgressBar;
    private TextView caloriesPercentageTextView;
    private TextView caloriesDetailsTextView;
    private ConstraintLayout caloriesConstraintLayout;

    private CircularProgressBar heartBeatCircularProgressBar;
    private TextView heartBeatPercentageTextView;
    private TextView heartBeatDetailsTextView;
    private ConstraintLayout heartBeatConstraintLayout;

    private CircularProgressBar sleepCircularProgressBar;
    private TextView sleepPercentageTextView;
    private TextView sleepDetailsTextView;
    private ConstraintLayout sleepConstraintLayout;

    private CircularProgressBar weightCircularProgressBar;
    private TextView weightPercentageTextView;
    private TextView weightDetailsTextView;
    private ConstraintLayout weightConstraintLayout;

    private CircularProgressBar oximetryCircularProgressBar;
    private TextView oximetryPercentageTextView;
    private TextView oximetryDetailsTextView;
    private ConstraintLayout oximetryConstraintLayout;

    private SessionManager sessionManager;

    float percentageSteps = 100;
    float percentageDistance = 100;
    float percentageCalories = 100;
    float percentageHeartBeat = 100;
    float percentageSleep = 100;
    float percentageWeight = 100;
    float percentageOximetry = 100;

    private int maxSteps = 5000;
    private int maxDistance = 8000;
    private int maxCalories = 2800;
    private int maxHeartBeat = 190;
    private int maxSleep = 9;
    private int maxWeight = 85;
    private int maxOximetry = 100;

    private int totalSteps = 0;
    private int totalDistance = 0;
    private int totalCalories = 0;
    private int totalHeartBeat = 0;
    private float totalSleep = 0;
    private float totalWeight = 0;
    private float totalOximetry = 0;

    Date sessionStart = null;
    Date sessionEnd = null;
    int bpmAverage = 0;
    private FitnessOptions fitnessOptions = null;
    private GoogleSignInAccount account = null;

    public ReportsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reports, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarControles(view);
        inicializarDatos();
        inicializarEventos();
    }

    private void inicializarControles(View view) {
        stepsCircularProgressBar = view.findViewById(R.id.stepsCircularProgressBar);
        stepsPercentageTextView = view.findViewById(R.id.stepsPercentageTextView);
        stepsDetailsTextView = view.findViewById(R.id.stepsDetailsTextView);
        stepsConstraintLayout = view.findViewById(R.id.stepsConstraintLayout);
        stepsCircularProgressBar.setRoundBorder(true);

        distanceCircularProgressBar = view.findViewById(R.id.distanceCircularProgressBar);
        distancePercentageTextView = view.findViewById(R.id.distancePercentageTextView);
        distanceDetailsTextView = view.findViewById(R.id.distanceDetailsTextView);
        distanceConstraintLayout = view.findViewById(R.id.distanceConstraintLayout);
        distanceCircularProgressBar.setRoundBorder(true);

        caloriesCircularProgressBar = view.findViewById(R.id.caloriesCircularProgressBar);
        caloriesPercentageTextView = view.findViewById(R.id.caloriesPercentageTextView);
        caloriesDetailsTextView = view.findViewById(R.id.caloriesDetailsTextView);
        caloriesConstraintLayout = view.findViewById(R.id.caloriesConstraintLayout);
        caloriesCircularProgressBar.setRoundBorder(true);

        heartBeatCircularProgressBar = view.findViewById(R.id.heartBeatCircularProgressBar);
        heartBeatPercentageTextView = view.findViewById(R.id.heartBeatPercentageTextView);
        heartBeatDetailsTextView = view.findViewById(R.id.heartBeatDetailsTextView);
        heartBeatConstraintLayout = view.findViewById(R.id.heartBeatConstraintLayout);
        heartBeatCircularProgressBar.setRoundBorder(true);

        sleepCircularProgressBar = view.findViewById(R.id.sleepCircularProgressBar);
        sleepPercentageTextView = view.findViewById(R.id.sleepPercentageTextView);
        sleepDetailsTextView = view.findViewById(R.id.sleepDetailsTextView);
        sleepConstraintLayout = view.findViewById(R.id.sleepConstraintLayout);
        sleepCircularProgressBar.setRoundBorder(true);

        weightCircularProgressBar = view.findViewById(R.id.weightCircularProgressBar);
        weightPercentageTextView = view.findViewById(R.id.weightPercentageTextView);
        weightDetailsTextView = view.findViewById(R.id.weightDetailsTextView);
        weightConstraintLayout = view.findViewById(R.id.weightConstraintLayout);
        weightCircularProgressBar.setRoundBorder(true);

        oximetryCircularProgressBar = view.findViewById(R.id.oximetryCircularProgressBar);
        oximetryPercentageTextView = view.findViewById(R.id.oximetryPercentageTextView);
        oximetryDetailsTextView = view.findViewById(R.id.oximetryDetailsTextView);
        oximetryConstraintLayout = view.findViewById(R.id.oximetryConstraintLayout);
        oximetryCircularProgressBar.setRoundBorder(true);
    }
    private void inicializarDatos() {
        sessionManager = SessionManager.getInstance(getContext());
        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_SLEEP_SEGMENT, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_READ)
                .build();

        account = GoogleSignIn.getAccountForExtension(getActivity(), fitnessOptions);

        //si no tiene los permisos, los pide
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    getActivity(), // debe ser el contexto del activity padre
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    account,
                    fitnessOptions
            );
        }
        else getStepsGoogleFit();
    }
    private void inicializarEventos() {
        stepsConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onStepHistoryClickedListener != null)
                    onStepHistoryClickedListener.OnStepHistoryClicked();
            }
        });
        distanceConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDistanceHistoryClickedListener != null)
                    onDistanceHistoryClickedListener.OnDistanceHistoryClicked();
            }
        });
        caloriesConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCaloriesHistoryClickedListener != null)
                    onCaloriesHistoryClickedListener.OnCaloriesHistoryClicked();
            }
        });
        heartBeatConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onHeartbeatHistoryClickedListener != null)
                    onHeartbeatHistoryClickedListener.OnHeartbeatHistoryClicked();
            }
        });
        sleepConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSleepHistoryClickedListener != null)
                    onSleepHistoryClickedListener.OnSleepHistoryClicked();
            }
        });
        weightConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onWeightHistoryClickedListener != null)
                    onWeightHistoryClickedListener.OnWeightHistoryClicked();
            }
        });
        oximetryConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onOximetryClickedListener != null)
                    onOximetryClickedListener.OnOximetryClicked();
            }
        });
    }

    public void getStepsGoogleFit() {
        Fitness.getHistoryClient(getActivity(), GoogleSignIn.getAccountForExtension(getActivity(), fitnessOptions))
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        if (!dataSet.getDataPoints().isEmpty()) {
                            DataPoint dataPoint = dataSet.getDataPoints().get(0);

                            totalSteps = dataPoint.getValue(Field.FIELD_STEPS).asInt();
                            stepsDetailsTextView.setText(String.format("%s pasos", String.valueOf(totalSteps)));

                            if (totalSteps < maxSteps)
                                percentageSteps = (totalSteps*1.f / maxSteps) * 100;

                            stepsPercentageTextView.setText(String.valueOf(new DecimalFormat("#.##").format(percentageSteps) + "%"));
                            stepsCircularProgressBar.setProgress(percentageSteps);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mostrarToast("Primero acepte todos los permisos: " + e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DataSet>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSet> task) {
                        getDistanceGoogleFit();
                    }
                });
    }
    public void getDistanceGoogleFit() {
        Fitness.getHistoryClient(getActivity(), GoogleSignIn.getAccountForExtension(getActivity(), fitnessOptions))
                .readDailyTotal(DataType.TYPE_DISTANCE_DELTA)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        if (!dataSet.getDataPoints().isEmpty()) {
                            DataPoint dataPoint = dataSet.getDataPoints().get(0);

                            totalDistance = (int)dataPoint.getValue(Field.FIELD_DISTANCE).asFloat();
                            distanceDetailsTextView.setText(String.format("%s metros", String.valueOf(totalDistance)));

                            if (totalDistance < maxDistance)
                                percentageDistance = (totalDistance*1.f / maxDistance) * 100;

                            distancePercentageTextView.setText(String.valueOf(new DecimalFormat("#.##").format(percentageDistance) + "%"));
                            distanceCircularProgressBar.setProgress(percentageDistance);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mostrarToast("Primero acepte todos los permisos: " + e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DataSet>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSet> task) {
                        getCaloriesGoogleFit();
                    }
                });
    }
    public void getCaloriesGoogleFit() {
        Fitness.getHistoryClient(getActivity(), GoogleSignIn.getAccountForExtension(getActivity(), fitnessOptions))
                .readDailyTotal(DataType.TYPE_CALORIES_EXPENDED)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        if (!dataSet.getDataPoints().isEmpty()) {
                            DataPoint dataPoint = dataSet.getDataPoints().get(0);

                            totalCalories = (int)dataPoint.getValue(Field.FIELD_CALORIES).asFloat();
                            caloriesDetailsTextView.setText(String.format("%s calorías", String.valueOf(totalCalories)));

                            if (totalCalories < maxCalories)
                                percentageCalories = (totalCalories*1.f / maxCalories) * 100;

                            caloriesPercentageTextView.setText(String.valueOf(new DecimalFormat("#.##").format(percentageCalories) + "%"));
                            caloriesCircularProgressBar.setProgress(percentageCalories);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mostrarToast("Primero acepte todos los permisos: " + e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DataSet>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSet> task) {
                        getHeartBeatGoogleFit();
                    }
                });
    }
    public void getHeartBeatGoogleFit() {
        Fitness.getHistoryClient(getActivity(), GoogleSignIn.getAccountForExtension(getActivity(), fitnessOptions))
                .readDailyTotal(DataType.TYPE_HEART_RATE_BPM)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        if (!dataSet.getDataPoints().isEmpty()) {
                            DataPoint dataPoint = dataSet.getDataPoints().get(0);

                            totalHeartBeat = (int)dataPoint.getValue(Field.FIELD_AVERAGE).asFloat();
                            heartBeatDetailsTextView.setText(String.format("%s Bpm", String.valueOf(totalHeartBeat)));

                            if (totalHeartBeat < maxHeartBeat)
                                percentageHeartBeat = (totalHeartBeat*1.f / maxHeartBeat) * 100;

                            heartBeatPercentageTextView.setText(String.valueOf(new DecimalFormat("#.##").format(percentageHeartBeat) + "%"));
                            heartBeatCircularProgressBar.setProgress(percentageHeartBeat);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mostrarToast("Primero acepte todos los permisos: " + e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DataSet>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSet> task) {
                        getSleepGoogleFit();
                    }
                });
    }
    public void getSleepGoogleFit() {
        long periodStartMillis = getStartOfDay();
        long periodEndMillis = getEndOfDay();

        SessionsClient client = Fitness.getSessionsClient(getActivity(), GoogleSignIn.getAccountForExtension(getActivity(), fitnessOptions));

        SessionReadRequest sessionReadRequest = new SessionReadRequest.Builder()
                .read(DataType.TYPE_SLEEP_SEGMENT)
                .includeSleepSessions()
                .readSessionsFromAllApps()
                .setTimeInterval(periodStartMillis, periodEndMillis, TimeUnit.MILLISECONDS)
                .build();

        client.readSession(sessionReadRequest)
                .addOnSuccessListener(new OnSuccessListener<SessionReadResponse>() {
                    @Override
                    public void onSuccess(SessionReadResponse sessionReadResponse) {
                        List<Session> sessions = sessionReadResponse.getSessions();
                        if (!sessions.isEmpty()) {
                            sessionStart = new Date(sessions.get(sessions.size() - 1).getStartTime(TimeUnit.MILLISECONDS));
                            sessionEnd = new Date(sessions.get(sessions.size() - 1).getEndTime(TimeUnit.MILLISECONDS));

                            long milliseconds = sessionEnd.getTime() - sessionStart.getTime();
                            totalSleep = TimeUnit.MILLISECONDS.toHours(milliseconds);
                            int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                            int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
                            sleepDetailsTextView.setText(String.format("%s Horas %s min", String.valueOf(hours), String.valueOf(minutes)));

                            if (totalSleep < maxSleep)
                                percentageSleep = (totalSleep*1.f / maxSleep) * 100;

                            sleepPercentageTextView.setText(String.valueOf(new DecimalFormat("#.##").format(percentageSleep) + "%"));
                            sleepCircularProgressBar.setProgress(percentageSleep);
                            getHeartBeatInSleepGoogleFit(sessionStart, sessionEnd);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mostrarToast("Es necesario que brinde los permisos de actividad física: " + e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<SessionReadResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<SessionReadResponse> task) {
                        getWeightGoogleFit();
                    }
                });
    }
    public void getHeartBeatInSleepGoogleFit(Date sessionStart, Date sessionEnd) {
        SessionsClient client = Fitness.getSessionsClient(getActivity(), GoogleSignIn.getAccountForExtension(getActivity(), fitnessOptions));

        SessionReadRequest sessionReadRequest = new SessionReadRequest.Builder()
                .read(DataType.TYPE_HEART_RATE_BPM)
                .includeSleepSessions()
                .readSessionsFromAllApps()
                .setTimeInterval(sessionStart.getTime(), sessionEnd.getTime(), TimeUnit.MILLISECONDS)
                .build();

        client.readSession(sessionReadRequest)
                .addOnSuccessListener(new OnSuccessListener<SessionReadResponse>() {
                    @Override
                    public void onSuccess(SessionReadResponse sessionReadResponse) {
                        float bpmSum = 0;
                        float count = 0;
                        for (Session session : sessionReadResponse.getSessions()) {
                            for (DataSet dataSet : sessionReadResponse.getDataSet(session)) {
                                for (DataPoint dataPoint : dataSet.getDataPoints()) {
                                    bpmSum += dataPoint.getValue(Field.FIELD_BPM).asFloat();
                                    count++;
                                }
                            }
                        }

                        bpmAverage = (int) Math.ceil(bpmSum / count);
                        Log.i(TAG, "\tbpm: $bpmAverage average rate");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mostrarToast("Es necesario que brinde los permisos de actividad física: " + e);
                    }
                });
    }
    public void getWeightGoogleFit() {
        Fitness.getHistoryClient(getActivity(), GoogleSignIn.getAccountForExtension(getActivity(), fitnessOptions))
                .readDailyTotal(DataType.TYPE_WEIGHT)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        if (!dataSet.getDataPoints().isEmpty()) {
                            DataPoint dataPoint = dataSet.getDataPoints().get(0);

                            totalWeight = dataPoint.getValue(Field.FIELD_AVERAGE).asFloat();
                            weightDetailsTextView.setText(String.format("%s Kg", String.valueOf(totalWeight)));

                            if (totalWeight < maxWeight)
                                percentageWeight = (totalWeight / maxWeight) * 100;

                            weightPercentageTextView.setText(String.valueOf(new DecimalFormat("#.##").format(percentageWeight) + "%"));
                            weightCircularProgressBar.setProgress(percentageWeight);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mostrarToast("Primero acepte todos los permisos: " + e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DataSet>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSet> task) {
                        guardarDailyReport();
                        getOximetryToday();
                    }
                });
    }

    private void guardarDailyReport() {
        onProgressBarChanged.OnProgressBarVisible();

        int userId = patient == null ? sessionManager.getiduser() : patient.getId();
        DailyReport dailyReport = new DailyReport();
        dailyReport.setStepsHistory(new StepsHistory(totalSteps, userId));
        dailyReport.setDistanceHistory(new DistanceHistory(totalDistance, userId));
        dailyReport.setCaloriesHistory(new CaloriesHistory(totalCalories, userId));
        dailyReport.setHeartbeatHistory(new HeartbeatHistory(totalHeartBeat, userId));
        dailyReport.setSleepHistory(new SleepHistory(
                sessionStart == null ? -1 : bpmAverage,
                sessionStart == null ? FuncionesUtiles.getCurrentDateStr() : FuncionesUtiles.formatDateForAPIWithHour(sessionStart),
                sessionEnd == null ? FuncionesUtiles.getCurrentDateStr() : FuncionesUtiles.formatDateForAPIWithHour(sessionEnd),
                userId)
        );
        dailyReport.setWeightHistory(new WeightHistory(totalWeight, userId));
        JSONObject dailyReportJsonObject = dailyReport.toJsonObject();

        AndroidNetworking.post(HealthTrackApi.ADD_DAILY_REPORT)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(dailyReportJsonObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onProgressBarChanged.OnProgressBarHide();
                    }

                    @Override
                    public void onError(ANError anError) {
                        onProgressBarChanged.OnProgressBarHide();
                        FuncionesUtiles.mostrarMensaje(
                                "Error",
                                "Error al subir información al servidor: " + anError.getErrorDetail(),
                                ReportsFragment.this.getContext()
                        );
                    }
                });
    }
    private void mostrarToast(String mensaje) {
        Toast.makeText(this.getContext(), mensaje, Toast.LENGTH_LONG).show();
    }
    private long getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime().getTime();
    }
    private long getEndOfDay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        return calendar.getTime().getTime();
    }
    public void getOximetryToday(){
        onProgressBarChanged.OnProgressBarVisible();

        int userId = patient == null ? sessionManager.getiduser() : patient.getId();
        String url = HealthTrackApi.OXIMETRY_TODAY(userId);
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onProgressBarChanged.OnProgressBarHide();
                        if (response != null) {
                            Oximetry oximetry = Oximetry.from(response);
                            totalOximetry = oximetry.getSaturation();
                            oximetryDetailsTextView.setText(String.format("%s SpO2", String.valueOf(totalOximetry)));

                            if (totalOximetry < maxOximetry)
                                percentageOximetry = (totalOximetry*1.f / maxOximetry) * 100;

                            oximetryPercentageTextView.setText(String.valueOf(new DecimalFormat("#.##").format(percentageOximetry) + "%"));
                            oximetryCircularProgressBar.setProgress(percentageOximetry);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        onProgressBarChanged.OnProgressBarHide();
                        //mostrarToast(anError.getMessage());
                    }
                });
    }

    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public interface OnProgressBarChanged {
        void OnProgressBarVisible();
        void OnProgressBarHide();
    }
    private OnProgressBarChanged onProgressBarChanged;
    public void setOnProgressBarChanged(OnProgressBarChanged onProgressBarChanged) {
        this.onProgressBarChanged = onProgressBarChanged;
    }

    public interface OnStepHistoryClickedListener {
        void OnStepHistoryClicked();
    }
    private OnStepHistoryClickedListener onStepHistoryClickedListener;
    public void setOnStepHistoryClicked(OnStepHistoryClickedListener onStepHistoryClickedListener) {
        this.onStepHistoryClickedListener = onStepHistoryClickedListener;
    }

    public interface OnDistanceHistoryClickedListener {
        void OnDistanceHistoryClicked();
    }
    private OnDistanceHistoryClickedListener onDistanceHistoryClickedListener;
    public void setOnDistanceHistoryClicked(OnDistanceHistoryClickedListener onDistanceHistoryClickedListener) {
        this.onDistanceHistoryClickedListener = onDistanceHistoryClickedListener;
    }

    public interface OnCaloriesHistoryClickedListener {
        void OnCaloriesHistoryClicked();
    }
    private OnCaloriesHistoryClickedListener onCaloriesHistoryClickedListener;
    public void setOnCaloriesHistoryClicked(OnCaloriesHistoryClickedListener onCaloriesHistoryClickedListener) {
        this.onCaloriesHistoryClickedListener = onCaloriesHistoryClickedListener;
    }

    public interface OnHeartbeatHistoryClickedListener {
        void OnHeartbeatHistoryClicked();
    }
    private OnHeartbeatHistoryClickedListener onHeartbeatHistoryClickedListener;
    public void setOnHeartbeatHistoryClicked(OnHeartbeatHistoryClickedListener onHeartbeatHistoryClickedListener) {
        this.onHeartbeatHistoryClickedListener = onHeartbeatHistoryClickedListener;
    }

    public interface OnSleepHistoryClickedListener {
        void OnSleepHistoryClicked();
    }
    private OnSleepHistoryClickedListener onSleepHistoryClickedListener;
    public void setOnSleepHistoryClicked(OnSleepHistoryClickedListener onSleepHistoryClickedListener) {
        this.onSleepHistoryClickedListener = onSleepHistoryClickedListener;
    }

    public interface OnWeightHistoryClickedListener {
        void OnWeightHistoryClicked();
    }
    private OnWeightHistoryClickedListener onWeightHistoryClickedListener;
    public void setOnWeightHistoryClicked(OnWeightHistoryClickedListener onWeightHistoryClickedListener) {
        this.onWeightHistoryClickedListener = onWeightHistoryClickedListener;
    }

    public interface OnOximetryClickedListener {
        void OnOximetryClicked();
    }
    private OnOximetryClickedListener onOximetryClickedListener;
    public void setOnOximetryClicked(OnOximetryClickedListener onOximetryClickedListener) {
        this.onOximetryClickedListener = onOximetryClickedListener;
    }
}











