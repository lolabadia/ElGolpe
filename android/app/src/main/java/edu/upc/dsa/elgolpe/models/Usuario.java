package edu.upc.dsa.elgolpe.models;

/** Resposta del registre i del login (mateixos camps que UsuarioTO del servidor). */
public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private int monedas;

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public int getMonedas() {
        return monedas;
    }
}
