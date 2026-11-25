package edu.upc.dsa.dto;

public class InventarioTO {
    private int objetoId;
    private String nombre;
    private int cantidad;

    public InventarioTO() {
    }

    public InventarioTO(int objetoId, String nombre, int cantidad) {
        this.objetoId = objetoId;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public int getObjetoId() {
        return objetoId;
    }

    public void setObjetoId(int objetoId) {
        this.objetoId = objetoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}