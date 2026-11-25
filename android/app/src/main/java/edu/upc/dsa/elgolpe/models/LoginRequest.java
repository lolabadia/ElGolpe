package edu.upc.dsa.elgolpe.models;

/** Cos de POST /usuarios/login (mateixos camps que LoginTO del servidor). */
public class LoginRequest {
    private final String nombre;
    private final String password;

    public LoginRequest(String nombre, String password) {
        this.nombre = nombre;
        this.password = password;
    }
}
