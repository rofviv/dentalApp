package com.example.rofviv.dentalapp;


public class Datos {

    private String id;
    private String doctor;
    private String fechaHora;
    private String estado;
    private String boton;
    private int imagen;

    public Datos(String id, String doctor, String fechaHora, String estado, String boton, int imagen) {
        this.id = id;
        this.doctor = doctor;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.boton = boton;
        this.imagen = imagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getBoton() {
        return boton;
    }

    public void setBoton(String boton) {
        this.boton = boton;
    }
}
