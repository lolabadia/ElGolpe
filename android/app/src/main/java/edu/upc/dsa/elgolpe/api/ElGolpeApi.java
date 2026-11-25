package edu.upc.dsa.elgolpe.api;

import edu.upc.dsa.elgolpe.models.LoginRequest;
import edu.upc.dsa.elgolpe.models.RegistroRequest;
import edu.upc.dsa.elgolpe.models.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Rutes de l'API d'El Golpe (relatives a ApiClient.BASE_URL).
 * Les rutes de la botiga i les partides s'afegiran aquí mateix.
 */
public interface ElGolpeApi {

    /** 201 usuari creat · 400 falta algun camp · 409 el nom ja existeix */
    @POST("usuarios/registro")
    Call<Usuario> registro(@Body RegistroRequest body);

    /** 200 login correcte · 400 falta el nom o la contrasenya · 401 credencials incorrectes */
    @POST("usuarios/login")
    Call<Usuario> login(@Body LoginRequest body);
}
