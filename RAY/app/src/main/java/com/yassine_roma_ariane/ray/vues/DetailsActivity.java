package com.yassine_roma_ariane.ray.vues;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.yassine_roma_ariane.ray.R;
import com.yassine_roma_ariane.ray.modeles.Trip;
import com.yassine_roma_ariane.ray.modeles.Voyage;
import com.yassine_roma_ariane.ray.viewModel.VoyageViewModel;
import com.yassine_roma_ariane.ray.modeles.Reservation;

import com.yassine_roma_ariane.ray.viewModel.ReservationViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private ImageView imageVoyage;
    private TextView txtTitre, txtDescription, txtDureePrix, txtActivites, txtPlacesRestantes;
    private Spinner spinnerDates;
    private Button btnReserverMaintenant, btnReserver, btnRetour;
    private EditText edtNombrePersonnes;
    private LinearLayout layoutNombrePersonnes;
    private Voyage voyageSelectionne;
    private Trip tripSelectionne;
    private VoyageViewModel viewModel;
    private ReservationViewModel reservationViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imageVoyage = findViewById(R.id.imageVoyage);
        txtTitre = findViewById(R.id.txtTitreVoyage);
        txtDescription = findViewById(R.id.txtDescriptionVoyage);
        txtDureePrix = findViewById(R.id.txtDureePrixVoyage);
        txtActivites = findViewById(R.id.txtActivitesVoyage);
        txtPlacesRestantes = findViewById(R.id.txtPlacesRestantes);
        spinnerDates = findViewById(R.id.spinnerDates);
        btnReserverMaintenant = findViewById(R.id.btnReserverMaintenant);
        btnReserver = findViewById(R.id.btnReserver);
        btnRetour = findViewById(R.id.btnRetour);
        edtNombrePersonnes = findViewById(R.id.edtNombrePersonnes);
        layoutNombrePersonnes = findViewById(R.id.layoutNombrePersonnes); // Assure-toi que cet ID est bien dans ton layout

        desactiverReservation();

        // Initialisation des ViewModels
        viewModel = new ViewModelProvider(this).get(VoyageViewModel.class);
        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        int voyageId = getIntent().getIntExtra("ID_VOYAGE", -1);

        viewModel.getVoyageParId(voyageId).observe(this, new Observer<Voyage>() {
            @Override
            public void onChanged(Voyage voyage) {
                if (voyage != null) {
                    voyageSelectionne = voyage;
                    afficherVoyage(voyage);
                } else {
                    Toast.makeText(DetailsActivity.this, "Voyage non trouvé", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinnerDates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tripSelectionne = voyageSelectionne.getTrips().get(position);
                txtPlacesRestantes.setText(tripSelectionne.getNb_places_disponibles() + " places disponibles");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        btnReserverMaintenant.setOnClickListener(v -> {
            activerReservation();
        });

        btnReserver.setOnClickListener(v -> {
            if (tripSelectionne == null) {
                Toast.makeText(this, "Veuillez choisir une date d’abord", Toast.LENGTH_SHORT).show();
                return;
            }

            String nbStr = edtNombrePersonnes.getText().toString();
            if (nbStr.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer un nombre de personnes", Toast.LENGTH_SHORT).show();
                return;
            }

            int nbPlacesDemandees = Integer.parseInt(nbStr);
            if (nbPlacesDemandees <= 0) {
                Toast.makeText(this, "Nombre de personnes invalide", Toast.LENGTH_SHORT).show();
                return;
            }

            if (nbPlacesDemandees > tripSelectionne.getNb_places_disponibles()) {
                Toast.makeText(this, "Nombre de places insuffisant", Toast.LENGTH_SHORT).show();
                return;
            }

            double prixTotal = nbPlacesDemandees * voyageSelectionne.getPrix();
            Toast.makeText(this, "Réservation confirmée: " + prixTotal + "$", Toast.LENGTH_LONG).show();

            tripSelectionne.setNb_places_disponibles(
                    tripSelectionne.getNb_places_disponibles() - nbPlacesDemandees);
            txtPlacesRestantes.setText(tripSelectionne.getNb_places_disponibles() + " places disponibles");


            int clientId = getSharedPreferences("session", MODE_PRIVATE).getInt("clientId", -1);


            Reservation reservation = new Reservation(
                    0,
                    voyageSelectionne.getId(),
                    clientId,
                    tripSelectionne.getDate(),
                    nbPlacesDemandees,
                    "confirmée"
            );

            reservationViewModel.ajouterReservation(reservation);
        });

        btnRetour.setOnClickListener(v -> finish());
    }

    private void afficherVoyage(Voyage voyage) {
        txtTitre.setText(voyage.getNom_voyage());
        txtDescription.setText(voyage.getDescription());
        txtDureePrix.setText("Durée : " + voyage.getDuree_jours() + " jours\nPrix : " + voyage.getPrix() + "$ par personne");
        txtActivites.setText("Activités : " + voyage.getActivites_incluses());

        // Chargement image avec Glide
        Glide.with(this).load(voyage.getImage_url()).into(imageVoyage);

        List<String> datesList = new ArrayList<>();
        for (Trip trip : voyage.getTrips()) {
            datesList.add(trip.getDate());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, datesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDates.setAdapter(adapter);
        spinnerDates.setSelection(0);
    }

    private void desactiverReservation() {
        layoutNombrePersonnes.setAlpha(0.3f);
        layoutNombrePersonnes.setEnabled(false);
        for (int i = 0; i < layoutNombrePersonnes.getChildCount(); i++) {
            View child = layoutNombrePersonnes.getChildAt(i);
            child.setEnabled(false);
        }
    }

    private void activerReservation() {
        layoutNombrePersonnes.setAlpha(1f);
        layoutNombrePersonnes.setEnabled(true);
        for (int i = 0; i < layoutNombrePersonnes.getChildCount(); i++) {
            View child = layoutNombrePersonnes.getChildAt(i);
            child.setEnabled(true);
        }
    }
}
