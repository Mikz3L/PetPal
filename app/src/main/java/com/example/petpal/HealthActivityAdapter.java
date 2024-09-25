package com.example.petpal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HealthActivityAdapter extends RecyclerView.Adapter<HealthActivityAdapter.ViewHolder> {

    private List<HealthActivity> healthActivityList;

    public HealthActivityAdapter(List<HealthActivity> healthActivityList) {
        this.healthActivityList = healthActivityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_health_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HealthActivity activity = healthActivityList.get(position);
        holder.actividadTextView.setText(activity.getActividad());
        holder.fechaTextView.setText(activity.getFecha());
    }

    @Override
    public int getItemCount() {
        return healthActivityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView actividadTextView;
        TextView fechaTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            actividadTextView = itemView.findViewById(R.id.text_actividad);
            fechaTextView = itemView.findViewById(R.id.text_fecha);
        }
    }
}
