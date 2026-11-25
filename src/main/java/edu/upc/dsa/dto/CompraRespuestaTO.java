package edu.upc.dsa.dto;

import edu.upc.dsa.domain.models.Compra;

public class CompraRespuestaTO {
    private int id;
    private int usuarioId;
    private int objetoId;
    private int cantidad;
    private int precioTotal;
    private String fecha;
    private int monedasRestantes;

    public CompraRespuestaTO() {
    }

    public CompraRespuestaTO(Compra c, int monedasRestantes) {
        this.id = c.getId();
        this.usuarioId = c.getUsuarioId();
        this.objetoId = c.getObjetoId();
        this.cantidad = c.getCantidad();
        this.precioTotal = c.getPrecioTotal();
        this.fecha = c.getFecha();
        this.monedasRestantes = monedasRestantes;
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

    public int getObjetoId() {
        return objetoId;
    }

    public void setObjetoId(int objetoId) {
        this.objetoId = objetoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(int precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getMonedasRestantes() {
        return monedasRestantes;
    }

    public void setMonedasRestantes(int monedasRestantes) {
        this.monedasRestantes = monedasRestantes;
    }
}