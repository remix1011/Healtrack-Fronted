package com.karique.work.dev.healthtrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.models.CaringEnvironmentHistory;
import com.karique.work.dev.healthtrack.util.FuncionesFecha;

import java.util.List;


public class CaringEnvironmentHistoryAdapter extends RecyclerView.Adapter<CaringEnvironmentHistoryAdapter.CaringEnvironmentHistoryViewHolder> {
    private List<CaringEnvironmentHistory> caringEnvironmentHistories;
    private ContextProvider contextProvider;

    public CaringEnvironmentHistoryAdapter(List<CaringEnvironmentHistory> caringEnvironmentHistories, ContextProvider contextProvider) {
        this.caringEnvironmentHistories = caringEnvironmentHistories;
        this.contextProvider = contextProvider;
    }

    @Override
    public CaringEnvironmentHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CaringEnvironmentHistoryViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_caring_environment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CaringEnvironmentHistoryViewHolder holder, int position) {
        final CaringEnvironmentHistory caringEnvironmentHistory = this.caringEnvironmentHistories.get(position);

        holder.clinicNameTextView.setText(caringEnvironmentHistory.getClinicName());
        holder.enviromentNameTextView.setText(caringEnvironmentHistory.getEnviromentName());
        holder.recordDateTextView.setText(FuncionesFecha.formatDateWithHourSlashGuion(caringEnvironmentHistory.getRecordDate()));
    }

    @Override
    public int getItemCount() {
        return caringEnvironmentHistories.size();
    }

    public class CaringEnvironmentHistoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayoutCompat caringEnvironmentInnerLinearLayoutCompat;
        TextView clinicNameTextView;
        TextView enviromentNameTextView;
        TextView recordDateTextView;

        public CaringEnvironmentHistoryViewHolder(View itemView) {
            super(itemView);
            caringEnvironmentInnerLinearLayoutCompat = itemView.findViewById(R.id.caringEnvironmentInnerLinearLayoutCompat);
            clinicNameTextView = itemView.findViewById(R.id.clinicNameTextView);
            enviromentNameTextView = itemView.findViewById(R.id.enviromentNameTextView);
            recordDateTextView = itemView.findViewById(R.id.recordDateTextView);

            caringEnvironmentInnerLinearLayoutCompat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onCaringEnvironmentHistoryClickedListener != null) {
                        onCaringEnvironmentHistoryClickedListener.OnCaringEnvironmentHistoryClicked(caringEnvironmentHistories.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public interface ContextProvider {
        Context getContext();
    }

    public interface OnCaringEnvironmentHistoryClickedListener {
        void OnCaringEnvironmentHistoryClicked(CaringEnvironmentHistory caringEnvironmentHistory);
    }
    private OnCaringEnvironmentHistoryClickedListener onCaringEnvironmentHistoryClickedListener;
    public void setOnCaringEnvironmentHistoryClicked(OnCaringEnvironmentHistoryClickedListener onCaringEnvironmentHistoryClickedListener) {
        this.onCaringEnvironmentHistoryClickedListener = onCaringEnvironmentHistoryClickedListener;
    }
}
