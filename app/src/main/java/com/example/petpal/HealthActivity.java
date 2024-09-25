package com.example.petpal;

public class HealthActivity {
    private String actividad;
    private String fecha;

    public HealthActivity(String actividad, String fecha) {
        this.actividad = actividad;
        this.fecha = fecha;
    }

    public String getActividad() {
        return actividad;
    }

    public String getFecha() {
        return fecha;
    }
}
