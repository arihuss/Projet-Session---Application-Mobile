package com.yassine_roma_ariane.ray.modeles;

public class Reservation {
    private int id;
    private int voyageId;
    private int clientId;
    private String date;
    private int nbPersonnes;
    private String statut; // "confirmée", "annulée"

    public Reservation() {}

    public Reservation(int id, int voyageId, int clientId, String date, int nbPersonnes, String statut) {
        this.id = id;
        this.voyageId = voyageId;
        this.clientId = clientId;
        this.date = date;
        this.nbPersonnes = nbPersonnes;
        this.statut = statut;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(int voyageId) {
        this.voyageId = voyageId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNbPersonnes() {
        return nbPersonnes;
    }

    public void setNbPersonnes(int nbPersonnes) {
        this.nbPersonnes = nbPersonnes;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
