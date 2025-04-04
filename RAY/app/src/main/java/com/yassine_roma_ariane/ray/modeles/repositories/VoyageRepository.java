package com.yassine_roma_ariane.ray.modeles.repositories;


import com.yassine_roma_ariane.ray.modeles.CompteUtilisateur;
import com.yassine_roma_ariane.ray.modeles.Voyage;
import com.yassine_roma_ariane.ray.modeles.dao.CompteUtilisateurDAO;
import com.yassine_roma_ariane.ray.modeles.dao.VoyageDAO;

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

    public void fetchVoyagesFromAPI(VoyageRepository.FetchVoyageCallback callback){
        new Thread(()-> {
            try {
                List<Voyage> voyages = VoyageDAO.getVoyages();
                callback.onFetch(voyages);
            }catch (IOException | JSONException e){
                callback.onError("Erreur: " + e.getMessage());
            }
        }).start();
    }

    public void fetchVoyagesByFilter(String destination,
                                     String type, String date,
                                     double budget,
                                     VoyageRepository.FetchVoyageByFilterCallback callback){
        new Thread(()-> {
            try {
                List<Voyage> voyages = VoyageDAO.getVoyagesByFilter(destination,type,date, budget);
                callback.onFetch(voyages);
            }catch (IOException | JSONException e){
                callback.onError("Erreur: " + e.getMessage());
            }
        }).start();
    }
}
