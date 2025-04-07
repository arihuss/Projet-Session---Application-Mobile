package com.yassine_roma_ariane.ray.vues.adaptateurs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.yassine_roma_ariane.ray.R;
import com.yassine_roma_ariane.ray.modeles.Reservation;
import com.yassine_roma_ariane.ray.vues.ConfirmationActivity;

import java.util.List;

public class HistoriqueAdapter extends ArrayAdapter {
    private Context contexte;
    private int viewRessourceId;
    private Resources resources;
    private List<Reservation> reservations;
    private TextView tvAnnuler;

    public HistoriqueAdapter(@NonNull Context context, int resource, @NonNull List <Reservation> objects) {
        super(context, resource, objects);
        contexte = context;
        viewRessourceId = resource;
        reservations = objects;
    }

    @Override
    public int getCount() {
        return reservations.size();
    }


    @SuppressLint("NewApi")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) contexte.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(viewRessourceId, parent, false);
        }

        Reservation reservation = reservations.get(position);

        if(reservation != null) {
            // Liaison avec layout d'une réservation dans la liste
            TextView tvNomReservation = view.findViewById(R.id.tvNomReservation);
            TextView tvDateReserv = view.findViewById(R.id.tvDateReserv);
            TextView tvPrixReserv = view.findViewById(R.id.tvPrixReserv);
            Button btnStatut = view.findViewById(R.id.btnStatut);
            ImageView imgReservation = view.findViewById(R.id.imgReservation);

            // Set le text
            tvNomReservation.setText("Nom de la destination");
            tvDateReserv.setText(reservation.getDate());
            tvPrixReserv.setText("Prix de la réservation" + " $");
            btnStatut.setText(reservation.getStatut());

            // Temporairement
            imgReservation.setImageResource(R.drawable.ray_logo);

            // TODO: Affichage de l'image de la destination (par le voyageId, il faut get le imgUrl)
//            Glide.with(contexte)
//                    .load(reservation.getImageUrl())  a modifier
//                    .placeholder(R.drawable.ray_logo)
//                    .into(imgReservation);


            // écouteur pour l'annulation
            tvAnnuler = view.findViewById(R.id.tvAnnuler);
            tvAnnuler.setOnClickListener(v -> {
                Intent intent =  new Intent(getContext(), ConfirmationActivity.class);
                intent.putExtra("reservation", reservation);
                getContext().startActivity(intent);
            });
        }

        return view;

    }


}