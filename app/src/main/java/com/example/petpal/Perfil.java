package com.example.petpal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Perfil extends AppCompatActivity {

    private TextView tvPetName, tvOwnerEmail;
    private ImageView profileImage;
    private Spinner spinnerActivities;
    private SharedPreferences sharedPreferences;
    private static final int REQUEST_PERMISSION = 1;
    private ArrayList<String> activitiesList;
    private FirebaseFirestore firestore;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Inicializar vistas
        tvPetName = findViewById(R.id.txt_nombre_usuario);
        tvOwnerEmail = findViewById(R.id.txt_email_usuario);
        profileImage = findViewById(R.id.profileImage);
        spinnerActivities = findViewById(R.id.my_spinner);
        tableLayout = findViewById(R.id.tableLayout);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        activitiesList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();

        loadActivitiesFromFirestore();

        checkPermissions();

        // Obtener datos del Intent o SharedPreferences
        loadUserProfile();

        // Seleccionar imagen de perfil
        profileImage.setOnClickListener(v -> selectImage());

        // Logout
        ImageButton btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(Perfil.this, MenuPrincipal.class); // Cambia "MainActivity" por la clase de tu menú principal
            startActivity(intent);
        });


        // Redirigir al registro de actividades
        Button btnRedirect = findViewById(R.id.btn_registrar_actividad);
        btnRedirect.setOnClickListener(v -> {
            Intent redirectIntent = new Intent(Perfil.this, RegistroActividades.class);
            startActivity(redirectIntent);
        });

        // Registrar actividad
        Button btnAssignDate = findViewById(R.id.btn_asignar_fecha);
        btnAssignDate.setOnClickListener(v -> registerActivity());
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            }
        }
    }

    private void loadUserProfile() {
        String petName = sharedPreferences.getString("petName", "Nombre no disponible");
        String ownerEmail = sharedPreferences.getString("ownerEmail", "Email no disponible");
        String imageUriString = sharedPreferences.getString("profileImage", null);

        tvPetName.setText("Nombre de Mascota: " + petName);
        tvOwnerEmail.setText("Correo del dueño: " + ownerEmail);

        if (imageUriString != null && !imageUriString.isEmpty()) {
            Uri imageUri = Uri.parse(imageUriString);
            try {
                Bitmap bitmap = loadImage(imageUri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                profileImage.setImageResource(R.drawable.ic_add);
            }
        } else {
            profileImage.setImageResource(R.drawable.ic_add);
        }
    }

    private void loadActivitiesFromFirestore() {
        firestore.collection("activities")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot documents = task.getResult();
                        activitiesList.clear();

                        if (documents != null) {
                            for (QueryDocumentSnapshot document : documents) {
                                String activityName = document.getString("nombre");
                                if (activityName != null) {
                                    activitiesList.add(activityName);
                                }
                            }
                            updateSpinner();
                        }
                    } else {
                        Toast.makeText(this, "Error al cargar actividades", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, activitiesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActivities.setAdapter(adapter);
    }

    private void registerActivity() {
        String selectedActivity = spinnerActivities.getSelectedItem().toString();

        // Generar fecha y hora aleatorias
        Calendar calendar = Calendar.getInstance();
        int randomDays = (int) (Math.random() * 10) + 1; // Adelantar entre 1 y 10 días
        int randomHours = (int) (Math.random() * 24); // Hora aleatoria (0-23)
        int randomMinutes = (int) (Math.random() * 60); // Minutos aleatorios (0-59)

        calendar.add(Calendar.DAY_OF_MONTH, randomDays);
        calendar.add(Calendar.HOUR_OF_DAY, randomHours);
        calendar.add(Calendar.MINUTE, randomMinutes);

        // Formato de la fecha y hora, con salto de línea entre fecha y hora
        String randomDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
        String randomTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime());
        String randomDateTime = randomDate + "\n" + randomTime; // Fecha y hora en líneas separadas

        // Crear nueva fila para la tabla
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Celda de fecha y hora
        TextView dateTimeCell = new TextView(this);
        dateTimeCell.setText(randomDateTime);
        dateTimeCell.setPadding(8, 8, 8, 8);
        dateTimeCell.setGravity(Gravity.CENTER);

        // Celda de actividad
        TextView activityCell = new TextView(this);
        activityCell.setText(selectedActivity);
        activityCell.setPadding(8, 8, 8, 8);
        activityCell.setGravity(Gravity.CENTER);

        row.addView(dateTimeCell);
        row.addView(activityCell);

        tableLayout.addView(row);
        Toast.makeText(this, "Actividad registrada: " + selectedActivity + "\nFecha y hora: " + randomDateTime, Toast.LENGTH_SHORT).show();
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        resultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    try {
                        Bitmap bitmap = loadImage(uri);
                        profileImage.setImageBitmap(bitmap);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("profileImage", uri.toString());
                        editor.apply();
                    } catch (IOException e) {
                        Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private Bitmap loadImage(Uri uri) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri));
        } else {
            return android.provider.MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        }
    }
}
