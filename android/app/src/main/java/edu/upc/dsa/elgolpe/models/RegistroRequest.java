package edu.upc.dsa.elgolpe.models;

/** Cos de POST /usuarios/registro (mateixos camps que RegistroTO del servidor). */
public class RegistroRequest {
    private final String nombre;
    private final String email;
    private final String password;

    public RegistroRequest(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }
}
