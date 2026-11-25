package edu.upc.dsa.elgolpe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import edu.upc.dsa.elgolpe.api.ApiClient;
import edu.upc.dsa.elgolpe.models.RegistroRequest;
import edu.upc.dsa.elgolpe.models.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Pantalla de registre: POST /usuarios/registro.
 * Si es crea el compte, l'usuari queda amb la sessió iniciada i s'obre MainActivity.
 */
public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout layoutNom, layoutEmail, layoutContrasenya, layoutRepetir;
    private TextInputEditText etNom, etEmail, etContrasenya, etRepetir;
    private Button btnCrear, btnAnarLogin;
    private ProgressBar progress;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        layoutNom = findViewById(R.id.layoutNom);
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutContrasenya = findViewById(R.id.layoutContrasenya);
        layoutRepetir = findViewById(R.id.layoutRepetir);
        etNom = findViewById(R.id.etNom);
        etEmail = findViewById(R.id.etEmail);
        etContrasenya = findViewById(R.id.etContrasenya);
        etRepetir = findViewById(R.id.etRepetir);
        btnCrear = findViewById(R.id.btnCrear);
        btnAnarLogin = findViewById(R.id.btnAnarLogin);
        progress = findViewById(R.id.progress);
        tvError = findViewById(R.id.tvError);

        btnCrear.setOnClickListener(v -> ferRegistre());
        btnAnarLogin.setOnClickListener(v -> finish()); // torna al login
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void ferRegistre() {
        String nom = text(etNom);
        String email = text(etEmail);
        String contrasenya = text(etContrasenya);
        String repetir = text(etRepetir);

        if (!validar(nom, email, contrasenya, repetir)) return;

        carregant(true);

        RegistroRequest body = new RegistroRequest(nom, email, contrasenya);
        ApiClient.getApi().registro(body).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                carregant(false);
                if (response.code() == 201 && response.body() != null) {
                    new SessionManager(RegisterActivity.this).guardarUsuario(response.body());
                    Toast.makeText(RegisterActivity.this, R.string.registre_ok, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else if (response.code() == 400) {
                    mostrarError(getString(R.string.error_400));
                } else if (response.code() == 409) {
                    layoutNom.setError(getString(R.string.error_409));
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

    /** Comprova els camps abans d'enviar res al servidor. */
    private boolean validar(String nom, String email, String contrasenya, String repetir) {
        layoutNom.setError(null);
        layoutEmail.setError(null);
        layoutContrasenya.setError(null);
        layoutRepetir.setError(null);
        tvError.setVisibility(View.GONE);

        boolean ok = true;
        if (nom.isEmpty()) {
            layoutNom.setError(getString(R.string.error_camp_buit));
            ok = false;
        }
        if (email.isEmpty()) {
            layoutEmail.setError(getString(R.string.error_camp_buit));
            ok = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            layoutEmail.setError(getString(R.string.error_email));
            ok = false;
        }
        if (contrasenya.isEmpty()) {
            layoutContrasenya.setError(getString(R.string.error_camp_buit));
            ok = false;
        }
        if (!contrasenya.equals(repetir)) {
            layoutRepetir.setError(getString(R.string.error_contrasenyes));
            ok = false;
        }
        return ok;
    }

    private void carregant(boolean si) {
        progress.setVisibility(si ? View.VISIBLE : View.GONE);
        btnCrear.setEnabled(!si);
        btnAnarLogin.setEnabled(!si);
    }

    private void mostrarError(String missatge) {
        tvError.setText(missatge);
        tvError.setVisibility(View.VISIBLE);
    }

    private static String text(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }
}
