package com.karique.work.dev.healthtrack.fragments;

import static com.karique.work.dev.healthtrack.activities.ResponsibleMainActivity.GOOGLE_FIT_PERMISSIONS_REQUEST_CODE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.models.Patient;
import com.karique.work.dev.healthtrack.models.UserCaringDetails;
import com.karique.work.dev.healthtrack.models.UserMetricsDetails;
import com.karique.work.dev.healthtrack.network.HealthTrackApi;
import com.karique.work.dev.healthtrack.session.SessionManager;
import com.karique.work.dev.healthtrack.util.FuncionesFecha;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RisponsibleReportsFragment extends Fragment {
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

    UserMetricsDetails userMetricsDetails;

    public RisponsibleReportsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_risponsible_reports, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarControles(view);
        inicializarDatos();
        inicializarEventos();
        getUserMetricsDetails();
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

    public void getUserMetricsDetails(){
        onProgressBarChanged.OnProgressBarVisible();

        int userId = patient == null ? sessionManager.getiduser() : patient.getId();
        String url = HealthTrackApi.USER_METRIC_DETAILS_TODAY_BY_USER(userId);
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onProgressBarChanged.OnProgressBarHide();
                        userMetricsDetails = UserMetricsDetails.from(response);

                        if (userMetricsDetails.getStepsHistory() != null) {
                            totalSteps = userMetricsDetails.getStepsHistory().getSteps();
                            stepsDetailsTextView.setText(String.format("%s pasos", String.valueOf(totalSteps)));

                            if (totalSteps < maxSteps)
                                percentageSteps = (totalSteps*1.f / maxSteps) * 100;

                            stepsPercentageTextView.setText(String.valueOf(new DecimalFormat("#.##").format(percentageSteps) + "%"));
                            stepsCircularProgressBar.setProgress(percentageSteps);
                        }

                        if (userMetricsDetails.getDistanceHistory() != null) {
                            totalDistance = userMetricsDetails.getDistanceHistory().getDistance();
                            distanceDetailsTextView.setText(String.format("%s metros", String.valueOf(totalDistance)));

                            if (totalDistance < maxDistance)
                                percentageDistance = (totalDistance*1.f / maxDistance) * 100;

                            distancePercentageTextView.setText(String.valueOf(new DecimalFormat("#.##").format(percentageDistance) + "%"));
                            distanceCircularProgressBar.setProgress(percentageDistance);
                        }

                        if (userMetricsDetails.getCaloriesHistory() != null) {
                            totalCalories = userMetricsDetails.getCaloriesHistory().getCalories();
                            caloriesDetailsTextView.setText(String.format("%s calorías", String.valueOf(totalCalories)));

                            if (totalCalories < maxCalories)
                                percentageCalories = (totalCalories*1.f / maxCalories) * 100;

                            caloriesPercentageTextView.setText(String.valueOf(new DecimalFormat("#.##").format(percentageCalories) + "%"));
                            caloriesCircularProgressBar.setProgress(percentageCalories);
                        }

                        if (userMetricsDetails.getHeartbeatHistory() != null) {
                            totalHeartBeat = userMetricsDetails.getHeartbeatHistory().getAverageBpm();
                            heartBeatDetailsTextView.setText(String.format("%s Bpm", String.valueOf(totalHeartBeat)));

                            if (totalHeartBeat < maxHeartBeat)
                                percentageHeartBeat = (totalHeartBeat*1.f / maxHeartBeat) * 100;

                            heartBeatPercentageTextView.setText(String.valueOf(new DecimalFormat("#.##").format(percentageHeartBeat) + "%"));
                            heartBeatCircularProgressBar.setProgress(percentageHeartBeat);
                        }

                        if (userMetricsDetails.getSleepHistory() != null) {
                            Date startDate = FuncionesFecha.getDateWithHourFromString(userMetricsDetails.getSleepHistory().getStartDateTime());
                            Date endDate = FuncionesFecha.getDateWithHourFromString(userMetricsDetails.getSleepHistory().getEndDateTime());
                            long milliseconds = endDate.getTime() - startDate.getTime();
                            int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                            int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
                            float total = (float) (hours + (minutes*1.0/100));
                            sleepDetailsTextView.setText(String.format("%s Horas %s min", String.valueOf(hours), String.valueOf(minutes)));

                            if (totalSleep < maxSleep)
                                percentageSleep = (totalSleep*1.f / maxSleep) * 100;

                            sleepPercentageTextView.setText(String.valueOf(new DecimalFormat("#.##").format(percentageSleep) + "%"));
                            sleepCircularProgressBar.setProgress(percentageSleep);
                        }

                        if (userMetricsDetails.getWeightHistory() != null) {
                            totalWeight = (int)userMetricsDetails.getWeightHistory().getWeight();
                            weightDetailsTextView.setText(String.format("%s Kg", String.valueOf(totalWeight)));

                            if (totalWeight < maxWeight)
                                percentageWeight = (totalWeight / maxWeight) * 100;

                            weightPercentageTextView.setText(String.valueOf(new DecimalFormat("#.##").format(percentageWeight) + "%"));
                            weightCircularProgressBar.setProgress(percentageWeight);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        onProgressBarChanged.OnProgressBarHide();
                        mostrarToast(anError.getMessage());
                    }
                });
    }
    private void mostrarToast(String mensaje) {
        Toast.makeText(this.getContext(), mensaje, Toast.LENGTH_LONG).show();
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