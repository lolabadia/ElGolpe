package edu.upc.dsa.elgolpe.models;

/** Resposta 201 de POST /usuarios/{id}/devoluciones (format proposat, vegeu el README). */
public class DevolucionRespuesta {
    private int objetoId;
    private int cantidad;
    private int monedasDevueltas;
    private int monedasRestantes;

    public int getObjetoId() {
        return objetoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getMonedasDevueltas() {
        return monedasDevueltas;
    }

    public int getMonedasRestantes() {
        return monedasRestantes;
    }
}
