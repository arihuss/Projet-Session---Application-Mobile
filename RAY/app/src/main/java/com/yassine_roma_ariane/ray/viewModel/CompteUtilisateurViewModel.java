package com.yassine_roma_ariane.ray.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yassine_roma_ariane.ray.modeles.CompteUtilisateur;
import com.yassine_roma_ariane.ray.modeles.repositories.CompteRepository;

import java.util.List;

public class CompteUtilisateurViewModel extends ViewModel {
    private CompteRepository repository;

    private final MutableLiveData<List<CompteUtilisateur>> comptesMutable = new MutableLiveData<>();
    private final MutableLiveData<String> messageMutable = new MutableLiveData<>();
    private final MutableLiveData<Boolean> estAuthentifier = new MutableLiveData<>(false);

    // ajout dun LiveData<CompteUtilisateur> pour pouvoir recuperer l'id de lutilisateur connecté lors dune reservtaion
    private final MutableLiveData<CompteUtilisateur> utilisateurConnecte = new MutableLiveData<>();
    public LiveData<CompteUtilisateur> getUtilisateurConnecte() {
        return utilisateurConnecte;
    }


    public CompteUtilisateurViewModel() {
        repository = new CompteRepository();
        // Chargement initial des comptes
        refreshComptes();
    }

    public LiveData<List<CompteUtilisateur>> getComptes(){return comptesMutable;}

    public LiveData<String> getMessage() {return messageMutable;}

    public LiveData<Boolean> isAuthentifier(){return estAuthentifier;}

    public void refreshComptes(){
        repository.fetchComptesFromAPI(new CompteRepository.FetchComptesCallback() {
            @Override
            public void onFetch(List<CompteUtilisateur> comptes) {
                comptesMutable.postValue(comptes);
            }
            @Override
            public void onError(String errorMessage) {
                messageMutable.postValue(errorMessage);
            }
        });
    }

    public void creerCompte(CompteUtilisateur compte){
        repository.creerCompte(compte, new CompteRepository.CreerCompteCallback() {
            @Override
            public void onCompteCreer(boolean success) {
                if (success){
                    messageMutable.postValue("Compte creer avec success");
                }
                else{
                    messageMutable.postValue("Echec de creation du compte");
                }
            }

            @Override
            public void onError(String errorMessage) {
                    messageMutable.postValue("Une erreur est survenue: " +errorMessage);
            }
        });
    }

    public void authentifierCompte(String courriel, String mdp){
        repository.authentifierCompte(courriel, mdp, new CompteRepository.AuthentifierCompteCallback() {

            @Override
            public void onAuthentificationReussie(CompteUtilisateur utilisateur) {
                utilisateurConnecte.postValue(utilisateur); // tocke l'utilisateur connecté
                estAuthentifier.postValue(true);
                messageMutable.postValue("Connexion réussie");
            }

            @Override
            public void onEchec() {
                estAuthentifier.postValue(false);
                messageMutable.postValue("Échec de l'authentification");
            }

            @Override
            public void onError(String errorMessage) {
                messageMutable.postValue("Une erreur est survenue : " + errorMessage);
            }
        });
    }

}
