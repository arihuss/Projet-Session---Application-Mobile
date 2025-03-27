package com.yassine_roma_ariane.ray.modeles.repositories;

import android.util.Log;

import com.yassine_roma_ariane.ray.modeles.CompteUtilisateur;
import com.yassine_roma_ariane.ray.modeles.dao.CompteUtilisateurDAO;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class CompteRepository {

    public interface FetchComptesCallback {
        void onFetch(List<CompteUtilisateur> comptes);
        void onError(String errorMessage);
    }

    public interface CreerCompteCallback {
        void onCompteCreer(boolean success);
        void onError(String errorMessage);
    }

    public interface AuthentiferCompteCallback {
        void onAuthentificationReussie(boolean success);
        void onError(String errorMessage);
    }

    public void fetchComptesFromAPI(FetchComptesCallback callback){
        new Thread(()-> {
            try {
                List<CompteUtilisateur> comptes = CompteUtilisateurDAO.getComptes();
                callback.onFetch(comptes);
            }catch (IOException | JSONException e){
                callback.onError("Erreur: " + e.getMessage());
            }
        }).start();
    }

    public void creerCompte(CompteUtilisateur compte, CreerCompteCallback callback){
        new Thread(()-> {
            try {
                boolean success = CompteUtilisateurDAO.creerCompte(compte);
                callback.onCompteCreer(success);
            }catch (IOException | JSONException e) {
                callback.onError("Erreur : " + e.getMessage());
            }
        }).start();
    }

    public void authentifierCompte(String courriel, String mdp, AuthentiferCompteCallback callback){
        new Thread(()-> {
            try{
                boolean success = CompteUtilisateurDAO.authentifierCompte(courriel,mdp);
                callback.onAuthentificationReussie(success);
            }catch (IOException| JSONException e) {
                Log.e("CompteRepository","Erreur d'authenfication",e);
                callback.onError("Erreur: " + e.getMessage());
            }
        });
    }
}
