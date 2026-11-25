package edu.upc.dsa.elgolpe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import edu.upc.dsa.elgolpe.api.ApiClient;
import edu.upc.dsa.elgolpe.models.LoginRequest;
import edu.upc.dsa.elgolpe.models.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Pantalla d'inici de sessió: POST /usuarios/login.
 * Si el login és correcte, guarda l'usuari a SharedPreferences i obre MainActivity.
 */
public class LoginActivity extends AppCompatActivity {

    private TextInputLayout layoutNom, layoutContrasenya;
    private TextInputEditText etNom, etContrasenya;
    private Button btnEntrar, btnAnarRegistre;
    private ProgressBar progress;
    private TextView tvError;

    private SessionManager sessio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessio = new SessionManager(this);

        // Si ja hi ha una sessió guardada, anem directament a la pantalla principal
        if (sessio.haySesion()) {
            anarAMain();
            return;
        }

        setContentView(R.layout.activity_login);

        layoutNom = findViewById(R.id.layoutNom);
        layoutContrasenya = findViewById(R.id.layoutContrasenya);
        etNom = findViewById(R.id.etNom);
        etContrasenya = findViewById(R.id.etContrasenya);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnAnarRegistre = findViewById(R.id.btnAnarRegistre);
        progress = findViewById(R.id.progress);
        tvError = findViewById(R.id.tvError);

        btnEntrar.setOnClickListener(v -> ferLogin());
        btnAnarRegistre.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void ferLogin() {
        String nom = text(etNom);
        String contrasenya = text(etContrasenya);

        layoutNom.setError(null);
        layoutContrasenya.setError(null);
        tvError.setVisibility(View.GONE);

        boolean ok = true;
        if (nom.isEmpty()) {
            layoutNom.setError(getString(R.string.error_camp_buit));
            ok = false;
        }
        if (contrasenya.isEmpty()) {
            layoutContrasenya.setError(getString(R.string.error_camp_buit));
            ok = false;
        }
        if (!ok) return;

        carregant(true);

        ApiClient.getApi().login(new LoginRequest(nom, contrasenya)).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                carregant(false);
                if (response.code() == 200 && response.body() != null) {
                    sessio.guardarUsuario(response.body());
                    anarAMain();
                } else if (response.code() == 400) {
                    mostrarError(getString(R.string.error_400));
                } else if (response.code() == 401) {
                    mostrarError(getString(R.string.error_401));
                } else {
                    mostrarError(getString(R.string.error_servidor, response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                carregant(false);
                mostrarError(getString(R.string.error_connexio));
            }
        });
    }

    private void anarAMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void carregant(boolean si) {
        progress.setVisibility(si ? View.VISIBLE : View.GONE);
        btnEntrar.setEnabled(!si);
        btnAnarRegistre.setEnabled(!si);
    }

    private void mostrarError(String missatge) {
        tvError.setText(missatge);
        tvError.setVisibility(View.VISIBLE);
    }

    private static String text(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }
}
