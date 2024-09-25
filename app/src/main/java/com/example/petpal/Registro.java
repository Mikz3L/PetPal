package com.example.petpal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Registro extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button registerButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        dbHelper = new DatabaseHelper(this);

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        registerButton = findViewById(R.id.buttonRegister);

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("El correo es obligatorio");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("La contraseña es obligatoria");
                return;
            }

            boolean isRegistered = dbHelper.registerUser(email, password);
            if (isRegistered) {
                Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Registro.this, Login.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Registro.this, "Error en el registro. Intenta con otro correo.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
