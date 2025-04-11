package com.yassine_roma_ariane.ray.modeles.dao;

import com.yassine_roma_ariane.ray.modeles.Reservation;

import java.util.List;

public interface InterfaceReservationDAO {

    /**
     * Ajouter une reservation
     * @param reservation
     */
    void ajouterReservation(Reservation reservation);

    /**
     * Récupérer les reservations pour le client
     * @param clientId Le id du client
     * @return Une liste de reservation
     */
    List<Reservation> getReservationsPourClient(String clientId);

    /**
     * Annuler la reservation du client
     * @param id le id du client
     */
    void annulerReservation(int id);
}
