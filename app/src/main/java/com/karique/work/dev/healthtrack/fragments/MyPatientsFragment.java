package com.karique.work.dev.healthtrack.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.adapters.MyPatientsAdapter;
import com.karique.work.dev.healthtrack.models.Patient;
import com.karique.work.dev.healthtrack.models.User;
import com.karique.work.dev.healthtrack.network.HealthTrackApi;
import com.karique.work.dev.healthtrack.session.SessionManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyPatientsFragment extends Fragment {
    public static final int FRAGMENT_TYPE_MY_PATIENTS = 1;

    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;
    private AppCompatButton reintentarAppCompatButton;

    private RecyclerView myPatientsRecyclerView;
    private MyPatientsAdapter myPatientsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Patient> patients;

    private SessionManager sessionManager;

    private EditText searchEditText;

    public MyPatientsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_patients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarControles(view);
        inicializarEventos(view);
        inicializarDatos(view);
        getPatients();
    }

    private void inicializarControles(View view) {
        myPatientsRecyclerView = view.findViewById(R.id.myPatientsRecyclerView);
        messageConstraintLayout = view.findViewById(R.id.messageConstraintLayout);
        messageTextView = view.findViewById(R.id.messageTextView);
        reintentarAppCompatButton = view.findViewById(R.id.reintentarAppCompatButton);
        searchEditText = view.findViewById(R.id.searchEditText);
    }
    private void inicializarEventos(View view) {
        reintentarAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPatients();
            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                myPatientsAdapter.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
    private void inicializarDatos(View view) {
        sessionManager = SessionManager.getInstance(getContext());
        patients = new ArrayList<>();
        myPatientsAdapter = new MyPatientsAdapter(patients, new MyPatientsAdapter.ContextProvider() {
            @Override
            public Context getContext() {
                return MyPatientsFragment.this.getContext();
            }
        });
        myPatientsAdapter.setOnPatientClicked(new MyPatientsAdapter.OnPatientClickedListener() {
            @Override
            public void OnPatientClicked(Patient patient) {
                if (onPatientClickedListener != null) {
                    onPatientClickedListener.OnPatientClicked(patient);
                }
            }
        });
        myPatientsAdapter.setOnPatientLongClicked(new MyPatientsAdapter.OnPatientLongClickedListener() {
            @Override
            public void OnPatientLongClicked(Patient patient) {
                if (onPatientLongClickedListener != null) {
                    onPatientLongClickedListener.OnPatientLongClicked(patient);
                }
            }
        });
        layoutManager = new LinearLayoutManager(getActivity());
        myPatientsRecyclerView.setAdapter(myPatientsAdapter);
        myPatientsRecyclerView.setLayoutManager(layoutManager);
    }

    public void getPatients(){
        onProgressBarChanged.OnProgressBarVisible();

        AndroidNetworking.get(HealthTrackApi.PATIENTS_BY_USER(sessionManager.getiduser()))
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        onProgressBarChanged.OnProgressBarHide();
                        messageConstraintLayout.setVisibility(View.GONE);

                        patients.clear();
                        patients.addAll(Patient.from(response));
                        Collections.reverse(patients);
                        myPatientsAdapter.setPatientsGeneral(patients);
                        myPatientsAdapter.notifyDataSetChanged();

                        if (patients.isEmpty()){
                            showNoDataMessage("Sin datos");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        onProgressBarChanged.OnProgressBarHide();
                        showNoDataMessage("Error de carga: " + anError.getErrorDetail());
                    }
                });
    }
    private void showNoDataMessage(String message){
        messageTextView.setText(message);
        messageConstraintLayout.setVisibility(View.VISIBLE);
    }

    public interface OnPatientClickedListener {
        void OnPatientClicked(Patient patient);
    }
    private OnPatientClickedListener onPatientClickedListener;
    public void setOnPatientClicked(OnPatientClickedListener onPatientClickedListener) {
        this.onPatientClickedListener = onPatientClickedListener;
    }

    public interface OnPatientLongClickedListener {
        void OnPatientLongClicked(Patient patient);
    }
    private OnPatientLongClickedListener onPatientLongClickedListener;
    public void setOnPatientLongClicked(OnPatientLongClickedListener onPatientLongClickedListener) {
        this.onPatientLongClickedListener = onPatientLongClickedListener;
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