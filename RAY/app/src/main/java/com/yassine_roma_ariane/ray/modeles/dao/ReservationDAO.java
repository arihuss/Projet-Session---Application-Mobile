package com.yassine_roma_ariane.ray.modeles.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yassine_roma_ariane.ray.modeles.Reservation;
import com.yassine_roma_ariane.ray.modeles.sqlite.ReservationHelper;

import java.util.ArrayList;
import java.util.List;

public class ReservationDAO implements InterfaceReservationDAO {

    private SQLiteDatabase db;

    public ReservationDAO(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void ajouterReservation(Reservation reservation) {
        ContentValues values = new ContentValues();
        values.put(ReservationHelper.COL_VOYAGE_ID, reservation.getVoyageId());
        values.put(ReservationHelper.COL_CLIENT_ID, reservation.getClientId());
        values.put(ReservationHelper.COL_DATE, reservation.getDate());
        values.put(ReservationHelper.COL_NB_PERSONNES, reservation.getNbPersonnes());
        values.put(ReservationHelper.COL_STATUT, reservation.getStatut());

        db.insert(ReservationHelper.TABLE_NAME, null, values);
    }

    @Override
    public List<Reservation> getReservationsPourClient(int clientId) {
        List<Reservation> liste = new ArrayList<>();

        Cursor cursor = db.query(
                ReservationHelper.TABLE_NAME,
                null,
                ReservationHelper.COL_CLIENT_ID + " = ?",
                new String[]{String.valueOf(clientId)},
                null,
                null,
                ReservationHelper.COL_DATE + " ASC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Reservation reservation = new Reservation();
                reservation.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ReservationHelper.COL_ID)));
                reservation.setVoyageId(cursor.getInt(cursor.getColumnIndexOrThrow(ReservationHelper.COL_VOYAGE_ID)));
                reservation.setClientId(cursor.getInt(cursor.getColumnIndexOrThrow(ReservationHelper.COL_CLIENT_ID)));
                reservation.setDate(cursor.getString(cursor.getColumnIndexOrThrow(ReservationHelper.COL_DATE)));
                reservation.setNbPersonnes(cursor.getInt(cursor.getColumnIndexOrThrow(ReservationHelper.COL_NB_PERSONNES)));
                reservation.setStatut(cursor.getString(cursor.getColumnIndexOrThrow(ReservationHelper.COL_STATUT)));

                liste.add(reservation);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return liste;
    }

    @Override
    public void annulerReservation(int id) {
        ContentValues values = new ContentValues();
        values.put(ReservationHelper.COL_STATUT, "annulée");

        db.update(
                ReservationHelper.TABLE_NAME,
                values,
                ReservationHelper.COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }
}
