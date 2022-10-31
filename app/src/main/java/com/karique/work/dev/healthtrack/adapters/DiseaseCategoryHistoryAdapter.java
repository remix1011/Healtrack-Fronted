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
import com.karique.work.dev.healthtrack.models.DiseaseCategoryHistory;
import com.karique.work.dev.healthtrack.util.FuncionesFecha;

import java.util.List;


public class DiseaseCategoryHistoryAdapter extends RecyclerView.Adapter<DiseaseCategoryHistoryAdapter.DiseaseCategoryHistoryViewHolder> {
    private List<DiseaseCategoryHistory> diseaseCategoryHistories;
    private ContextProvider contextProvider;

    public DiseaseCategoryHistoryAdapter(List<DiseaseCategoryHistory> diseaseCategoryHistories, ContextProvider contextProvider) {
        this.diseaseCategoryHistories = diseaseCategoryHistories;
        this.contextProvider = contextProvider;
    }

    @Override
    public DiseaseCategoryHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiseaseCategoryHistoryViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_disease_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseCategoryHistoryViewHolder holder, int position) {
        final DiseaseCategoryHistory demeanorHistory = this.diseaseCategoryHistories.get(position);

        holder.diseaseCategoryTypeDescriptionTextView.setText(demeanorHistory.getDiseaseCategoryType().getDescription());
        holder.recordDateTextView.setText(FuncionesFecha.formatDateWithHourSlashGuion(demeanorHistory.getRecordDate()));
    }

    @Override
    public int getItemCount() {
        return diseaseCategoryHistories.size();
    }

    public class DiseaseCategoryHistoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayoutCompat diseaseCategoryInnerLinearLayoutCompat;
        TextView diseaseCategoryTypeDescriptionTextView;
        TextView recordDateTextView;

        public DiseaseCategoryHistoryViewHolder(View itemView) {
            super(itemView);
            diseaseCategoryInnerLinearLayoutCompat = itemView.findViewById(R.id.diseaseCategoryInnerLinearLayoutCompat);
            diseaseCategoryTypeDescriptionTextView = itemView.findViewById(R.id.diseaseCategoryTypeDescriptionTextView);
            recordDateTextView = itemView.findViewById(R.id.recordDateTextView);

            diseaseCategoryInnerLinearLayoutCompat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onDiseaseCategoryHistoryClickedListener != null) {
                        onDiseaseCategoryHistoryClickedListener.OnDiseaseCategoryHistoryClicked(diseaseCategoryHistories.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public interface ContextProvider {
        Context getContext();
    }

    public interface OnDiseaseCategoryHistoryClickedListener {
        void OnDiseaseCategoryHistoryClicked(DiseaseCategoryHistory diseaseCategoryHistory);
    }
    private OnDiseaseCategoryHistoryClickedListener onDiseaseCategoryHistoryClickedListener;
    public void setOnDiseaseCategoryHistoryClicked(OnDiseaseCategoryHistoryClickedListener onDiseaseCategoryHistoryClickedListener) {
        this.onDiseaseCategoryHistoryClickedListener = onDiseaseCategoryHistoryClickedListener;
    }
}
