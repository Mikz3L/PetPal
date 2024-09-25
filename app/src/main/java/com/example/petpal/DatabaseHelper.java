package com.example.petpal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PetPal.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT," // Asegúrate de que exista la columna `name`
                + "email TEXT UNIQUE," // Y `email` también
                + "password TEXT"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Método para registrar un usuario
    public boolean registerUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("email", email);
        values.put("password", password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Método para verificar las credenciales en el inicio de sesión
    public boolean verifyUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM " + TABLE_USERS + " WHERE email = ?", new String[]{email});

        boolean validCredentials = false;
        if (cursor.moveToFirst()) {
            String storedPassword = cursor.getString(0);
            validCredentials = storedPassword.equals(password);
        }
        cursor.close();
        db.close();
        return validCredentials;
    }

    // Método para obtener todos los usuarios
    public List<Usuarios> getAllUsers() {
        List<Usuarios> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String name = null; // Asigna un valor por defecto o null si no tienes un nombre

                // Creamos un nuevo objeto User y lo agregamos a la lista
                userList.add(new Usuarios(id, name, email));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }


    // Método para actualizar el email de un usuario
    public boolean updateUser(int id, String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", newEmail);

        int rowsAffected = db.update(TABLE_USERS, values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    // Método para eliminar un usuario
    public boolean deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_USERS, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0;
    }
}
