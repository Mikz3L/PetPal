package com.example.petpal;

import java.util.Objects;

public class Usuarios {
    private int id;
    private String name;
    private String email;

    public Usuarios(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Usuarios{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuarios usuarios = (Usuarios) obj;
        return id == usuarios.id && name.equals(usuarios.name) && email.equals(usuarios.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}
