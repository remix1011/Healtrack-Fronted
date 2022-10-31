package com.karique.work.dev.healthtrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.models.Patient;

import java.util.ArrayList;
import java.util.List;


public class MyPatientsAdapter extends RecyclerView.Adapter<MyPatientsAdapter.UserViewHolder> implements Filterable {
    private List<Patient> patients;
    private List<Patient> patientsGeneral;
    private ContextProvider contextProvider;

    public MyPatientsAdapter(List<Patient> patients, ContextProvider contextProvider) {
        this.patients = patients;
        this.patientsGeneral = new ArrayList<>();
        this.contextProvider = contextProvider;
    }
    public void setPatientsGeneral(List<Patient> patientsGeneral) {
        this.patientsGeneral.clear();
        this.patientsGeneral.addAll(patientsGeneral);
    }

    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_patient, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final Patient user = this.patients.get(position);

        holder.fullNameTextView.setText(user.getFullName());
        holder.userNameTextView.setText(user.getUsername());

        holder.clinicNameTextView.setText("Clínica: " + user.getClinicName());
        holder.enviromentNameTextView.setText("Ambiente: " + user.getEnviromentName());
        holder.diseaseCategoryDescriptionTextView.setText("Avance de la enfermedad: " + user.getDiseaseCategoryDescription());
        holder.treatmentDescriptionTextView.setText("Tratamiento: " + user.getTreatmentDescription());
        holder.demeanorTextView.setText("Conducta: " + user.getDemeanor());
        holder.ageTextView.setText("Edad: " + user.getEdad());
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout patientConstraintLayout;
        TextView fullNameTextView;
        TextView userNameTextView;

        TextView clinicNameTextView;
        TextView enviromentNameTextView;
        TextView diseaseCategoryDescriptionTextView;
        TextView treatmentDescriptionTextView;
        TextView demeanorTextView;
        TextView ageTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            patientConstraintLayout = itemView.findViewById(R.id.patientConstraintLayout);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);

            clinicNameTextView = itemView.findViewById(R.id.clinicNameTextView);
            enviromentNameTextView = itemView.findViewById(R.id.enviromentNameTextView);
            diseaseCategoryDescriptionTextView = itemView.findViewById(R.id.diseaseCategoryDescriptionTextView);
            treatmentDescriptionTextView = itemView.findViewById(R.id.treatmentDescriptionTextView);
            demeanorTextView = itemView.findViewById(R.id.demeanorTextView);
            ageTextView = itemView.findViewById(R.id.ageTextView);

            patientConstraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPatientClickedListener != null) {
                        onPatientClickedListener.OnPatientClicked(patients.get(getAdapterPosition()));
                    }
                }
            });
            patientConstraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (onPatientLongClickedListener != null) {
                        onPatientLongClickedListener.OnPatientLongClicked(patients.get(getAdapterPosition()));
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return new MiFiltro();
    }
    private class MiFiltro extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return new FilterResults();
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (constraint.length() == 0) {
                patients.clear();
                patients.addAll(patientsGeneral);
            }
            else {
                List<Patient> autoevaluacionesAux = new ArrayList<>();
                for (int i = 0; i < patientsGeneral.size(); i++) {
                    Patient patient = patientsGeneral.get(i);
                    //busqueda por usuario
                    if (patient.getUsername().contains(constraint)) {
                        autoevaluacionesAux.add(patient);
                    }
                    //busqueda por nombre
                    else if (patient.getFullName().contains(constraint)) {
                        autoevaluacionesAux.add(patient);
                    }
                    //busqueda por clinica
                    else if (patient.getClinicName().contains(constraint)) {
                        autoevaluacionesAux.add(patient);
                    }
                    //busqueda por ambiente
                    else if (patient.getEnviromentName().contains(constraint)) {
                        autoevaluacionesAux.add(patient);
                    }
                    //busqueda por categoria de la enfermedad
                    else if (patient.getDiseaseCategoryDescription().contains(constraint)) {
                        autoevaluacionesAux.add(patient);
                    }
                    //busqueda por tratamiento
                    else if (patient.getTreatmentDescription().contains(constraint)) {
                        autoevaluacionesAux.add(patient);
                    }
                    //busqueda por conducta
                    else if (patient.getDemeanor().contains(constraint)) {
                        autoevaluacionesAux.add(patient);
                    }
                    //busqueda por edad
                    else if (String.valueOf(patient.getEdad()).contains(constraint)) {
                        autoevaluacionesAux.add(patient);
                    }
                }
                patients.clear();
                patients.addAll(autoevaluacionesAux);
            }
            notifyDataSetChanged();
        }
    }

    public interface ContextProvider {
        Context getContext();
    }

    public interface OnPatientClickedListener {
        void OnPatientClicked(Patient patient);
    }
    private OnPatientClickedListener onPatientClickedListener;
    public void setOnPatientClicked(OnPatientClickedListener onPatientClickedListener) {
        this.onPatientClickedListener = onPatientClickedListener;
    }

    public interface OnPatientLongClickedListener {
        void OnPatientLongClicked(Patient user);
    }
    private OnPatientLongClickedListener onPatientLongClickedListener;
    public void setOnPatientLongClicked(OnPatientLongClickedListener onPatientLongClickedListener) {
        this.onPatientLongClickedListener = onPatientLongClickedListener;
    }
}
