package edu.upc.dsa.dto;

public class CompraTO {
    private Integer objetoId;
    private int cantidad;

    public CompraTO() {
    }

    public Integer getObjetoId() {
        return objetoId;
    }

    public void setObjetoId(Integer objetoId) {
        this.objetoId = objetoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}