package com.example.petpal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditUserActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText ownerNameEditText;
    private Button saveButton;
    private DatabaseHelper databaseHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user); // Cambia esto a activity_edit_user

        emailEditText = findViewById(R.id.edit_email_input);  // ID corregido
        ownerNameEditText = findViewById(R.id.editTextOwnerName);
        saveButton = findViewById(R.id.save_button);
        databaseHelper = new DatabaseHelper(this);

        // Obtener datos del intent
        userId = getIntent().getIntExtra("Id", -1);
        String userEmail = getIntent().getStringExtra("Email");
        String userOwnerName = getIntent().getStringExtra("OwnerName");

        emailEditText.setText(userEmail);
        ownerNameEditText.setText(userOwnerName);

        saveButton.setOnClickListener(v -> {
            String newEmail = emailEditText.getText().toString().trim();
            String newOwnerName = ownerNameEditText.getText().toString().trim();

            if (!TextUtils.isEmpty(newEmail) && !TextUtils.isEmpty(newOwnerName)) {
                // Actualizar el usuario
                boolean isUpdated = databaseHelper.updateUser(userId, newOwnerName, newEmail);
                if (isUpdated) {
                    Toast.makeText(EditUserActivity.this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
                    finish(); // Regresar a la actividad anterior
                } else {
                    Toast.makeText(EditUserActivity.this, "Error al actualizar el usuario", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(EditUserActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
