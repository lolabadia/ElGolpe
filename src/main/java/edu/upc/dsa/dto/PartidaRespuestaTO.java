package edu.upc.dsa.dto;

public class PartidaRespuestaTO {
    private int partidaId;
    private boolean exito;
    private int monedasGanadas;
    private int monedasTotales;

    public PartidaRespuestaTO() {
    }

    public PartidaRespuestaTO(int partidaId, boolean exito, int monedasGanadas, int monedasTotales) {
        this.partidaId = partidaId;
        this.exito = exito;
        this.monedasGanadas = monedasGanadas;
        this.monedasTotales = monedasTotales;
    }

    public int getPartidaId() {
        return partidaId;
    }

    public void setPartidaId(int partidaId) {
        this.partidaId = partidaId;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public int getMonedasGanadas() {
        return monedasGanadas;
    }

    public void setMonedasGanadas(int monedasGanadas) {
        this.monedasGanadas = monedasGanadas;
    }

    public int getMonedasTotales() {
        return monedasTotales;
    }

    public void setMonedasTotales(int monedasTotales) {
        this.monedasTotales = monedasTotales;
    }
}