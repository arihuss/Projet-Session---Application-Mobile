package com.yassine_roma_ariane.ray.vues;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.yassine_roma_ariane.ray.R;
import com.yassine_roma_ariane.ray.modeles.Reservation;
import com.yassine_roma_ariane.ray.modeles.Voyage;
import com.yassine_roma_ariane.ray.viewModel.ReservationViewModel;
import com.yassine_roma_ariane.ray.viewModel.VoyageViewModel;

public class ConfirmationActivity extends AppCompatActivity {

    private ImageView imageVoyage;
    private TextView txtNomVoyage, txtMessageConfirmation;
    private Button btnAnnuler, btnRetour;

    private Reservation reservation;
    private VoyageViewModel voyageViewModel;
    private ReservationViewModel reservationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        imageVoyage = findViewById(R.id.imageVoyage);
        txtNomVoyage = findViewById(R.id.txtNomVoyage);
        txtMessageConfirmation = findViewById(R.id.txtMessageConfirmation);
        btnAnnuler = findViewById(R.id.btnAnnuler);
        btnRetour = findViewById(R.id.btnRetour);

        // ViewModels
        voyageViewModel = new ViewModelProvider(this).get(VoyageViewModel.class);
        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        // Récupération de la réservation
        reservation = (Reservation) getIntent().getSerializableExtra("reservation");
        if (reservation == null) {
            Toast.makeText(this, "Aucune réservation reçue", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Charger les infos du voyage
        voyageViewModel.getVoyageParId(reservation.getVoyageId()).observe(this, voyage -> {
            if (voyage != null) {
                txtNomVoyage.setText(voyage.getNom_voyage());

                Glide.with(this)
                        .load(voyage.getImage_url())
                        .placeholder(R.drawable.ray_logo)
                        .into(imageVoyage);
            } else {
                txtNomVoyage.setText("Voyage #" + reservation.getVoyageId());
                imageVoyage.setImageResource(R.drawable.ray_logo);
            }
        });

        // Annuler la réservation
        btnAnnuler.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
            String clientId = prefs.getString("clientId", null);

            if (clientId == null) {
                Toast.makeText(this, "Erreur : client non connecté", Toast.LENGTH_SHORT).show();
                return;
            }

            // Mise à jour locale (SQLite)
            reservationViewModel.annulerReservation(reservation.getId(), clientId);

            // Mise à jour distante (serveur JSON)
            voyageViewModel.rendrePlacesDisponibles(
                    reservation.getVoyageId(),
                    reservation.getDate(),
                    reservation.getNbPersonnes()
            );

            Toast.makeText(this, "Réservation annulée", Toast.LENGTH_SHORT).show();
            finish(); // Retour à HistoriqueFragment
        });

        btnRetour.setOnClickListener(v -> finish());
    }
}
