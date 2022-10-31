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
import com.karique.work.dev.healthtrack.models.TreatmentHistory;
import com.karique.work.dev.healthtrack.util.FuncionesFecha;

import java.util.List;


public class TreatmentHistoryAdapter extends RecyclerView.Adapter<TreatmentHistoryAdapter.TreatmentHistoryViewHolder> {
    private List<TreatmentHistory> treatmentHistories;
    private ContextProvider contextProvider;

    public TreatmentHistoryAdapter(List<TreatmentHistory> treatmentHistories, ContextProvider contextProvider) {
        this.treatmentHistories = treatmentHistories;
        this.contextProvider = contextProvider;
    }

    @Override
    public TreatmentHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TreatmentHistoryViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_treatment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TreatmentHistoryViewHolder holder, int position) {
        final TreatmentHistory treatmentHistory = this.treatmentHistories.get(position);

        holder.treatmentDescriptionTextView.setText(treatmentHistory.getDescription());
        holder.recordDateTextView.setText(FuncionesFecha.formatDateWithHourSlashGuion(treatmentHistory.getRecordDate()));
    }

    @Override
    public int getItemCount() {
        return treatmentHistories.size();
    }

    public class TreatmentHistoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayoutCompat treatmentHistoryInnerLinearLayoutCompat;
        TextView treatmentDescriptionTextView;
        TextView recordDateTextView;

        public TreatmentHistoryViewHolder(View itemView) {
            super(itemView);
            treatmentHistoryInnerLinearLayoutCompat = itemView.findViewById(R.id.treatmentHistoryInnerLinearLayoutCompat);
            treatmentDescriptionTextView = itemView.findViewById(R.id.treatmentDescriptionTextView);
            recordDateTextView = itemView.findViewById(R.id.recordDateTextView);

            treatmentHistoryInnerLinearLayoutCompat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onTreatmentHistoryClickedListener != null) {
                        onTreatmentHistoryClickedListener.OnTreatmentHistoryClicked(treatmentHistories.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public interface ContextProvider {
        Context getContext();
    }

    public interface OnTreatmentHistoryClickedListener {
        void OnTreatmentHistoryClicked(TreatmentHistory treatmentHistory);
    }
    private OnTreatmentHistoryClickedListener onTreatmentHistoryClickedListener;
    public void setOnTreatmentHistoryClicked(OnTreatmentHistoryClickedListener onTreatmentHistoryClickedListener) {
        this.onTreatmentHistoryClickedListener = onTreatmentHistoryClickedListener;
    }
}
