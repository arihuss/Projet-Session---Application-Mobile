package com.yassine_roma_ariane.ray.vues.adaptateurs;

/**
 * Adaptateur pour un historique de voyage
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.yassine_roma_ariane.ray.R;
import com.yassine_roma_ariane.ray.modeles.Reservation;
import com.yassine_roma_ariane.ray.modeles.Voyage;
import com.yassine_roma_ariane.ray.vues.ConfirmationActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoriqueAdapter extends ArrayAdapter<Reservation> {

    private final Context context;
    private final int layoutResourceId;
    private final List<Reservation> reservations;

    // Association entre id de voyage et objet Voyage
    private final Map<Integer, Voyage> voyagesMap = new HashMap<>();

    public HistoriqueAdapter(@NonNull Context context, int resource, @NonNull List<Reservation> reservations) {
        super(context, resource, reservations);
        this.context = context;
        this.layoutResourceId = resource;
        this.reservations = reservations;
    }

    public void setReservations(List<Reservation> newReservations) {
        reservations.clear();
        reservations.addAll(newReservations);
        notifyDataSetChanged();
    }

    public void setVoyages(List<Voyage> voyages) {
        voyagesMap.clear();
        for (Voyage v : voyages) {
            voyagesMap.put(v.getId(), v);
        }
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(layoutResourceId, parent, false);
        }

        Reservation reservation = reservations.get(position);

        if (reservation != null) {
            TextView tvNom = view.findViewById(R.id.tvNomReservation);
            TextView tvDate = view.findViewById(R.id.tvDateReserv);
            TextView tvPrix = view.findViewById(R.id.tvPrixReserv);
            Button btnStatut = view.findViewById(R.id.btnStatut);
            TextView tvAnnuler = view.findViewById(R.id.tvAnnuler);
            ImageView img = view.findViewById(R.id.imgReservation);

            // Cherche le voyage lié à la réservation
            Voyage voyage = voyagesMap.get(reservation.getVoyageId());

            if (voyage != null) {
                // Affiche le vrai nom du voyage
                tvNom.setText(voyage.getNom_voyage());

                // Affiche l'image du voyage
                Glide.with(context)
                        .load(voyage.getImage_url())
                        .placeholder(R.drawable.ray_logo)
                        .into(img);

                // Calcul du prix total
                double prixTotal = voyage.getPrix() * reservation.getNbPersonnes();
                tvPrix.setText(String.format("%.2f CAD", prixTotal));
            } else {
                // Fallback si aucun voyage trouvé
                tvNom.setText("Voyage #" + reservation.getVoyageId());
                img.setImageResource(R.drawable.ray_logo);
                tvPrix.setText("??? CAD");
            }

            tvDate.setText(reservation.getDate());
            btnStatut.setText(reservation.getStatut());

            if ("confirmée".equalsIgnoreCase(reservation.getStatut())) {
                btnStatut.setBackgroundTintList(context.getColorStateList(R.color.accentPale));
            } else {
                btnStatut.setBackgroundTintList(context.getColorStateList(R.color.rouge));
            }

            // Affiche "Annuler ?" seulement si la réservation est future et confirmée
            if ("confirmée".equalsIgnoreCase(reservation.getStatut()) && dateFuture(reservation.getDate())) {
                tvAnnuler.setVisibility(View.VISIBLE);
                tvAnnuler.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ConfirmationActivity.class);
                    intent.putExtra("reservation", reservation);
                    context.startActivity(intent);
                });
            } else {
                tvAnnuler.setVisibility(View.GONE);
            }
        }

        return view;
    }

    private boolean dateFuture(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateStr);
            return date != null && date.after(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
