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
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar el DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Referencias a los campos de entrada y el botón
        emailEditText = findViewById(R.id.email_input);
        passwordEditText = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);
        registerTextView = findViewById(R.id.register_link);

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

            // Verificación de credenciales de administrador
            if (email.equals("admin@gmail.com") && password.equals("123")) {
                // Mostrar el ProgressBar
                progressBar.setVisibility(View.VISIBLE);

                // Ejecutar en un hilo separado para simular un tiempo de espera
                new Thread(() -> {
                    try {
                        Thread.sleep(3000); // Espera de 3 segundos

                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);  // Ocultar el ProgressBar
                            Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                            // Redirigir a la actividad UserListActivity si es el administrador
                            Intent intent = new Intent(Login.this, activity_user_list.class);
                            startActivity(intent);
                            finish();
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                // Lógica de inicio de sesión normal para otros usuarios
                progressBar.setVisibility(View.VISIBLE);

                // Verifica las credenciales en la base de datos
                new Thread(() -> {
                    boolean isValidUser = databaseHelper.verifyUserCredentials(email, password);

                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        if (isValidUser) {
                            Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                            // Redirigir al MenuPrincipal para otros usuarios
                            Intent intent = new Intent(Login.this, MenuPrincipal.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Mensaje de error si las credenciales son incorrectas
                            Toast.makeText(Login.this, "Credenciales incorrectas. Intenta nuevamente.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            }
        });

        // Acción al hacer clic en el TextView para registrarse
        registerTextView.setOnClickListener(v -> {
            // Ir a la actividad de registro
            Intent intent = new Intent(Login.this, Registro.class);
            startActivity(intent);
        });
    }
}
