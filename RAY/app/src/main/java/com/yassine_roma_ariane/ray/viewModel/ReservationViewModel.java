package com.yassine_roma_ariane.ray.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.yassine_roma_ariane.ray.modeles.Reservation;
import com.yassine_roma_ariane.ray.modeles.repositories.ReservationRepository;

import java.util.List;

public class ReservationViewModel extends AndroidViewModel {

    private ReservationRepository repository;
    private MutableLiveData<List<Reservation>> reservations = new MutableLiveData<>();

    public ReservationViewModel(@NonNull Application application) {
        super(application);
        repository = new ReservationRepository(application.getApplicationContext());
    }

    public LiveData<List<Reservation>> getReservations() {
        return reservations;
    }

    public void loadReservationsPourClient(int clientId) {
        List<Reservation> liste = repository.getReservationsPourClient(clientId);
        reservations.setValue(liste);
    }

    public void ajouterReservation(Reservation reservation) {
        repository.ajouterReservation(reservation);
        // recharge la liste après ajout si nécessaire
        loadReservationsPourClient(reservation.getClientId());
    }

    public void annulerReservation(int idReservation, int clientId) {
        repository.annulerReservation(idReservation);
        loadReservationsPourClient(clientId);
    }
}
