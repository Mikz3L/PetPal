package com.example.petpal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.navigation.NavigationView;

public class MenuPrincipal extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TextView messageSent;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);  // Asegúrate de cargar el layout correcto

        // Configuración para ajustar el diseño a los bordes de la pantalla
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configuración del DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configura el botón de logout
        ImageButton btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        // Configura el botón de enviar mensaje
        ImageButton btnSendMessage = findViewById(R.id.btn_send_message); // Asegúrate de que este botón esté en tu layout
        messageSent = findViewById(R.id.message_sent);
        ratingBar = findViewById(R.id.rating_bar);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ocultar el RatingBar
                ratingBar.setVisibility(View.GONE);

                // Mostrar el mensaje
                messageSent.setVisibility(View.VISIBLE);

                // Configurar un temporizador para ocultar el mensaje después de 2 segundos
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        messageSent.setVisibility(View.GONE);
                    }
                }, 2000); // 2000 milisegundos = 2 segundos
            }
        });
    }

    private void cerrarSesion() {
        // Lógica para cerrar sesión
        SharedPreferences preferences = getSharedPreferences("Usuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("Nombre");
        editor.apply();
        Intent intent = new Intent(MenuPrincipal.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
