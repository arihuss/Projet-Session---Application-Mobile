package com.yassine_roma_ariane.ray.modeles.dao;

import com.yassine_roma_ariane.ray.modeles.Reservation;

import java.util.List;

public interface InterfaceReservationDAO {
    void ajouterReservation(Reservation reservation);
    List<Reservation> getReservationsPourClient(int clientId);
    void annulerReservation(int id);
}
