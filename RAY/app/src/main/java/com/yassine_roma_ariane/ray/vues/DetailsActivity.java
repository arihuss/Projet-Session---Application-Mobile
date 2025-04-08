package com.yassine_roma_ariane.ray.vues;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.yassine_roma_ariane.ray.R;
import com.yassine_roma_ariane.ray.modeles.Reservation;
import com.yassine_roma_ariane.ray.modeles.Trip;
import com.yassine_roma_ariane.ray.modeles.Voyage;
import com.yassine_roma_ariane.ray.viewModel.ReservationViewModel;
import com.yassine_roma_ariane.ray.viewModel.VoyageViewModel;

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

    private VoyageViewModel voyageViewModel;
    private ReservationViewModel reservationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initialiserUI();
        initialiserViewModels();

        int voyageId = getIntent().getIntExtra("ID_VOYAGE", -1);

        voyageViewModel.getVoyageParId(voyageId).observe(this, voyage -> {
            if (voyage != null) {
                voyageSelectionne = voyage;
                afficherVoyage(voyage);
            } else {
                Toast.makeText(this, "Voyage introuvable", Toast.LENGTH_SHORT).show();
            }
        });

        spinnerDates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tripSelectionne = voyageSelectionne.getTrips().get(position);
                txtPlacesRestantes.setText(tripSelectionne.getNb_places_disponibles() + " places disponibles");
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnReserverMaintenant.setOnClickListener(v -> activerReservation());

        btnReserver.setOnClickListener(v -> gererReservation());

        btnRetour.setOnClickListener(v -> finish());
    }

    private void initialiserUI() {
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
        layoutNombrePersonnes = findViewById(R.id.layoutNombrePersonnes);
        desactiverReservation();
    }

    private void initialiserViewModels() {
        voyageViewModel = new ViewModelProvider(this).get(VoyageViewModel.class);
        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
    }

    private void afficherVoyage(Voyage voyage) {
        txtTitre.setText(voyage.getNom_voyage());
        txtDescription.setText(voyage.getDescription());
        txtDureePrix.setText("Durée : " + voyage.getDuree_jours() + " jours\nPrix : " + voyage.getPrix() + "$");
        txtActivites.setText("Activités : " + voyage.getActivites_incluses());
        Glide.with(this).load(voyage.getImage_url()).into(imageVoyage);

        List<String> datesList = new ArrayList<>();
        for (Trip trip : voyage.getTrips()) {
            datesList.add(trip.getDate());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_selected_item, datesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDates.setAdapter(adapter);
    }

    private void gererReservation() {
        if (tripSelectionne == null) {
            Toast.makeText(this, "Veuillez choisir une date", Toast.LENGTH_SHORT).show();
            return;
        }

        String nbStr = edtNombrePersonnes.getText().toString();
        if (nbStr.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un nombre de personnes", Toast.LENGTH_SHORT).show();
            return;
        }

        int nbPlaces = Integer.parseInt(nbStr);
        if (nbPlaces <= 0 || nbPlaces > tripSelectionne.getNb_places_disponibles()) {
            Toast.makeText(this, "Nombre de places invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        String clientId = getSharedPreferences("session", MODE_PRIVATE).getString("clientId", null);
        double prixTotal = nbPlaces * voyageSelectionne.getPrix();

        Reservation reservation = new Reservation(
                0,
                voyageSelectionne.getId(),
                clientId,
                tripSelectionne.getDate(),
                nbPlaces,
                "confirmée"
        );

        reservationViewModel.ajouterReservation(reservation);
        voyageViewModel.rendrePlacesDisponibles(voyageSelectionne.getId(), tripSelectionne.getDate(), -nbPlaces);

        Toast.makeText(this, "Réservation confirmée: " + prixTotal + "$", Toast.LENGTH_LONG).show();
        txtPlacesRestantes.setText((tripSelectionne.getNb_places_disponibles() - nbPlaces) + " places disponibles");

        desactiverReservation();
    }

    private void desactiverReservation() {
        layoutNombrePersonnes.setAlpha(0.3f);
        setEnabledRecursive(layoutNombrePersonnes, false);
    }

    private void activerReservation() {
        layoutNombrePersonnes.setAlpha(1f);
        setEnabledRecursive(layoutNombrePersonnes, true);
    }

    private void setEnabledRecursive(ViewGroup group, boolean enabled) {
        group.setEnabled(enabled);
        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            child.setEnabled(enabled);
        }
    }
}
