package com.karique.work.dev.healthtrack.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.activities.LoginActivity;
import com.karique.work.dev.healthtrack.models.Oximetry;
import com.karique.work.dev.healthtrack.models.Patient;
import com.karique.work.dev.healthtrack.models.User;
import com.karique.work.dev.healthtrack.network.HealthTrackApi;
import com.karique.work.dev.healthtrack.session.SessionManager;
import com.libizo.CustomEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

public class ProfileFragment extends Fragment {
    public static final int FRAGMENT_TYPE_PROFILE = 4;

    private CustomEditText usernameEditText;
    private CustomEditText fullnameCustomEditText;
    private CustomEditText emailCustomEditText;
    private CustomEditText dniCustomEditText;
    private CustomEditText birthdayCustomEditText;
    private AppCompatRadioButton userResponsableAppCompatRadioButton;
    private AppCompatRadioButton userEspecialistaAppCompatRadioButton;

    private AppCompatImageButton editPasswordAppCompatImageButton;
    private ConstraintLayout logoutConstraintLayout;
    private LinearLayout passwordLinearLayout;
    private RadioGroup userTypeRadioGroup;
    private TextView especialistasAsignadosTextView;

    private SessionManager sessionManager;

    private Patient patient;

    public ProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarControles(view);
        inicializarEventos();
        inicializarDatos();
        if (sessionManager.getUser().isResponsible()) {
            getEspecialistas();
        }
    }

    private void inicializarControles(View view) {
        sessionManager = SessionManager.getInstance(getContext());
        usernameEditText = view.findViewById(R.id.usernameEditText);
        fullnameCustomEditText = view.findViewById(R.id.fullnameCustomEditText);
        emailCustomEditText = view.findViewById(R.id.emailCustomEditText);
        dniCustomEditText = view.findViewById(R.id.dniCustomEditText);
        birthdayCustomEditText = view.findViewById(R.id.birthdayCustomEditText);
        userResponsableAppCompatRadioButton = view.findViewById(R.id.userResponsableAppCompatRadioButton);
        userEspecialistaAppCompatRadioButton = view.findViewById(R.id.userEspecialistaAppCompatRadioButton);
        passwordLinearLayout = view.findViewById(R.id.passwordLinearLayout);

        editPasswordAppCompatImageButton = view.findViewById(R.id.editPasswordAppCompatImageButton);
        logoutConstraintLayout = view.findViewById(R.id.logoutBorderConstraintLayout);
        userTypeRadioGroup = view.findViewById(R.id.userTypeRadioGroup);
        especialistasAsignadosTextView = view.findViewById(R.id.especialistasAsignadosTextView);

        //if (sessionManager.getusertypeid() == 1) {
        //    editPasswordAppCompatImageButton.setVisibility(View.GONE);
        //    logoutConstraintLayout.setVisibility(View.GONE);
        //}
        //else {
        //    editPasswordAppCompatImageButton.setVisibility(View.VISIBLE);
        //    logoutConstraintLayout.setVisibility(View.VISIBLE);
        //}
    }
    private void inicializarEventos() {
        logoutConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogOutDialog();
            }
        });
        editPasswordAppCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onEditPasswordClickedListener != null){
                    onEditPasswordClickedListener.OnEditPasswordClicked();
                }
            }
        });
    }
    private void inicializarDatos() {
        usernameEditText.setText(patient == null ? sessionManager.getusername() : patient.getUser().getUsername());
        fullnameCustomEditText.setText(patient == null ? sessionManager.getfullname() : patient.getUser().getFullName());
        emailCustomEditText.setText(patient == null ? sessionManager.getemail() : patient.getUser().getEmail());
        dniCustomEditText.setText(patient == null ? sessionManager.getdni() : patient.getUser().getDni());
        birthdayCustomEditText.setText(patient == null ? sessionManager.getbirthday() : patient.getUser().getBirthday());

        userResponsableAppCompatRadioButton.setChecked(patient == null ? sessionManager.getUser().isResponsible() : patient.getUser().isResponsible());
        userEspecialistaAppCompatRadioButton.setChecked(patient == null ? sessionManager.getUser().isSpecialist() : patient.getUser().isSpecialist());
        passwordLinearLayout.setVisibility(patient == null ? View.VISIBLE : View.GONE);
        logoutConstraintLayout.setVisibility(patient == null ? View.VISIBLE : View.GONE);

        userResponsableAppCompatRadioButton.setEnabled(false);
        userResponsableAppCompatRadioButton.setClickable(false);
        userResponsableAppCompatRadioButton.setFocusable(false);

        userEspecialistaAppCompatRadioButton.setEnabled(false);
        userEspecialistaAppCompatRadioButton.setClickable(false);
        userEspecialistaAppCompatRadioButton.setFocusable(false);

        //especialistasAsignadosTextView.setVisibility(sessionManager.getUser().isResponsible() ? View.VISIBLE : View.GONE);
    }
    public void getEspecialistas(){
        onProgressBarChanged.OnProgressBarVisible();

        int userId = sessionManager.getiduser();
        String url = HealthTrackApi.USER_SPECIALIST_ASIGNED(userId);
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        onProgressBarChanged.OnProgressBarHide();
                        if (response != null) {
                            List<User> especialistas = User.from(response);
                            StringBuilder stringBuilder = new StringBuilder();

                            if (especialistas.size() == 0) {
                                stringBuilder
                                        .append("Especialistas asignados\n")
                                        .append(getContext().getString(R.string.no_data))
                                        .append("\n");
                            } else {
                                if (especialistas.size() == 1) {
                                    stringBuilder.append("Especialista asignado\n");
                                }
                                else {
                                    stringBuilder.append("Especialistas asignados\n");
                                }

                                for (int i = 0; i < especialistas.size(); i++) {
                                    stringBuilder
                                            .append("- ")
                                            .append(especialistas.get(i).getFullName())
                                            .append("\n");
                                }
                                stringBuilder.append("\n");
                            }

                            especialistasAsignadosTextView.setText(stringBuilder.toString());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        onProgressBarChanged.OnProgressBarHide();
                        //mostrarToast(anError.getMessage());
                    }
                });
    }

    private void showLogOutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Seguro que desea cerrar sesión?")
                .setCancelable(true)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logOut();
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
    private void logOut(){
        sessionManager.deleteUser();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public interface OnEditPasswordClickedListener {
        void OnEditPasswordClicked();
    }
    private OnEditPasswordClickedListener onEditPasswordClickedListener;
    public void setOnEditPasswordClicked(OnEditPasswordClickedListener onEditPasswordClickedListener) {
        this.onEditPasswordClickedListener = onEditPasswordClickedListener;
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