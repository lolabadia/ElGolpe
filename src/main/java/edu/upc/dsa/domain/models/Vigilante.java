package edu.upc.dsa.domain.models;

public class Vigilante {
    private int id;
    private int museoId;
    private TipoVigilante tipo;
    private int x;
    private int y;
    private String direccion;
    private String ruta;

    public Vigilante() {
    }

    public Vigilante(int id, int museoId, TipoVigilante tipo, int x, int y, String direccion, String ruta) {
        this.id = id;
        this.museoId = museoId;
        this.tipo = tipo;
        this.x = x;
        this.y = y;
        this.direccion = direccion;
        this.ruta = ruta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMuseoId() {
        return museoId;
    }

    public void setMuseoId(int museoId) {
        this.museoId = museoId;
    }

    public TipoVigilante getTipo() {
        return tipo;
    }

    public void setTipo(TipoVigilante tipo) {
        this.tipo = tipo;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public String toString() {
        return "Vigilante{" +
                "id=" + id +
                ", museoId=" + museoId +
                ", tipo=" + tipo +
                ", x=" + x +
                ", y=" + y +
                ", direccion='" + direccion + "'" +
                ", ruta='" + ruta + "'" +
                "}";
    }
}
