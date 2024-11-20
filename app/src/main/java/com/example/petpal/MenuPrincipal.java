package com.example.petpal;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MenuPrincipal extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TextView messageSent;
    private RatingBar ratingBar;
    private ImageButton btnMenu;

    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        // Configuración del DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        btnMenu = findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));

        ImageButton btnSendMessage = findViewById(R.id.btn_send_message);
        messageSent = findViewById(R.id.message_sent);
        ratingBar = findViewById(R.id.rating_bar);

        btnSendMessage.setOnClickListener(v -> {
            ratingBar.setVisibility(View.GONE);
            messageSent.setVisibility(View.VISIBLE);

            new Handler().postDelayed(() -> messageSent.setVisibility(View.GONE), 2000);
        });

        // Verificar permisos al inicio
        checkPermissions();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_profile) {
                    // Recuperar datos del usuario y pasarlos al perfil
                    SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    String petName = preferences.getString("petName", "Usuario no definido");
                    String ownerEmail = preferences.getString("ownerEmail", "Email no definido");
                    String profileImageUri = preferences.getString("profileImage", null); // Recuperar URI de imagen

                    // Pasar los datos a la actividad Perfil
                    Intent intent = new Intent(MenuPrincipal.this, Perfil.class);
                    intent.putExtra("petName", petName);
                    intent.putExtra("ownerEmail", ownerEmail);
                    intent.putExtra("profileImageUri", profileImageUri); // Pasar URI de la imagen
                    startActivity(intent);
                }
                if (id == R.id.nav_location) {
                    // Verificar permiso de ubicación
                    if (ContextCompat.checkSelfPermission(MenuPrincipal.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(MenuPrincipal.this, Geolocalizacion.class);
                        startActivity(intent);
                    } else {
                        // Solicitar permiso de ubicación si no está concedido
                        ActivityCompat.requestPermissions(MenuPrincipal.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                    }
                }

                if (id == R.id.nav_community) {
                    Intent intent = new Intent(MenuPrincipal.this, Community.class);
                    startActivity(intent);
                }

                else if (id == R.id.nav_logout) {
                    cerrarSesion();
                }

                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });
    }

    private void checkPermissions() {
        // Verificar y solicitar permisos para notificaciones
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
        }
    }

    private void cerrarSesion() {
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // Limpiar datos del usuario
        editor.apply();

        // Redirigir a login
        Intent intent = new Intent(MenuPrincipal.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);  // Eliminar el stack de actividades anteriores
        startActivity(intent);
        finish();
    }

    // Manejo de permisos (cuando el usuario responde a la solicitud)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de ubicación concedido", Toast.LENGTH_SHORT).show();
                // Ahora puedes abrir la actividad de geolocalización
                Intent intent = new Intent(MenuPrincipal.this, Geolocalizacion.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de notificaciones concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}