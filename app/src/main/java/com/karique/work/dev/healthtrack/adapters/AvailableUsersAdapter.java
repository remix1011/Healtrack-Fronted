package com.karique.work.dev.healthtrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.karique.work.dev.healthtrack.R;
import com.karique.work.dev.healthtrack.models.User;

import java.util.List;


public class AvailableUsersAdapter extends RecyclerView.Adapter<AvailableUsersAdapter.UserViewHolder> {
    private List<User> users;
    private ContextProvider contextProvider;

    public AvailableUsersAdapter(List<User> users, ContextProvider contextProvider) {
        this.users = users;
        this.contextProvider = contextProvider;
    }

    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_available_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final User user = this.users.get(position);

        holder.fullNameTextView.setText(user.getFullName());
        holder.userNameTextView.setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout patientConstraintLayout;
        TextView fullNameTextView;
        TextView userNameTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            patientConstraintLayout = itemView.findViewById(R.id.patientConstraintLayout);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);

            patientConstraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onUserClickedListener != null) {
                        onUserClickedListener.OnUserClicked(users.get(getAdapterPosition()));
                    }
                }
            });
            patientConstraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (onUserLongClickedListener != null) {
                        onUserLongClickedListener.OnUserLongClicked(users.get(getAdapterPosition()));
                    }
                    return true;
                }
            });
        }
    }

    public interface ContextProvider {
        Context getContext();
    }

    public interface OnUserClickedListener {
        void OnUserClicked(User user);
    }
    private OnUserClickedListener onUserClickedListener;
    public void setOnUserClicked(OnUserClickedListener onUserClickedListener) {
        this.onUserClickedListener = onUserClickedListener;
    }

    public interface OnUserLongClickedListener {
        void OnUserLongClicked(User user);
    }
    private OnUserLongClickedListener onUserLongClickedListener;
    public void setOnUserLongClicked(OnUserLongClickedListener onUserLongClickedListener) {
        this.onUserLongClickedListener = onUserLongClickedListener;
    }
}
