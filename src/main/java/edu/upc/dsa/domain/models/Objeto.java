package edu.upc.dsa.domain.models;

public class Objeto {
    private int id;
    private String nombre;
    private String descripcion;
    private int precio;
    private String efecto;

    public Objeto() {
    }

    public Objeto(int id, String nombre, String descripcion, int precio, String efecto) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.efecto = efecto;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getEfecto() {
        return efecto;
    }

    public void setEfecto(String efecto) {
        this.efecto = efecto;
    }

    @Override
    public String toString() {
        return "Objeto{" +
                "id=" + id +
                ", nombre='" + nombre + "'" +
                ", descripcion='" + descripcion + "'" +
                ", precio=" + precio +
                ", efecto='" + efecto + "'" +
                "}";
    }
}
