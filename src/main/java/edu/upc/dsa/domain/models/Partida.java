package edu.upc.dsa.domain.models;

public class Partida {
    private int id;
    private int usuarioId;
    private int museoId;
    private boolean exito;
    private int tiempoSegundos;
    private int monedasGanadas;
    private String fecha;

    public Partida() {
    }

    public Partida(int id, int usuarioId, int museoId, boolean exito, int tiempoSegundos, int monedasGanadas, String fecha) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.museoId = museoId;
        this.exito = exito;
        this.tiempoSegundos = tiempoSegundos;
        this.monedasGanadas = monedasGanadas;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getMuseoId() {
        return museoId;
    }

    public void setMuseoId(int museoId) {
        this.museoId = museoId;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public int getTiempoSegundos() {
        return tiempoSegundos;
    }

    public void setTiempoSegundos(int tiempoSegundos) {
        this.tiempoSegundos = tiempoSegundos;
    }

    public int getMonedasGanadas() {
        return monedasGanadas;
    }

    public void setMonedasGanadas(int monedasGanadas) {
        this.monedasGanadas = monedasGanadas;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Partida{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", museoId=" + museoId +
                ", exito=" + exito +
                ", tiempoSegundos=" + tiempoSegundos +
                ", monedasGanadas=" + monedasGanadas +
                ", fecha='" + fecha + "'" +
                "}";
    }
}
