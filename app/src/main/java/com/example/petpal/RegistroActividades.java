package com.example.petpal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistroActividades extends AppCompatActivity {

    private EditText editTextActividad;
    private Button btnGuardar;
    private ProgressBar progressBar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_actividades);

        // Inicialización de vistas
        editTextActividad = findViewById(R.id.actividadInput);
        btnGuardar = findViewById(R.id.btnRegistrarActividad);
        progressBar = findViewById(R.id.progressBar);

        // Inicialización de Firebase Firestore
        db = FirebaseFirestore.getInstance();

        btnGuardar.setOnClickListener(v -> {
            // Verificar que el campo de actividad no esté vacío
            String actividad = editTextActividad.getText().toString().trim();

            if (actividad.isEmpty()) {
                Toast.makeText(RegistroActividades.this, "Por favor ingrese una actividad", Toast.LENGTH_SHORT).show();
            } else {
                saveActivity(actividad);
            }
        });
    }

    // Método para guardar la actividad en Firestore
    private void saveActivity(String actividad) {
        progressBar.setVisibility(View.VISIBLE); // Mostrar el ProgressBar

        // Crear un mapa con los datos de la actividad
        Map<String, Object> activity = new HashMap<>();
        activity.put("nombre", actividad);

        // Guardar los datos en Firestore
        db.collection("activities")
                .add(activity)
                .addOnSuccessListener(documentReference -> {
                    progressBar.setVisibility(View.GONE); // Ocultar el ProgressBar
                    Toast.makeText(RegistroActividades.this, "Actividad registrada con éxito", Toast.LENGTH_SHORT).show();
                    clearFields(); // Limpiar los campos después de registrar
                    navigateToProfile(); // Navegar a la pantalla de perfil (o la pantalla que desees)
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE); // Ocultar el ProgressBar
                    Toast.makeText(RegistroActividades.this, "Error al registrar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Método para limpiar el campo de actividad
    private void clearFields() {
        editTextActividad.setText(""); // Limpiar el campo de actividad
    }

    // Método para navegar a la pantalla de perfil (o cualquier otra pantalla que desees)
    private void navigateToProfile() {
        Intent intent = new Intent(RegistroActividades.this, Perfil.class); // Cambia a la actividad deseada
        startActivity(intent);
    }
}
