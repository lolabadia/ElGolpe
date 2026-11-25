package edu.upc.dsa.dto;

public class RankingTO {
    private int posicion;
    private String nombre;
    private int museosCompletados;
    private int tiempoTotal;

    public RankingTO() {
    }

    public RankingTO(int posicion, String nombre, int museosCompletados, int tiempoTotal) {
        this.posicion = posicion;
        this.nombre = nombre;
        this.museosCompletados = museosCompletados;
        this.tiempoTotal = tiempoTotal;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getMuseosCompletados() {
        return museosCompletados;
    }

    public void setMuseosCompletados(int museosCompletados) {
        this.museosCompletados = museosCompletados;
    }

    public int getTiempoTotal() {
        return tiempoTotal;
    }

    public void setTiempoTotal(int tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }
}