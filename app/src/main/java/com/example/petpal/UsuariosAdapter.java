package com.example.petpal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

        // Evento del botón Editar
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditUserActivity.class);
            intent.putExtra("Id", user.getId());
            intent.putExtra("Email", user.getEmail());
            intent.putExtra("OwnerName", user.getName());

            // Verificar si el contexto es una instancia de UsuariosActivity para evitar el ClassCastException
            if (context instanceof UsuariosActivity) {
                ((UsuariosActivity) context).startActivityForResult(intent, 1);
            } else {
                context.startActivity(intent);
            }
        });

        // Evento del botón Eliminar
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

    // Método para actualizar la lista de usuarios
    public void updateUsers(List<Usuarios> newUserList) {
        this.userList = newUserList;
        notifyDataSetChanged(); // Notifica que los datos han cambiado y debe actualizarse la vista
    }

    // Clase ViewHolder interna para el RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView emailTextView;
        TextView nameTextView;
        Button deleteButton;
        Button editButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.textViewEmail);
            nameTextView = itemView.findViewById(R.id.userName);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
            editButton = itemView.findViewById(R.id.buttonUpdate);
        }
    }
}
