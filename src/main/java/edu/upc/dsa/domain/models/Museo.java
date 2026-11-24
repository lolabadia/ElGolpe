package edu.upc.dsa.domain.models;

public class Museo {
    private int id;
    private String nombre;
    private int dificultad;
    private int ancho;
    private int alto;
    private String mapa;
    private int recompensa;
    private String nombreObra;

    public Museo() {
    }

    public Museo(int id, String nombre, int dificultad, int ancho, int alto, String mapa, int recompensa, String nombreObra) {
        this.id = id;
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.ancho = ancho;
        this.alto = alto;
        this.mapa = mapa;
        this.recompensa = recompensa;
        this.nombreObra = nombreObra;
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

    public int getDificultad() {
        return dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public String getMapa() {
        return mapa;
    }

    public void setMapa(String mapa) {
        this.mapa = mapa;
    }

    public int getRecompensa() {
        return recompensa;
    }

    public void setRecompensa(int recompensa) {
        this.recompensa = recompensa;
    }

    public String getNombreObra() {
        return nombreObra;
    }

    public void setNombreObra(String nombreObra) {
        this.nombreObra = nombreObra;
    }

    @Override
    public String toString() {
        return "Museo{" +
                "id=" + id +
                ", nombre='" + nombre + "'" +
                ", dificultad=" + dificultad +
                ", ancho=" + ancho +
                ", alto=" + alto +
                ", mapa='" + mapa + "'" +
                ", recompensa=" + recompensa +
                ", nombreObra='" + nombreObra + "'" +
                "}";
    }
}
