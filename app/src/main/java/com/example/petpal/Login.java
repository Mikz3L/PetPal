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
    private TextView registerTextView;  // Nuevo TextView para ir al registro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Referencias a los campos de entrada y el botón
        emailEditText = findViewById(R.id.email_input);
        passwordEditText = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);
        registerTextView = findViewById(R.id.register_link);  // Referencia al TextView de registro

        // Acción al hacer clic en el botón de iniciar sesión
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
                try {
                    Thread.sleep(3000); // Aumentar a 3 segundos (2 segundos de espera + 1 segundo adicional)

                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);  // Ocultar el ProgressBar

                        Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                        // Redirigir al MenuPrincipal
                        Intent intent = new Intent(Login.this, MenuPrincipal.class);
                        startActivity(intent);
                        finish();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        // Acción al hacer clic en el TextView para registrarse
        registerTextView.setOnClickListener(v -> {
            // Ir a la actividad de registro
            Intent intent = new Intent(Login.this, registro.class);  // registro es tu clase de actividad de registro
            startActivity(intent);
        });
    }
}
