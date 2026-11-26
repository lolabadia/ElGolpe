package edu.upc.dsa.elgolpe.botiga;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import edu.upc.dsa.elgolpe.R;
import edu.upc.dsa.elgolpe.SessionManager;
import edu.upc.dsa.elgolpe.api.ApiClient;
import edu.upc.dsa.elgolpe.models.CompraRequest;
import edu.upc.dsa.elgolpe.models.CompraRespuesta;
import edu.upc.dsa.elgolpe.models.Objeto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Botiga: llistat d'objectes (GET /tienda/objetos) i compra (POST /usuarios/{id}/compras).
 */
public class BotigaActivity extends AppCompatActivity {

    /** Màxim d'unitats que es poden comprar d'un cop, per no fer la roda infinita. */
    private static final int MAX_PER_COMPRA = 10;

    private SessionManager sessio;
    private ObjecteAdapter adapter;
    private TextView tvMonedes, tvMissatge;
    private ProgressBar progress;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botiga);
        setTitle(R.string.botiga_titol);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessio = new SessionManager(this);

        tvMonedes = findViewById(R.id.tvMonedes);
        tvMissatge = findViewById(R.id.tvMissatge);
        progress = findViewById(R.id.progress);
        recycler = findViewById(R.id.recycler);

        adapter = new ObjecteAdapter(this::demanarQuantitat);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        tvMissatge.setOnClickListener(v -> carregarObjectes()); // tocar per reintentar

        actualitzarMonedes();
        carregarObjectes();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void carregarObjectes() {
        progress.setVisibility(View.VISIBLE);
        tvMissatge.setVisibility(View.GONE);

        ApiClient.getApi().getObjetos().enqueue(new Callback<List<Objeto>>() {
            @Override
            public void onResponse(@NonNull Call<List<Objeto>> call, @NonNull Response<List<Objeto>> response) {
                progress.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setObjectes(response.body());
                    if (response.body().isEmpty()) mostrarMissatge(getString(R.string.botiga_buida));
                } else {
                    mostrarMissatge(getString(R.string.error_servidor, response.code())
                            + "\n" + getString(R.string.tocar_reintentar));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Objeto>> call, @NonNull Throwable t) {
                progress.setVisibility(View.GONE);
                mostrarMissatge(getString(R.string.error_connexio) + "\n" + getString(R.string.tocar_reintentar));
            }
        });
    }

    /** En tocar "Comprar": si no arriba ni per a una unitat avisa; si no, demana la quantitat. */
    private void demanarQuantitat(Objeto o) {
        int monedes = sessio.getMonedas();
        if (o.getPrecio() > monedes) {
            mostrarSenseMonedes(o.getPrecio(), monedes);
            return;
        }
        int maxim = o.getPrecio() > 0 ? Math.min(MAX_PER_COMPRA, monedes / o.getPrecio()) : MAX_PER_COMPRA;

        QuantitatDialog.mostrar(this,
                getString(R.string.botiga_comprar_titol, o.getNombre()),
                getString(R.string.botiga_comprar),
                maxim,
                q -> getString(R.string.botiga_total, q * o.getPrecio(), monedes - q * o.getPrecio()),
                q -> comprar(o, q));
    }

    private void comprar(Objeto o, int quantitat) {
        progress.setVisibility(View.VISIBLE);

        ApiClient.getApi().comprar(sessio.getUsuarioId(), new CompraRequest(o.getId(), quantitat))
                .enqueue(new Callback<CompraRespuesta>() {
                    @Override
                    public void onResponse(@NonNull Call<CompraRespuesta> call, @NonNull Response<CompraRespuesta> response) {
                        progress.setVisibility(View.GONE);
                        if (response.code() == 201 && response.body() != null) {
                            sessio.setMonedas(response.body().getMonedasRestantes());
                            actualitzarMonedes();
                            avisar(getString(R.string.botiga_compra_ok, quantitat, o.getNombre()));
                        } else if (response.code() == 402) {
                            mostrarSenseMonedes(quantitat * o.getPrecio(), sessio.getMonedas());
                        } else if (response.code() == 400) {
                            avisar(getString(R.string.error_quantitat));
                        } else if (response.code() == 404) {
                            avisar(getString(R.string.error_objecte_no_existeix));
                        } else {
                            avisar(getString(R.string.error_servidor, response.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CompraRespuesta> call, @NonNull Throwable t) {
                        progress.setVisibility(View.GONE);
                        avisar(getString(R.string.error_connexio));
                    }
                });
    }

    private void mostrarSenseMonedes(int necessaries, int tens) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.botiga_sense_monedes_titol)
                .setMessage(getString(R.string.botiga_sense_monedes, necessaries, tens))
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void actualitzarMonedes() {
        tvMonedes.setText(getString(R.string.main_monedes, sessio.getMonedas()));
    }

    private void mostrarMissatge(String text) {
        tvMissatge.setText(text);
        tvMissatge.setVisibility(View.VISIBLE);
    }

    private void avisar(String text) {
        Snackbar.make(recycler, text, Snackbar.LENGTH_LONG).show();
    }
}
