package com.example.contactos;

public class Contacto {

    String idPush;
    String id;
    String nombre;
    String telefono;

    public Contacto(String id, String idPush,String nombre, String telefono) {
        this.id = id;
        this.idPush = idPush;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Contacto() {
    }

    public String getIdPush() {
        return idPush;
    }

    public void setIdPush(String idPush) {
        this.idPush = idPush;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
