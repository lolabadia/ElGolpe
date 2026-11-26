package edu.upc.dsa.elgolpe.models;

/** Una línia de GET /usuarios/{id}/inventario (mateixos camps que InventarioTO). */
public class InventarioItem {
    private int objetoId;
    private String nombre;
    private int cantidad;

    public int getObjetoId() {
        return objetoId;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }
}
