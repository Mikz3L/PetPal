package com.example.petpal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class activity_user_list extends AppCompatActivity {

    private static final int REQUEST_CODE_EDIT_USER = 1; // Define un código de solicitud
    private RecyclerView recyclerView;
    private UsuariosAdapter usuariosAdapter;
    private DatabaseHelper dbHelper;
    private List<Usuarios> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list); // Asegúrate de que el layout sea el correcto

        // Inicializa la base de datos
        dbHelper = new DatabaseHelper(this);

        // Obtén la lista de usuarios de la base de datos
        loadUsers(); // Carga los usuarios al iniciar

        // Configura el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        // Verifica si recyclerView es null
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            Log.e("UsuariosActivity", "RecyclerView no encontrado.");
        }

        // Configura el adaptador con la lista de usuarios
        if (recyclerView != null) {
            usuariosAdapter = new UsuariosAdapter(this, userList);
            recyclerView.setAdapter(usuariosAdapter);
        }
    }

    // Método para cargar la lista de usuarios
    private void loadUsers() {
        userList = dbHelper.getAllUsers();
        if (userList == null) {
            userList = new ArrayList<>(); // Inicializa la lista vacía si es null
        }
        // Si el adaptador ya fue inicializado, actualiza la lista de usuarios
        if (usuariosAdapter != null) {
            usuariosAdapter.updateUsers(userList);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_USER && resultCode == RESULT_OK) {
            // Vuelve a cargar la lista de usuarios
            loadUsers();
        }
    }
}
