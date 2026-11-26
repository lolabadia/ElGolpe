package edu.upc.dsa.elgolpe.models;

/**
 * Cos de POST /usuarios/{id}/devoluciones.
 * ATENCIÓ: aquesta ruta encara no existeix al servidor (vegeu el README).
 */
public class DevolucionRequest {
    private final int objetoId;
    private final int cantidad;

    public DevolucionRequest(int objetoId, int cantidad) {
        this.objetoId = objetoId;
        this.cantidad = cantidad;
    }
}
