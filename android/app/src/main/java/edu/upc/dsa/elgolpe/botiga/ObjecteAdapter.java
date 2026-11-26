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
import edu.upc.dsa.elgolpe.models.Objeto;

/** Llista d'objectes de la botiga, cadascun amb el botó "Comprar". */
public class ObjecteAdapter extends RecyclerView.Adapter<ObjecteAdapter.ViewHolder> {

    public interface OnComprarListener {
        void onComprar(Objeto objeto);
    }

    private final List<Objeto> objectes = new ArrayList<>();
    private final OnComprarListener listener;

    public ObjecteAdapter(OnComprarListener listener) {
        this.listener = listener;
    }

    public void setObjectes(List<Objeto> nous) {
        objectes.clear();
        if (nous != null) objectes.addAll(nous);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_objecte, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Objeto o = objectes.get(position);
        h.tvNom.setText(o.getNombre());
        h.tvDescripcio.setText(o.getDescripcion());
        h.tvPreu.setText(h.itemView.getContext().getString(R.string.botiga_preu, o.getPrecio()));
        h.btnComprar.setOnClickListener(v -> listener.onComprar(o));
    }

    @Override
    public int getItemCount() {
        return objectes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNom, tvDescripcio, tvPreu;
        final Button btnComprar;

        ViewHolder(View v) {
            super(v);
            tvNom = v.findViewById(R.id.tvNom);
            tvDescripcio = v.findViewById(R.id.tvDescripcio);
            tvPreu = v.findViewById(R.id.tvPreu);
            btnComprar = v.findViewById(R.id.btnComprar);
        }
    }
}
