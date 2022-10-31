package com.karique.work.dev.healthtrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.models.DemeanorHistory;
import com.karique.work.dev.healthtrack.util.FuncionesFecha;

import java.util.List;


public class DemeanorHistoryAdapter extends RecyclerView.Adapter<DemeanorHistoryAdapter.DemeanorHistoryViewHolder> {
    private List<DemeanorHistory> demeanorHistories;
    private ContextProvider contextProvider;

    public DemeanorHistoryAdapter(List<DemeanorHistory> demeanorHistories, ContextProvider contextProvider) {
        this.demeanorHistories = demeanorHistories;
        this.contextProvider = contextProvider;
    }

    @Override
    public DemeanorHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DemeanorHistoryViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_demeanor, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DemeanorHistoryViewHolder holder, int position) {
        final DemeanorHistory demeanorHistory = this.demeanorHistories.get(position);

        holder.demeanorTitleTextView.setText(demeanorHistory.getDemeanor());
        holder.frecuencyTextView.setText(demeanorHistory.getFrecuency());
        holder.intensityTextView.setText(demeanorHistory.getIntensity());
        holder.recordDateTextView.setText(FuncionesFecha.formatDateWithHourSlashGuion(demeanorHistory.getRecordDate()));
    }

    @Override
    public int getItemCount() {
        return demeanorHistories.size();
    }

    public class DemeanorHistoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayoutCompat demeanorInnerLinearLayoutCompat;
        TextView demeanorTitleTextView;
        TextView frecuencyTextView;
        TextView intensityTextView;
        TextView recordDateTextView;

        public DemeanorHistoryViewHolder(View itemView) {
            super(itemView);
            demeanorInnerLinearLayoutCompat = itemView.findViewById(R.id.demeanorInnerLinearLayoutCompat);
            demeanorTitleTextView = itemView.findViewById(R.id.demeanorTitleTextView);
            frecuencyTextView = itemView.findViewById(R.id.frecuencyTextView);
            intensityTextView = itemView.findViewById(R.id.intensityTextView);
            recordDateTextView = itemView.findViewById(R.id.recordDateTextView);

            demeanorInnerLinearLayoutCompat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onDemeanorHistoryClickedListener != null) {
                        onDemeanorHistoryClickedListener.OnDemeanorHistoryClicked(demeanorHistories.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public interface ContextProvider {
        Context getContext();
    }

    public interface OnDemeanorHistoryClickedListener {
        void OnDemeanorHistoryClicked(DemeanorHistory news);
    }
    private OnDemeanorHistoryClickedListener onDemeanorHistoryClickedListener;
    public void setOnDemeanorHistoryClicked(OnDemeanorHistoryClickedListener onDemeanorHistoryClickedListener) {
        this.onDemeanorHistoryClickedListener = onDemeanorHistoryClickedListener;
    }
}
