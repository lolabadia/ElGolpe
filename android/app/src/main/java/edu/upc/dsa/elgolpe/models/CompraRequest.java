package edu.upc.dsa.elgolpe.models;

/** Cos de POST /usuarios/{id}/compras (mateixos camps que CompraTO del servidor). */
public class CompraRequest {
    private final int objetoId;
    private final int cantidad;

    public CompraRequest(int objetoId, int cantidad) {
        this.objetoId = objetoId;
        this.cantidad = cantidad;
    }
}
