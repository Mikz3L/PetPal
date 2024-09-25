package com.example.petpal;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UsuariosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UsuariosAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        dbHelper = new DatabaseHelper(this);
        List<Usuarios> userList = dbHelper.getAllUsers();

        if (userList.isEmpty()) {
            Toast.makeText(this, "No users found.", Toast.LENGTH_SHORT).show();
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsuariosAdapter(this, userList);
        recyclerView.setAdapter(adapter);
    }

}
