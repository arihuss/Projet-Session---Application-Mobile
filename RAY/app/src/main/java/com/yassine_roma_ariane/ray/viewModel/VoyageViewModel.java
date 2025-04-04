package com.yassine_roma_ariane.ray.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yassine_roma_ariane.ray.modeles.Voyage;

import com.yassine_roma_ariane.ray.modeles.repositories.VoyageRepository;

import java.util.List;

public class VoyageViewModel extends ViewModel {

    private VoyageRepository repository;

    private final MutableLiveData<List<Voyage>> voyagesMutable = new MutableLiveData<>();
    private final MutableLiveData<List<Voyage>> voyagesFiltre = new MutableLiveData<>();
    private final MutableLiveData<String> messageMutable = new MutableLiveData<>();

    public VoyageViewModel() {
        repository = new VoyageRepository();
        // Chargement initial des comptes
        refreshVoyages();
    }

    public LiveData<List<Voyage>> getVoyages(){return voyagesMutable;}

    public LiveData<String> getMessage() {return messageMutable;}

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



}
