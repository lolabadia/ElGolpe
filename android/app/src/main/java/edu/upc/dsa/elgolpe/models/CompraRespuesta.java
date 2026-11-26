package edu.upc.dsa.elgolpe.models;

/** Resposta 201 de POST /usuarios/{id}/compras (mateixos camps que CompraRespuestaTO). */
public class CompraRespuesta {
    private int id;
    private int usuarioId;
    private int objetoId;
    private int cantidad;
    private int precioTotal;
    private String fecha;
    private int monedasRestantes;

    public int getObjetoId() {
        return objetoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getPrecioTotal() {
        return precioTotal;
    }

    public int getMonedasRestantes() {
        return monedasRestantes;
    }
}
