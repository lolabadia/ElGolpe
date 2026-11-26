package edu.upc.dsa.elgolpe.botiga;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import edu.upc.dsa.elgolpe.R;
import edu.upc.dsa.elgolpe.SessionManager;
import edu.upc.dsa.elgolpe.api.ApiClient;
import edu.upc.dsa.elgolpe.models.DevolucionRequest;
import edu.upc.dsa.elgolpe.models.DevolucionRespuesta;
import edu.upc.dsa.elgolpe.models.InventarioItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Inventari de l'usuari (GET /usuarios/{id}/inventario) i retorn d'objectes
 * (POST /usuarios/{id}/devoluciones).
 */
public class InventariActivity extends AppCompatActivity {

    private SessionManager sessio;
    private InventariAdapter adapter;
    private TextView tvMonedes, tvMissatge;
    private ProgressBar progress;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventari);
        setTitle(R.string.inventari_titol);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessio = new SessionManager(this);

        tvMonedes = findViewById(R.id.tvMonedes);
        tvMissatge = findViewById(R.id.tvMissatge);
        progress = findViewById(R.id.progress);
        recycler = findViewById(R.id.recycler);

        adapter = new InventariAdapter(this::demanarQuantitat);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        tvMissatge.setOnClickListener(v -> carregarInventari());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Es recarrega cada cop que s'obre, per si s'ha comprat alguna cosa a la botiga
        actualitzarMonedes();
        carregarInventari();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void carregarInventari() {
        progress.setVisibility(View.VISIBLE);
        tvMissatge.setVisibility(View.GONE);

        ApiClient.getApi().getInventario(sessio.getUsuarioId()).enqueue(new Callback<List<InventarioItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<InventarioItem>> call, @NonNull Response<List<InventarioItem>> response) {
                progress.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setItems(response.body());
                    if (adapter.getItemCount() == 0) mostrarMissatge(getString(R.string.inventari_buit));
                } else if (response.code() == 404) {
                    mostrarMissatge(getString(R.string.error_usuari_no_existeix));
                } else {
                    mostrarMissatge(getString(R.string.error_servidor, response.code())
                            + "\n" + getString(R.string.tocar_reintentar));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<InventarioItem>> call, @NonNull Throwable t) {
                progress.setVisibility(View.GONE);
                mostrarMissatge(getString(R.string.error_connexio) + "\n" + getString(R.string.tocar_reintentar));
            }
        });
    }

    private void demanarQuantitat(InventarioItem item) {
        QuantitatDialog.mostrar(this,
                getString(R.string.inventari_retornar_titol, item.getNombre()),
                getString(R.string.inventari_retornar),
                item.getCantidad(),
                q -> getString(R.string.inventari_retornar_resum, q, item.getCantidad()),
                q -> retornar(item, q));
    }

    private void retornar(InventarioItem item, int quantitat) {
        progress.setVisibility(View.VISIBLE);

        ApiClient.getApi().retornar(sessio.getUsuarioId(), new DevolucionRequest(item.getObjetoId(), quantitat))
                .enqueue(new Callback<DevolucionRespuesta>() {
                    @Override
                    public void onResponse(@NonNull Call<DevolucionRespuesta> call, @NonNull Response<DevolucionRespuesta> response) {
                        progress.setVisibility(View.GONE);
                        if (response.code() == 201 && response.body() != null) {
                            sessio.setMonedas(response.body().getMonedasRestantes());
                            actualitzarMonedes();
                            avisar(getString(R.string.inventari_retorn_ok, quantitat, item.getNombre(),
                                    response.body().getMonedasDevueltas()));
                            carregarInventari();
                        } else if (response.code() == 409) {
                            avisar(getString(R.string.error_no_tens_prou));
                            carregarInventari();
                        } else if (response.code() == 400) {
                            avisar(getString(R.string.error_quantitat));
                        } else if (response.code() == 404) {
                            // També surt 404 si el servidor encara no té la ruta de devolucions
                            avisar(getString(R.string.error_retorn_404));
                        } else {
                            avisar(getString(R.string.error_servidor, response.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DevolucionRespuesta> call, @NonNull Throwable t) {
                        progress.setVisibility(View.GONE);
                        avisar(getString(R.string.error_connexio));
                    }
                });
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
