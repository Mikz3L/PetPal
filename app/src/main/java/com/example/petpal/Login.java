package com.example.petpal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

public class Login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView registerTextView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.email_input);
        passwordEditText = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);
        registerTextView = findViewById(R.id.register_link);

        registerTextView.setOnClickListener(v -> {
            // Redirige a la actividad de Registro
            Intent intent = new Intent(Login.this, Registro.class);
            startActivity(intent);
        });

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

            db.collection("usuarios")
                    .whereEqualTo("email", email)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                            String petName = documentSnapshot.getString("petName");
                            String ownerName = documentSnapshot.getString("ownerName");
                            String ownerEmail = documentSnapshot.getString("email");
                            String profileImageUrl = documentSnapshot.getString("profileImage");

                            // Guardar los datos del usuario, incluida la imagen de perfil, en SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("petName", petName);
                            editor.putString("ownerName", ownerName);
                            editor.putString("ownerEmail", ownerEmail);
                            editor.putString("profileImage", profileImageUrl);  // Guardar la URL de la imagen
                            editor.apply();

                            Toast.makeText(Login.this, "Bienvenido " + ownerName, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Login.this, MenuPrincipal.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Credenciales incorrectas. Intenta nuevamente.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
