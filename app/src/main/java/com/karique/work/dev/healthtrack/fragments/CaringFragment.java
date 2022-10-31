package com.karique.work.dev.healthtrack.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
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
import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.models.Patient;
import com.karique.work.dev.healthtrack.models.UserCaringDetails;
import com.karique.work.dev.healthtrack.network.HealthTrackApi;
import com.karique.work.dev.healthtrack.session.SessionManager;

import org.json.JSONObject;

public class CaringFragment extends Fragment {
    public static final int FRAGMENT_TYPE_CARING = 2;

    private SessionManager sessionManager;

    private Patient patient;
    private UserCaringDetails userCaringDetails;

    private TextView clinicNameTextView;
    private TextView enviromentNameTextView;
    private TextView diseaseCategoryTypeDescriptionTextView;
    private TextView treatmentDescriptionTextView;
    private TextView demeanorTitleTextView;
    private TextView frecuencyTextView;
    private TextView intensityTextView;
    private TextView recommendationDescriptionTextView;

    private LinearLayoutCompat caringEnvironmentInnerLinearLayoutCompat;
    private LinearLayoutCompat diseaseCategoryInnerLinearLayoutCompat;
    private LinearLayoutCompat treatmentHistoryInnerLinearLayoutCompat;
    private LinearLayoutCompat demeanorInnerLinearLayoutCompat;
    private LinearLayoutCompat recommendationsInnerLinearLayoutCompat;

    private LinearLayoutCompat allDataLinearLayoutCompat;

    public CaringFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_caring, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializarControles(view);
        inicializarEventos(view);
        inicializarDatos(view);
        getUserCaringDetails();
    }

    private void inicializarControles(View view) {
        clinicNameTextView = view.findViewById(R.id.clinicNameTextView);
        enviromentNameTextView = view.findViewById(R.id.enviromentNameTextView);
        diseaseCategoryTypeDescriptionTextView = view.findViewById(R.id.diseaseCategoryTypeDescriptionTextView);
        treatmentDescriptionTextView = view.findViewById(R.id.treatmentDescriptionTextView);
        demeanorTitleTextView = view.findViewById(R.id.demeanorTitleTextView);
        frecuencyTextView = view.findViewById(R.id.frecuencyTextView);
        intensityTextView = view.findViewById(R.id.intensityTextView);
        recommendationDescriptionTextView = view.findViewById(R.id.recommendationDescriptionTextView);

        caringEnvironmentInnerLinearLayoutCompat = view.findViewById(R.id.caringEnvironmentInnerLinearLayoutCompat);
        diseaseCategoryInnerLinearLayoutCompat = view.findViewById(R.id.diseaseCategoryInnerLinearLayoutCompat);
        treatmentHistoryInnerLinearLayoutCompat = view.findViewById(R.id.treatmentHistoryInnerLinearLayoutCompat);
        demeanorInnerLinearLayoutCompat = view.findViewById(R.id.demeanorInnerLinearLayoutCompat);
        recommendationsInnerLinearLayoutCompat = view.findViewById(R.id.recommendationsInnerLinearLayoutCompat);

        allDataLinearLayoutCompat = view.findViewById(R.id.allDataLinearLayoutCompat);
    }
    private void inicializarEventos(View view) {
        caringEnvironmentInnerLinearLayoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCaringEnviromentClickedListener != null) {
                    onCaringEnviromentClickedListener.OnCaringEnviromentClicked();
                }
            }
        });
        diseaseCategoryInnerLinearLayoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDiseaseCategoryClickedListener != null) {
                    onDiseaseCategoryClickedListener.OnDiseaseCategoryClicked();
                }
            }
        });
        treatmentHistoryInnerLinearLayoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTreatmentHistoryClickedListener != null) {
                    onTreatmentHistoryClickedListener.OnTreatmentHistoryClicked();
                }
            }
        });
        demeanorInnerLinearLayoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDemeanorClickedListener != null) {
                    onDemeanorClickedListener.OnDemeanorClicked();
                }
            }
        });
        recommendationsInnerLinearLayoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRecommendationsClickedListener != null) {
                    onRecommendationsClickedListener.OnRecommendationsClicked();
                }
            }
        });
    }
    private void inicializarDatos(View view) {
        sessionManager = SessionManager.getInstance(getContext());
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void getUserCaringDetails(){
        onProgressBarChanged.OnProgressBarVisible();
        allDataLinearLayoutCompat.setVisibility(View.GONE);

        int userId = patient == null ? sessionManager.getiduser() : patient.getId();
        String url = HealthTrackApi.USER_CARING_DETAILS_BY_USER(userId);
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onProgressBarChanged.OnProgressBarHide();
                        allDataLinearLayoutCompat.setVisibility(View.VISIBLE);
                        userCaringDetails = UserCaringDetails.from(response);

                        clinicNameTextView.setText(userCaringDetails.getCaringEnvironment() == null ? "No hay datos" : userCaringDetails.getCaringEnvironment().getClinicName());
                        enviromentNameTextView.setText(userCaringDetails.getCaringEnvironment() == null ? "No hay datos" : userCaringDetails.getCaringEnvironment().getEnviromentName());
                        diseaseCategoryTypeDescriptionTextView.setText(userCaringDetails.getDiseaseCategory() == null ? "No hay datos" : userCaringDetails.getDiseaseCategory().getDiseaseCategoryType().getDescription());
                        treatmentDescriptionTextView.setText(userCaringDetails.getTreatment() == null ? "No hay datos" : userCaringDetails.getTreatment().getDescription());
                        demeanorTitleTextView.setText("Conducta: " + (userCaringDetails.getDemeanor() == null ? "No hay datos" : userCaringDetails.getDemeanor().getDemeanor()));
                        frecuencyTextView.setText(userCaringDetails.getDemeanor() == null ? "No hay datos" : userCaringDetails.getDemeanor().getFrecuency());
                        intensityTextView.setText(userCaringDetails.getDemeanor() == null ? "No hay datos" : userCaringDetails.getDemeanor().getIntensity());
                        recommendationDescriptionTextView.setText(userCaringDetails.getRecommendations() == null ? "No hay datos" : userCaringDetails.getRecommendations().getDescription());
                    }

                    @Override
                    public void onError(ANError anError) {
                        onProgressBarChanged.OnProgressBarHide();
                        allDataLinearLayoutCompat.setVisibility(View.GONE);
                        mostrarToast(anError.getMessage());
                    }
                });
    }
    private void mostrarToast(String mensaje) {
        Toast.makeText(this.getContext(), mensaje, Toast.LENGTH_LONG).show();
    }

    public interface OnCaringEnviromentClickedListener {
        void OnCaringEnviromentClicked();
    }
    private OnCaringEnviromentClickedListener onCaringEnviromentClickedListener;
    public void setOnCaringEnviromentClicked(OnCaringEnviromentClickedListener onCaringEnviromentClickedListener) {
        this.onCaringEnviromentClickedListener = onCaringEnviromentClickedListener;
    }

    public interface OnDiseaseCategoryClickedListener {
        void OnDiseaseCategoryClicked();
    }
    private OnDiseaseCategoryClickedListener onDiseaseCategoryClickedListener;
    public void setOnDiseaseCategoryClicked(OnDiseaseCategoryClickedListener onDiseaseCategoryClickedListener) {
        this.onDiseaseCategoryClickedListener = onDiseaseCategoryClickedListener;
    }

    public interface OnTreatmentHistoryClickedListener {
        void OnTreatmentHistoryClicked();
    }
    private OnTreatmentHistoryClickedListener onTreatmentHistoryClickedListener;
    public void setOnTreatmentHistoryClicked(OnTreatmentHistoryClickedListener onTreatmentHistoryClickedListener) {
        this.onTreatmentHistoryClickedListener = onTreatmentHistoryClickedListener;
    }

    public interface OnDemeanorClickedListener {
        void OnDemeanorClicked();
    }
    private OnDemeanorClickedListener onDemeanorClickedListener;
    public void setOnDemeanorClicked(OnDemeanorClickedListener onDemeanorClickedListener) {
        this.onDemeanorClickedListener = onDemeanorClickedListener;
    }

    public interface OnRecommendationsClickedListener {
        void OnRecommendationsClicked();
    }
    private OnRecommendationsClickedListener onRecommendationsClickedListener;
    public void setOnRecommendationsClicked(OnRecommendationsClickedListener onRecommendationsClickedListener) {
        this.onRecommendationsClickedListener = onRecommendationsClickedListener;
    }


    public interface OnProgressBarChanged {
        void OnProgressBarVisible();
        void OnProgressBarHide();
    }
    private OnProgressBarChanged onProgressBarChanged;
    public void setOnProgressBarChanged(OnProgressBarChanged onProgressBarChanged) {
        this.onProgressBarChanged = onProgressBarChanged;
    }
}