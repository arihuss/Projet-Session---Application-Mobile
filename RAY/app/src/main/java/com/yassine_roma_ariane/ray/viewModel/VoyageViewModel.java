package com.yassine_roma_ariane.ray.viewModel;

/**
 * Appelle les fonctions du repository sur les voyages
 */

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yassine_roma_ariane.ray.modeles.Trip;
import com.yassine_roma_ariane.ray.modeles.Voyage;

import com.yassine_roma_ariane.ray.modeles.repositories.VoyageRepository;

import java.util.ArrayList;
import java.util.List;

public class VoyageViewModel extends ViewModel {

    private VoyageRepository repository;

    private final MutableLiveData<List<Voyage>> voyagesMutable = new MutableLiveData<>();
    private final MutableLiveData<String> messageMutable = new MutableLiveData<>();
    private final MutableLiveData<List<String>> destinations = new MutableLiveData<>();
    private final MutableLiveData<List<String>> typesVoyages = new MutableLiveData<>();
    private final MutableLiveData<List<String>> datesVoyages = new MutableLiveData<>();


    public VoyageViewModel() {
        repository = new VoyageRepository();
        // Chargement initial des comptes
        refreshVoyages();
    }

    public LiveData<List<Voyage>> getVoyages(){return voyagesMutable;}

    public LiveData<String> getMessage() {return messageMutable;}

    public LiveData<List<String>> getDestinations() {return destinations;}

    public LiveData<List<String>> getTypesVoyages(){return typesVoyages;}

    public LiveData<List<String>> getDatesVoyages(){return datesVoyages;}

    public void filterVoyages(String destination, String type, String date, double budget) {
        repository.fetchVoyagesByFilter(destination, type, date, budget, new VoyageRepository.FetchVoyageByFilterCallback() {
            @Override
            public void onFetch(List<Voyage> voyages) {
                voyagesMutable.postValue(voyages);
            }
            @Override
            public void onError(String errorMessage) {
                messageMutable.postValue(errorMessage);
            }
        });
    }


    public void refreshVoyages(){
        repository.fetchVoyagesFromAPI(new VoyageRepository.FetchVoyageCallback() {
            @Override
            public void onFetch(List<Voyage> voyages) {
                voyagesMutable.postValue(voyages);
            }
            @Override
            public void onError(String errorMessage) {
                messageMutable.postValue(errorMessage);
            }
        });
    }

    public void findDestinations(){
        repository.fetchVoyagesFromAPI(new VoyageRepository.FetchVoyageCallback() {

            @Override
            public void onFetch(List<Voyage> voyages) {
                List<String> listeDestinations = new ArrayList<>();
                for (Voyage voyage : voyages){
                    String dest = voyage.getDestination();
                    if (!listeDestinations.contains(dest)) {
                        listeDestinations.add(dest); // éviter les doublons
                    }
                }
                destinations.postValue(listeDestinations);
            }

            @Override
            public void onError(String errorMessage) {
                    messageMutable.postValue(errorMessage);
            }
        });
    }

    public void findTypesVoyages(){
        repository.fetchVoyagesFromAPI(new VoyageRepository.FetchVoyageCallback() {

            @Override
            public void onFetch(List<Voyage> voyages) {
                List<String> listeTypesVoyages = new ArrayList<>();
                listeTypesVoyages.add("Tous");
                for (Voyage voyage : voyages){
                    String type = voyage.getType_de_voyage();
                    if (!listeTypesVoyages.contains(type)) {
                        listeTypesVoyages.add(type); // éviter les doublons
                    }
                }
                typesVoyages.postValue(listeTypesVoyages);
            }

            @Override
            public void onError(String errorMessage) {
                messageMutable.postValue(errorMessage);
            }
        });

    }

    public void findDatesVoyages(){
        repository.fetchVoyagesFromAPI(new VoyageRepository.FetchVoyageCallback() {

            @Override
            public void onFetch(List<Voyage> voyages) {
                List<String> listeDatesVoyages = new ArrayList<>();
                listeDatesVoyages.add("Tous");
                for (Voyage voyage : voyages){
                    for (Trip trip : voyage.getTrips()) {
                        String date = trip.getDate();
                        if (!listeDatesVoyages.contains(date)) {
                            listeDatesVoyages.add(date); // éviter les doublons
                        }
                    }
                }
                datesVoyages.postValue(listeDatesVoyages);
            }

            @Override
            public void onError(String errorMessage) {
                messageMutable.postValue(errorMessage);
            }
        });
    }

    public void rendrePlacesDisponibles(int voyageId, String date, int nbPlaces) {
        repository.rendrePlacesDisponibles(voyageId, date, nbPlaces, new VoyageRepository.UpdatePlacesCallback() {
            @Override
            public void onSuccess() {
                messageMutable.postValue("Places remises à disposition.");
                refreshVoyages(); // facultatif : recharge les données
            }

            @Override
            public void onFailure(String errorMessage) {
                messageMutable.postValue("Échec de la mise à jour des places : " + errorMessage);
            }
        });
    }


    public LiveData<Voyage> getVoyageParId(int id) {
        MutableLiveData<Voyage> voyageLiveData = new MutableLiveData<>();
        List<Voyage> listeVoyages = voyagesMutable.getValue();

        if (listeVoyages != null) {
            for (Voyage v : listeVoyages) {
                if (v.getId() == id) {
                    voyageLiveData.setValue(v);
                    return voyageLiveData;
                }
            }
        }

        // Si la liste est vide ou le voyage introuvable, on peut essayer de recharger les données
        repository.fetchVoyagesFromAPI(new VoyageRepository.FetchVoyageCallback() {
            @Override
            public void onFetch(List<Voyage> voyages) {
                for (Voyage v : voyages) {
                    if (v.getId() == id) {
                        voyageLiveData.postValue(v);
                        return;
                    }
                }
                messageMutable.postValue("Voyage introuvable.");
            }

            @Override
            public void onError(String errorMessage) {
                messageMutable.postValue("Erreur de chargement du voyage : " + errorMessage);
            }
        });

        return voyageLiveData;
    }

}
