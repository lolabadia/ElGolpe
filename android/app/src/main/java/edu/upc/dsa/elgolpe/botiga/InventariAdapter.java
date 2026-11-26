package edu.upc.dsa.elgolpe.botiga;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.upc.dsa.elgolpe.R;
import edu.upc.dsa.elgolpe.models.InventarioItem;

/** Llista dels objectes que té l'usuari, cadascun amb el botó "Retornar". */
public class InventariAdapter extends RecyclerView.Adapter<InventariAdapter.ViewHolder> {

    public interface OnRetornarListener {
        void onRetornar(InventarioItem item);
    }

    private final List<InventarioItem> items = new ArrayList<>();
    private final OnRetornarListener listener;

    public InventariAdapter(OnRetornarListener listener) {
        this.listener = listener;
    }

    public void setItems(List<InventarioItem> nous) {
        items.clear();
        if (nous != null) {
            for (InventarioItem i : nous) {
                if (i.getCantidad() > 0) items.add(i); // no mostrem objectes gastats
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventari, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        InventarioItem item = items.get(position);
        h.tvNom.setText(item.getNombre());
        h.tvQuantitat.setText(h.itemView.getContext().getString(R.string.inventari_quantitat, item.getCantidad()));
        h.btnRetornar.setOnClickListener(v -> listener.onRetornar(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNom, tvQuantitat;
        final Button btnRetornar;

        ViewHolder(View v) {
            super(v);
            tvNom = v.findViewById(R.id.tvNom);
            tvQuantitat = v.findViewById(R.id.tvQuantitat);
            btnRetornar = v.findViewById(R.id.btnRetornar);
        }
    }
}
