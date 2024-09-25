package com.example.petpal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView registerTextView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        emailEditText = findViewById(R.id.email_input);
        passwordEditText = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);
        registerTextView = findViewById(R.id.register_link);

        loginButton.setOnClickListener(v -> {
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

            progressBar.setVisibility(View.VISIBLE);

            new Thread(() -> {
                boolean loginSuccessful = dbHelper.verifyUserCredentials(email, password);

                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);

                    if (loginSuccessful) {
                        Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, MenuPrincipal.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Registro.class);
            startActivity(intent);
        });
    }
}
