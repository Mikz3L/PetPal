package com.example.petpal;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Geolocalizacion extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private MapView mapView;
    private GoogleMap googleMap;
    private Marker tapMarker; // Marcador que se moverá cuando el usuario haga tap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_geolocalizacion);

        // Configurar padding para el layout principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar el MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this); // Obtener el mapa de forma asíncrona
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Verificar si los permisos de ubicación han sido concedidos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permisos si no se han concedido
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            // Si los permisos están concedidos, habilitar la ubicación
            enableMyLocation();
        }

        // Configurar el listener para detectar toques en el mapa
        googleMap.setOnMapClickListener(latLng -> {
            // Si ya hay un marcador en el mapa, lo movemos al nuevo punto
            if (tapMarker != null) {
                tapMarker.setPosition(latLng);
            } else {
                // Si no hay marcador, creamos uno nuevo
                tapMarker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Marcador"));
            }
            // La cámara NO se mueve a la nueva posición del marcador
        });
    }

    // Método para habilitar la ubicación en el mapa
    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true); // Habilita la capa de ubicación

            googleMap.setOnMyLocationButtonClickListener(() -> {
                Location location = googleMap.getMyLocation(); // Obtiene la ubicación actual
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15)); // Mueve la cámara a la ubicación del usuario
                }
                return false;
            });
        } else {
            // Mostrar un mensaje o realizar una acción si no se tienen los permisos
            // Por ejemplo, solicitar permisos nuevamente
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    // Ciclo de vida del MapView
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    // Manejo de los resultados de los permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation(); // Habilitar la ubicación si se concede el permiso
            } else {
                // Manejo cuando el usuario no concede el permiso
                // Podrías mostrar un mensaje explicativo
            }
        }
    }
}
