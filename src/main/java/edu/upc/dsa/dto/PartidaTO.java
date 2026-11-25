package edu.upc.dsa.dto;

public class PartidaTO {
    private Integer museoId;
    private Boolean exito;
    private int tiempoSegundos;
    private ObjetoUsadoTO[] objetosUsados;

    public PartidaTO() {
    }

    public Integer getMuseoId() {
        return museoId;
    }

    public void setMuseoId(Integer museoId) {
        this.museoId = museoId;
    }

    public Boolean getExito() {
        return exito;
    }

    public void setExito(Boolean exito) {
        this.exito = exito;
    }

    public int getTiempoSegundos() {
        return tiempoSegundos;
    }

    public void setTiempoSegundos(int tiempoSegundos) {
        this.tiempoSegundos = tiempoSegundos;
    }

    public ObjetoUsadoTO[] getObjetosUsados() {
        return objetosUsados;
    }

    public void setObjetosUsados(ObjetoUsadoTO[] objetosUsados) {
        this.objetosUsados = objetosUsados;
    }
}