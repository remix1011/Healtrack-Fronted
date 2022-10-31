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
import com.karique.work.dev.healthtrack.models.RecommendationsHistory;
import com.karique.work.dev.healthtrack.util.FuncionesFecha;

import java.util.List;



public class RecommendationsHistoryAdapter extends RecyclerView.Adapter<RecommendationsHistoryAdapter.RecommendationsHistoryViewHolder> {
    private List<RecommendationsHistory> recommendationsHistories;
    private ContextProvider contextProvider;

    public RecommendationsHistoryAdapter(List<RecommendationsHistory> recommendationsHistories, ContextProvider contextProvider) {
        this.recommendationsHistories = recommendationsHistories;
        this.contextProvider = contextProvider;
    }

    @Override
    public RecommendationsHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecommendationsHistoryViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_recommendations, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationsHistoryViewHolder holder, int position) {
        final RecommendationsHistory recommendationsHistory = this.recommendationsHistories.get(position);

        holder.recommendationDescriptionTextView.setText(recommendationsHistory.getDescription());
        holder.recordDateTextView.setText(FuncionesFecha.formatDateWithHourSlashGuion(recommendationsHistory.getRecordDate()));
    }

    @Override
    public int getItemCount() {
        return recommendationsHistories.size();
    }

    public class RecommendationsHistoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayoutCompat recommendationsInnerLinearLayoutCompat;
        TextView recommendationDescriptionTextView;
        TextView recordDateTextView;

        public RecommendationsHistoryViewHolder(View itemView) {
            super(itemView);
            recommendationsInnerLinearLayoutCompat = itemView.findViewById(R.id.recommendationsInnerLinearLayoutCompat);
            recommendationDescriptionTextView = itemView.findViewById(R.id.recommendationDescriptionTextView);
            recordDateTextView = itemView.findViewById(R.id.recordDateTextView);

            recommendationsInnerLinearLayoutCompat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onRecommendationsHistoryClickedListener != null) {
                        onRecommendationsHistoryClickedListener.OnRecommendationsHistoryClicked(recommendationsHistories.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public interface ContextProvider {
        Context getContext();
    }

    public interface OnRecommendationsHistoryClickedListener {
        void OnRecommendationsHistoryClicked(RecommendationsHistory recommendationsHistory);
    }
    private OnRecommendationsHistoryClickedListener onRecommendationsHistoryClickedListener;
    public void setOnRecommendationsHistoryClicked(OnRecommendationsHistoryClickedListener onRecommendationsHistoryClickedListener) {
        this.onRecommendationsHistoryClickedListener = onRecommendationsHistoryClickedListener;
    }
}
