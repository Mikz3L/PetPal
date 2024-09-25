package com.example.petpal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Perfil extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HealthActivityAdapter adapter;
    private List<HealthActivity> healthActivityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil); // Asegúrate de que el nombre del layout sea correcto

        // Referencia al botón de cerrar sesión
        ImageButton btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad MenuPrincipal
                Intent intent = new Intent(Perfil.this, MenuPrincipal.class);
                startActivity(intent);
                finish(); // Cierra la actividad actual
            }
        });

        // Configurar el RecyclerView para mostrar las actividades de salud
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Crear lista de actividades de salud
        healthActivityList = new ArrayList<>();
        healthActivityList.add(new HealthActivity("Visita al Veterinario", "10/01/2024"));
        healthActivityList.add(new HealthActivity("Vacunación", "15/02/2024"));
        healthActivityList.add(new HealthActivity("Control de Peso", "05/03/2024"));

        // Configurar el adaptador y asignarlo al RecyclerView
        adapter = new HealthActivityAdapter(healthActivityList);
        recyclerView.setAdapter(adapter);
    }
}
