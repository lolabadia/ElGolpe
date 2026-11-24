package edu.upc.dsa.domain.models;

public class EntradaInventario {
    private int usuarioId;
    private int objetoId;
    private int cantidad;

    public EntradaInventario() {
    }

    public EntradaInventario(int usuarioId, int objetoId, int cantidad) {
        this.usuarioId = usuarioId;
        this.objetoId = objetoId;
        this.cantidad = cantidad;
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

    @Override
    public String toString() {
        return "EntradaInventario{" +
                "usuarioId=" + usuarioId +
                ", objetoId=" + objetoId +
                ", cantidad=" + cantidad +
                "}";
    }
}
