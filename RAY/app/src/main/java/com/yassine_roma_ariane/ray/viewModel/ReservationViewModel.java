package com.yassine_roma_ariane.ray.viewModel;

/**
 * Appelle les fonctions du Respository sur les reservations
 */

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.yassine_roma_ariane.ray.modeles.Reservation;
import com.yassine_roma_ariane.ray.modeles.repositories.ReservationRepository;

import java.util.List;

public class ReservationViewModel extends AndroidViewModel {

    private final ReservationRepository repository;
    private final MutableLiveData<List<Reservation>> reservations = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();

    public ReservationViewModel(@NonNull Application application) {
        super(application);
        repository = new ReservationRepository(application.getApplicationContext());
    }

    public LiveData<List<Reservation>> getReservations() {
        return reservations;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public void loadReservationsPourClient(String clientId) {
        new Thread(() -> {
            List<Reservation> liste = repository.getReservationsPourClient(clientId);
            reservations.postValue(liste);
        }).start();
    }

    public void ajouterReservation(Reservation reservation) {
        new Thread(() -> {
            repository.ajouterReservation(reservation);
            message.postValue("Réservation enregistrée avec succès.");
            loadReservationsPourClient(reservation.getClientId());
        }).start();
    }

    public void annulerReservation(int idReservation, String clientId) {
        new Thread(() -> {
            repository.annulerReservation(idReservation);
            message.postValue("Réservation annulée.");
            loadReservationsPourClient(clientId);
        }).start();
    }
}
