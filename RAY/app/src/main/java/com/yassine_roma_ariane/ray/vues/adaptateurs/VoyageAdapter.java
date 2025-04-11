package com.yassine_roma_ariane.ray.vues.adaptateurs;

/**
 * Adaptateur pour un voyage
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.yassine_roma_ariane.ray.R;
import com.yassine_roma_ariane.ray.modeles.Voyage;

import java.util.ArrayList;
import java.util.List;

public class VoyageAdapter extends ArrayAdapter {


    private Context contexte;
    private int viewResourceId;
    private Resources resources;

    private List<Voyage> voyages = new ArrayList<>();
    public VoyageAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.contexte = context;
        this.viewResourceId = resource;
        this.resources = contexte.getResources();
    }

    public void setVoyages(List<Voyage> voyages) {
        this.voyages.clear();
        this.voyages.addAll(voyages);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return voyages.size();
    }

    public Voyage getItem(int position) {
        return voyages.get(position);
    }

    @SuppressLint("NewApi")
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) contexte.getSystemService(Context.
                    LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(this.viewResourceId, parent, false);
        }

        Voyage voyage = getItem(position);

        if (voyage != null) {
            final TextView txtTitre = (TextView) view.findViewById(R.id.txtTitre);
            final TextView txtHeaderPrix = (TextView) view.findViewById(R.id.txtHeaderPrix);
            final TextView txtDesc = (TextView) view.findViewById(R.id.txtDesc);
            final ImageView imgVoyage = (ImageView) view.findViewById(R.id.imgVoyage);

            if (voyage != null) {
                txtTitre.setText(voyage.getNom_voyage());
                txtHeaderPrix.setText(voyage.getPrix() + "$");
                txtDesc.setText(voyage.getDescription());

                Glide.with(contexte)
                        .load(voyage.getImage_url())
                        .placeholder(R.drawable.ray_logo)
                        .error(R.drawable.ray_logo)
                        .into(imgVoyage);
            }
        }
        return view;
    }
}