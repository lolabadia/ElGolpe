package edu.upc.dsa.domain.models;

public class Compra {
    private int id;
    private int usuarioId;
    private int objetoId;
    private int cantidad;
    private int precioTotal;
    private String fecha;

    public Compra() {
    }

    public Compra(int id, int usuarioId, int objetoId, int cantidad, int precioTotal, String fecha) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.objetoId = objetoId;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
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

    @Override
    public String toString() {
        return "Compra{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", objetoId=" + objetoId +
                ", cantidad=" + cantidad +
                ", precioTotal=" + precioTotal +
                ", fecha='" + fecha + "'" +
                "}";
    }
}
