package edu.upc.dsa.dto;

import edu.upc.dsa.domain.models.Usuario;

public class UsuarioTO {
    private int id;
    private String nombre;
    private String email;
    private int monedas;

    public UsuarioTO() {
    }

    public UsuarioTO(Usuario u) {
        this.id = u.getId();
        this.nombre = u.getNombre();
        this.email = u.getEmail();
        this.monedas = u.getMonedas();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMonedas() {
        return monedas;
    }

    public void setMonedas(int monedas) {
        this.monedas = monedas;
    }
}