package edu.upc.dsa.elgolpe.botiga;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.function.Consumer;
import java.util.function.IntFunction;

/**
 * Diàleg per triar quantes unitats es compren o es retornen.
 * Mostra a sota un text que es recalcula en canviar la quantitat (p. ex. el preu total).
 */
public final class QuantitatDialog {

    private QuantitatDialog() {
    }

    public static void mostrar(Context context, String titol, String textBotoOk, int maxim,
                               IntFunction<String> textResum, Consumer<Integer> onAcceptar) {
        int padding = (int) (24 * context.getResources().getDisplayMetrics().density);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setPadding(padding, padding / 2, padding, 0);

        NumberPicker picker = new NumberPicker(context);
        picker.setMinValue(1);
        picker.setMaxValue(Math.max(1, maxim));
        picker.setValue(1);
        picker.setWrapSelectorWheel(false);

        TextView tvResum = new TextView(context);
        tvResum.setGravity(Gravity.CENTER);
        tvResum.setText(textResum.apply(1));
        picker.setOnValueChangedListener((p, abans, ara) -> tvResum.setText(textResum.apply(ara)));

        layout.addView(picker);
        layout.addView(tvResum);

        new AlertDialog.Builder(context)
                .setTitle(titol)
                .setView(layout)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(textBotoOk, (d, w) -> onAcceptar.accept(picker.getValue()))
                .show();
    }
}
