package com.yassine_roma_ariane.ray.modeles.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yassine_roma_ariane.ray.modeles.Reservation;
import com.yassine_roma_ariane.ray.modeles.dao.InterfaceReservationDAO;
import com.yassine_roma_ariane.ray.modeles.dao.ReservationDAO;
import com.yassine_roma_ariane.ray.modeles.sqlite.ReservationHelper;

import java.util.List;

public class ReservationRepository {

    private InterfaceReservationDAO dao;

    public ReservationRepository(Context context) {
        ReservationHelper helper = new ReservationHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        dao = new ReservationDAO(db); // implémentation concrète
    }

    public void ajouterReservation(Reservation reservation) {
        dao.ajouterReservation(reservation);

        // Mettre à jour les places disponibles côté serveur JSON
        VoyageRepository voyageRepository = new VoyageRepository();
        voyageRepository.rendrePlacesDisponibles(
                reservation.getVoyageId(),
                reservation.getDate(),
                -reservation.getNbPersonnes(), // Décrémentation
                new VoyageRepository.UpdatePlacesCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("ReservationRepository", "Places mises à jour avec succès");
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e("ReservationRepository", "Erreur mise à jour places : " + errorMessage);
                    }
                }
        );
    }


    public List<Reservation> getReservationsPourClient(String clientId) {
        return dao.getReservationsPourClient(clientId);
    }

    public void annulerReservation(int idReservation) {
        dao.annulerReservation(idReservation);
    }
}

