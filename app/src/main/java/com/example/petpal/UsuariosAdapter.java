package com.example.petpal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.ViewHolder> {

    private List<Usuarios> userList;
    private DatabaseHelper dbHelper;
    private Context context;

    public UsuariosAdapter(Context context, List<Usuarios> userList) {
        this.context = context;
        this.userList = userList;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuarios user = userList.get(position);
        holder.emailTextView.setText(user.getEmail());
        holder.nameTextView.setText(user.getName());

        holder.deleteButton.setOnClickListener(v -> {
            dbHelper.deleteUser(user.getId());
            userList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, userList.size());
            Toast.makeText(context, "Deleted " + user.getEmail(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView emailTextView;
        TextView nameTextView;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.textViewEmail);
            nameTextView = itemView.findViewById(R.id.userName);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
