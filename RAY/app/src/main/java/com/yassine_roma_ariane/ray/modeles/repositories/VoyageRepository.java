package com.yassine_roma_ariane.ray.modeles.repositories;

import com.yassine_roma_ariane.ray.modeles.Trip;
import com.yassine_roma_ariane.ray.modeles.Voyage;
import com.yassine_roma_ariane.ray.modeles.dao.VoyageDAO;
import com.yassine_roma_ariane.ray.modeles.dao.HttpJsonService;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class VoyageRepository {

    public interface FetchVoyageCallback {
        void onFetch(List<Voyage> voyages);
        void onError(String errorMessage);
    }

    public interface FetchVoyageByFilterCallback {
        void onFetch(List<Voyage> voyages);
        void onError(String errorMessage);
    }

    public interface UpdatePlacesCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public void fetchVoyagesFromAPI(FetchVoyageCallback callback) {
        new Thread(() -> {
            try {
                List<Voyage> voyages = VoyageDAO.getVoyages();
                callback.onFetch(voyages);
            } catch (IOException | JSONException e) {
                callback.onError("Erreur: " + e.getMessage());
            }
        }).start();
    }

    public void fetchVoyagesByFilter(String destination, String type, String date, double budget, FetchVoyageByFilterCallback callback) {
        new Thread(() -> {
            try {
                List<Voyage> voyages = VoyageDAO.getVoyagesByFilter(destination, type, date, budget);
                callback.onFetch(voyages);
            } catch (IOException | JSONException e) {
                callback.onError("Erreur: " + e.getMessage());
            }
        }).start();
    }

    public void rendrePlacesDisponibles(int voyageId, String date, int nbPlacesARemettre, UpdatePlacesCallback callback) {
        fetchVoyagesFromAPI(new FetchVoyageCallback() {
            @Override
            public void onFetch(List<Voyage> voyages) {
                for (Voyage voyage : voyages) {
                    if (voyage.getId() == voyageId) {
                        for (Trip trip : voyage.getTrips()) {
                            if (trip.getDate().equals(date)) {
                                int updated = trip.getNb_places_disponibles() + nbPlacesARemettre;
                                trip.setNb_places_disponibles(updated);

                                // Appel à la méthode pour mettre à jour le JSON via HTTP PUT
                                updateVoyageOnServer(voyage, callback);
                                return;
                            }
                        }
                    }
                }

                callback.onFailure("Voyage ou date introuvable");
            }

            @Override
            public void onError(String errorMessage) {
                callback.onFailure("Erreur de chargement des voyages : " + errorMessage);
            }
        });
    }

    private void updateVoyageOnServer(Voyage voyage, UpdatePlacesCallback callback) {
        new Thread(() -> {
            try {
                boolean success = new HttpJsonService().updateVoyage(voyage);
                if (success) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Échec de mise à jour du serveur");
                }
            } catch (IOException | JSONException e) {
                callback.onFailure("Erreur lors de l'appel PUT : " + e.getMessage());
            }
        }).start();
    }
}
