package com.yassine_roma_ariane.ray.modeles;

public class Trip {
    private String date;
    private int nb_places_disponibles;

    // Constructor
    public Trip(String date, int nb_places_disponibles) {
        this.date = date;
        this.nb_places_disponibles = nb_places_disponibles;
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNbPlacesDisponibles() {
        return nb_places_disponibles;
    }

    public void setNbPlacesDisponibles(int nb_places_disponibles) {
        this.nb_places_disponibles = nb_places_disponibles;
    }
}