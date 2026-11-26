package edu.upc.dsa.elgolpe.api;

import java.util.List;

import edu.upc.dsa.elgolpe.models.CompraRequest;
import edu.upc.dsa.elgolpe.models.CompraRespuesta;
import edu.upc.dsa.elgolpe.models.DevolucionRequest;
import edu.upc.dsa.elgolpe.models.DevolucionRespuesta;
import edu.upc.dsa.elgolpe.models.InventarioItem;
import edu.upc.dsa.elgolpe.models.LoginRequest;
import edu.upc.dsa.elgolpe.models.Objeto;
import edu.upc.dsa.elgolpe.models.RegistroRequest;
import edu.upc.dsa.elgolpe.models.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Rutes de l'API d'El Golpe (relatives a ApiClient.BASE_URL).
 */
public interface ElGolpeApi {

    // ---------- Usuaris ----------

    /** 201 usuari creat · 400 falta algun camp · 409 el nom ja existeix */
    @POST("usuarios/registro")
    Call<Usuario> registro(@Body RegistroRequest body);

    /** 200 login correcte · 400 falta el nom o la contrasenya · 401 credencials incorrectes */
    @POST("usuarios/login")
    Call<Usuario> login(@Body LoginRequest body);

    // ---------- Botiga ----------

    /** 200 llista d'objectes (pot ser buida) */
    @GET("tienda/objetos")
    Call<List<Objeto>> getObjetos();

    /** 201 compra feta · 400 quantitat no vàlida · 402 monedes insuficients · 404 no existeix l'usuari o l'objecte */
    @POST("usuarios/{id}/compras")
    Call<CompraRespuesta> comprar(@Path("id") int usuarioId, @Body CompraRequest body);

    /** 200 inventari (llista buida si no té res) · 404 no existeix l'usuari */
    @GET("usuarios/{id}/inventario")
    Call<List<InventarioItem>> getInventario(@Path("id") int usuarioId);

    /**
     * 201 devolució feta · 400 quantitat no vàlida · 404 no existeix l'usuari o l'objecte
     * · 409 no té prou unitats d'aquest objecte.
     * ATENCIÓ: aquesta ruta encara s'ha d'implementar al servidor.
     */
    @POST("usuarios/{id}/devoluciones")
    Call<DevolucionRespuesta> retornar(@Path("id") int usuarioId, @Body DevolucionRequest body);
}
