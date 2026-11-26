package edu.upc.dsa.elgolpe.models;

/** Objecte del catàleg de la botiga (GET /tienda/objetos). */
public class Objeto {
    private int id;
    private String nombre;
    private String descripcion;
    private int precio;
    private String efecto;

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    public String getEfecto() {
        return efecto;
    }
}
