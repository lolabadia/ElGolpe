package edu.upc.dsa.elgolpe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.upc.dsa.elgolpe.botiga.BotigaActivity;
import edu.upc.dsa.elgolpe.botiga.InventariActivity;

/**
 * Pantalla després del login: mostra l'usuari i les monedes i porta a la botiga i a l'inventari.
 */
public class MainActivity extends AppCompatActivity {

    private SessionManager sessio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessio = new SessionManager(this);
        if (!sessio.haySesion()) {
            tornarAlLogin();
            return;
        }

        setContentView(R.layout.activity_main);

        Button btnBotiga = findViewById(R.id.btnBotiga);
        Button btnInventari = findViewById(R.id.btnInventari);
        btnBotiga.setOnClickListener(v -> startActivity(new Intent(this, BotigaActivity.class)));
        btnInventari.setOnClickListener(v -> startActivity(new Intent(this, InventariActivity.class)));

        Button btnTancarSessio = findViewById(R.id.btnTancarSessio);
        btnTancarSessio.setOnClickListener(v -> {
            sessio.cerrarSesion();
            tornarAlLogin();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!sessio.haySesion()) return;
        // Al tornar d'altres pantalles (p. ex. després d'una compra) es refresquen les monedes
        TextView tvSalutacio = findViewById(R.id.tvSalutacio);
        TextView tvMonedes = findViewById(R.id.tvMonedes);
        tvSalutacio.setText(getString(R.string.main_salutacio, sessio.getNombre()));
        tvMonedes.setText(getString(R.string.main_monedes, sessio.getMonedas()));
    }

    private void tornarAlLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
