package edu.upc.dsa.elgolpe.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Crea una única instància de Retrofit per a tota l'app.
 *
 * 10.0.2.2 és l'adreça amb què l'emulador d'Android veu el "localhost" de l'ordinador.
 * Si proveu amb un mòbil real, canvieu-la per la IP de l'ordinador a la xarxa
 * (i al Main.java del servidor, BASE_URI ha de ser http://0.0.0.0:8080/dsaApp/).
 */
public class ApiClient {

    public static final String BASE_URL = "http://10.0.2.2:8080/dsaApp/elgolpe/";

    private static ElGolpeApi api;

    private ApiClient() {
    }

    public static synchronized ElGolpeApi getApi() {
        if (api == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            api = retrofit.create(ElGolpeApi.class);
        }
        return api;
    }
}
