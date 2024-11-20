package com.example.petpal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
                + "owner_name TEXT," // Agregado: columna para el nombre del dueño
                + "email TEXT UNIQUE," // Columna para el email
                + "password TEXT" // Columna para la contraseña
                + ")";
        try {
            db.execSQL(CREATE_USERS_TABLE);
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error al crear la tabla de usuarios: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error al actualizar la base de datos: " + e.getMessage());
        }
    }

    public boolean registerUser(String ownerName, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("owner_name", ownerName);
        values.put("email", email);
        values.put("password", password);

        try {
            long result = db.insert(TABLE_USERS, null, values);
            return result != -1;
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error al registrar usuario: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public boolean verifyUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean validCredentials = false;

        try {
            cursor = db.rawQuery("SELECT password FROM " + TABLE_USERS + " WHERE email = ?", new String[]{email});
            if (cursor.moveToFirst()) {
                String storedPassword = cursor.getString(0);
                validCredentials = storedPassword.equals(password);
            }
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error al verificar credenciales: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return validCredentials;
    }

    public List<Usuarios> getAllUsers() {
        List<Usuarios> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String ownerName = cursor.getString(cursor.getColumnIndexOrThrow("owner_name"));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

                    Usuarios user = new Usuarios(id, email, ownerName);
                    userList.add(user);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error al obtener todos los usuarios: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return userList;
    }

    public boolean updateUser(int userId, String ownerName, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_USERS + " WHERE email = ?", new String[]{email});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            Log.e("DatabaseError", "El email ya está en uso.");
            return false;
        }

        if (cursor != null) {
            cursor.close();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("owner_name", ownerName);  // Usa el nombre correcto de la columna
        contentValues.put("email", email);           // Usa el nombre correcto de la columna

        int result = db.update(TABLE_USERS, contentValues, "id = ?", new String[]{String.valueOf(userId)});
        db.close();

        return result > 0;
    }




    public boolean deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int rowsDeleted = db.delete(TABLE_USERS, "id = ?", new String[]{String.valueOf(id)});
            return rowsDeleted > 0;
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error al eliminar usuario: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }
}
