package com.example.petpal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    private EditText editTextOwnerName, editTextEmail, editTextPassword, editTextConfirmPassword,
            editTextPetName, editTextPetBirthdate, editTextPetBreed;
    private RadioButton radioPetDog, radioPetCat, radioPetOther;
    private CheckBox checkboxTerms;
    private ProgressBar progressBar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicialización de vistas
        editTextOwnerName = findViewById(R.id.editTextOwnerName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextPetName = findViewById(R.id.editTextPetName);
        editTextPetBirthdate = findViewById(R.id.editTextPetBirthdate);
        editTextPetBreed = findViewById(R.id.editTextPetBreed);
        radioPetDog = findViewById(R.id.radioPetDog);
        radioPetCat = findViewById(R.id.radioPetCat);
        radioPetOther = findViewById(R.id.radioPetOther);
        checkboxTerms = findViewById(R.id.checkbox_terms);
        progressBar = findViewById(R.id.progressBar);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        // Inicialización de Firebase Firestore
        db = FirebaseFirestore.getInstance();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si los campos están vacíos
                if (areFieldsValid()) {
                    registerUser();
                } else {
                    Toast.makeText(Registro.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para verificar que los campos están correctamente llenos
    private boolean areFieldsValid() {
        return !editTextOwnerName.getText().toString().isEmpty() &&
                !editTextEmail.getText().toString().isEmpty() &&
                !editTextPassword.getText().toString().isEmpty() &&
                !editTextConfirmPassword.getText().toString().isEmpty() &&
                !editTextPetName.getText().toString().isEmpty() &&
                !editTextPetBirthdate.getText().toString().isEmpty() &&
                !editTextPetBreed.getText().toString().isEmpty() &&
                checkboxTerms.isChecked() &&
                editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString());
    }

    // Método para registrar el usuario en Firestore
    private void registerUser() {
        progressBar.setVisibility(View.VISIBLE); // Mostrar el ProgressBar

        String ownerName = editTextOwnerName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String petName = editTextPetName.getText().toString();
        String petBirthdate = editTextPetBirthdate.getText().toString();
        String petBreed = editTextPetBreed.getText().toString();
        String petType = getPetType();

        // Crear un mapa con los datos a enviar a Firestore
        Map<String, Object> user = new HashMap<>();
        user.put("ownerName", ownerName);
        user.put("email", email);
        user.put("password", password);
        user.put("petName", petName);
        user.put("petBirthdate", petBirthdate);
        user.put("petBreed", petBreed);
        user.put("petType", petType);
        user.put("profileImage", ""); // Inicialmente sin foto de perfil

        // Guardar los datos en Firestore
        db.collection("usuarios")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    progressBar.setVisibility(View.GONE); // Ocultar el ProgressBar
                    Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    clearFields(); // Limpiar los campos después del registro

                    // Redirigir al usuario a la actividad de Login
                    Intent intent = new Intent(Registro.this, Login.class);
                    startActivity(intent);
                    finish(); // Finaliza la actividad actual para que no quede en el historial
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE); // Ocultar el ProgressBar
                    Toast.makeText(Registro.this, "Error al registrar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Método para obtener el tipo de mascota seleccionado
    private String getPetType() {
        if (radioPetDog.isChecked()) {
            return "Perro";
        } else if (radioPetCat.isChecked()) {
            return "Gato";
        } else {
            return "Otros";
        }
    }

    // Método para limpiar los campos
    private void clearFields() {
        editTextOwnerName.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
        editTextConfirmPassword.setText("");
        editTextPetName.setText("");
        editTextPetBirthdate.setText("");
        editTextPetBreed.setText("");
        checkboxTerms.setChecked(false);
        radioPetDog.setChecked(false);
        radioPetCat.setChecked(false);
        radioPetOther.setChecked(false);
    }
}
