package edu.upc.dsa.elgolpe;

import android.content.Context;
import android.content.SharedPreferences;

import edu.upc.dsa.elgolpe.models.Usuario;

/**
 * Guarda l'usuari que ha iniciat sessió a SharedPreferences.
 * Les altres pantalles (botiga, inventari...) agafen l'id d'aquí amb getUsuarioId().
 */
public class SessionManager {

    private static final String PREFS = "elgolpe_sessio";
    private static final String KEY_ID = "usuarioId";
    private static final String KEY_NOM = "nombre";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MONEDES = "monedas";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void guardarUsuario(Usuario u) {
        prefs.edit()
                .putInt(KEY_ID, u.getId())
                .putString(KEY_NOM, u.getNombre())
                .putString(KEY_EMAIL, u.getEmail())
                .putInt(KEY_MONEDES, u.getMonedas())
                .apply();
    }

    public boolean haySesion() {
        return prefs.contains(KEY_ID);
    }

    /** Retorna -1 si no hi ha ningú amb la sessió iniciada. */
    public int getUsuarioId() {
        return prefs.getInt(KEY_ID, -1);
    }

    public String getNombre() {
        return prefs.getString(KEY_NOM, "");
    }

    public int getMonedas() {
        return prefs.getInt(KEY_MONEDES, 0);
    }

    /** Per quan una compra o una partida canviï les monedes. */
    public void setMonedas(int monedas) {
        prefs.edit().putInt(KEY_MONEDES, monedas).apply();
    }

    public void cerrarSesion() {
        prefs.edit().clear().apply();
    }
}
