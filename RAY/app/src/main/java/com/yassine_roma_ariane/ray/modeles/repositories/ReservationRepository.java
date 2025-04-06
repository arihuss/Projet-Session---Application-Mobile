package com.yassine_roma_ariane.ray.modeles.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
    }

    public List<Reservation> getReservationsPourClient(int clientId) {
        return dao.getReservationsPourClient(clientId);
    }

    public void annulerReservation(int idReservation) {
        dao.annulerReservation(idReservation);
    }
}

