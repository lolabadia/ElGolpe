package edu.upc.dsa.domain.models;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String password;
    private int monedas;

    public Usuario() {
    }

    public Usuario(int id, String nombre, String email, String password, int monedas) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.monedas = monedas;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMonedas() {
        return monedas;
    }

    public void setMonedas(int monedas) {
        this.monedas = monedas;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + "'" +
                ", email='" + email + "'" +
                ", password='" + password + "'" +
                ", monedas=" + monedas +
                "}";
    }
}
