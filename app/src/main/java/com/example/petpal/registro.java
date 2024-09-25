package com.example.petpal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class registro extends AppCompatActivity {

    private CheckBox termsCheckBox;
    private Button registerButton;
    private ProgressBar progressBar;  // Añadido ProgressBar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        // Configurar el padding para las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referenciar el CheckBox, el botón de registro y el ProgressBar
        termsCheckBox = findViewById(R.id.checkbox_terms);
        registerButton = findViewById(R.id.buttonRegister);
        progressBar = findViewById(R.id.progressBar);  // Inicializar el ProgressBar

        // Deshabilitar el botón de registro si no se aceptan los términos
        registerButton.setOnClickListener(v -> {
            if (termsCheckBox.isChecked()) {
                // Mostrar el ProgressBar
                progressBar.setVisibility(View.VISIBLE);

                // Simular un registro exitoso (puedes reemplazar esto con la lógica real de registro)
                new Thread(() -> {
                    try {
                        // Simula un tiempo de espera para el registro
                        Thread.sleep(2000); // Simulación de 2 segundos

                        // Una vez registrado, actualizar la interfaz en el hilo principal
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);  // Ocultar el ProgressBar
                            Toast.makeText(registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            // Aquí puedes iniciar la lógica para redirigir al login
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                Toast.makeText(registro.this, "Debes aceptar los términos y condiciones", Toast.LENGTH_LONG).show();
            }
        });
    }
}
