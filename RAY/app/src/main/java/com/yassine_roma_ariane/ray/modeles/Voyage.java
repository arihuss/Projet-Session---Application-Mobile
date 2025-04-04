package com.yassine_roma_ariane.ray.modeles;

import java.util.List;

public class Voyage {
    private int id;
    private String nom_voyage;
    private String description;
    private double prix;
    private String destination;
    private String image_url;
    private int duree_jours;
    private List<Trip> trips;
    private String type_de_voyage;
    private String activites_incluses;

    // Constructor
    public Voyage(int id, String nom_voyage, String description, double prix,
                  String destination, String image_url, int duree_jours,
                  List<Trip> trips, String type_de_voyage, String activites_incluses) {
        this.id = id;
        this.nom_voyage = nom_voyage;
        this.description = description;
        this.prix = prix;
        this.destination = destination;
        this.image_url = image_url;
        this.duree_jours = duree_jours;
        this.trips = trips;
        this.type_de_voyage = type_de_voyage;
        this.activites_incluses = activites_incluses;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomVoyage() {
        return nom_voyage;
    }

    public void setNomVoyage(String nom_voyage) {
        this.nom_voyage = nom_voyage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String image_url) {
        this.image_url = image_url;
    }

    public int getDureeJours() {
        return duree_jours;
    }

    public void setDureeJours(int duree_jours) {
        this.duree_jours = duree_jours;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public String getTypeDeVoyage() {
        return type_de_voyage;
    }

    public void setTypeDeVoyage(String type_de_voyage) {
        this.type_de_voyage = type_de_voyage;
    }

    public String getActivitesIncluses() {
        return activites_incluses;
    }

    public void setActivitesIncluses(String activites_incluses) {
        this.activites_incluses = activites_incluses;
    }
}