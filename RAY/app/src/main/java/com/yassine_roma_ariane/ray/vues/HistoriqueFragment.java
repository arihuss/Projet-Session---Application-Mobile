package com.yassine_roma_ariane.ray.vues;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.yassine_roma_ariane.ray.R;
import com.yassine_roma_ariane.ray.modeles.Reservation;
import com.yassine_roma_ariane.ray.modeles.Voyage;
import com.yassine_roma_ariane.ray.viewModel.ReservationViewModel;
import com.yassine_roma_ariane.ray.viewModel.VoyageViewModel;
import com.yassine_roma_ariane.ray.vues.adaptateurs.HistoriqueAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoriqueFragment extends Fragment {

    private ListView lvReservations;
    private HistoriqueAdapter adaptateur;
    private ReservationViewModel reservationViewModel;
    private VoyageViewModel voyageViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_historique, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvReservations = view.findViewById(R.id.lvReservations);

        // Initialiser l'adaptateur avec une liste vide
        adaptateur = new HistoriqueAdapter(getActivity(), R.layout.activity_historique_adapter, new ArrayList<>());
        lvReservations.setAdapter(adaptateur);

        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
        voyageViewModel = new ViewModelProvider(this).get(VoyageViewModel.class);

        // Observer les voyages et les injecter dans l’adapter
        voyageViewModel.getVoyages().observe(getViewLifecycleOwner(), new Observer<List<Voyage>>() {
            @Override
            public void onChanged(List<Voyage> voyages) {
                if (voyages != null) {
                    adaptateur.setVoyages(voyages);
                }
            }
        });

        // Observer les réservations
        reservationViewModel.getReservations().observe(getViewLifecycleOwner(), new Observer<List<Reservation>>() {
            @Override
            public void onChanged(List<Reservation> reservations) {
                if (reservations != null) {
                    adaptateur.setReservations(reservations);
                } else {
                    Toast.makeText(getActivity(), "Erreur lors du chargement des réservations", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Charger clientId
        SharedPreferences prefs = getActivity().getSharedPreferences("session", getActivity().MODE_PRIVATE);
        String clientId = prefs.getString("clientId", null);

        if (clientId != null) {
            reservationViewModel.loadReservationsPourClient(clientId);
            voyageViewModel.refreshVoyages(); // on déclenche le fetch depuis le serveur JSON
        } else {
            Toast.makeText(getActivity(), "Aucun utilisateur connecté", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = getActivity().getSharedPreferences("session", getActivity().MODE_PRIVATE);
        String clientId = prefs.getString("clientId", null);

        if (clientId != null) {
            reservationViewModel.loadReservationsPourClient(clientId);
        }
    }

}
