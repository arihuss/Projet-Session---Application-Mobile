package com.yassine_roma_ariane.ray.modeles;

public class CompteUtilisateur {
    private int age;
    private String id; //changé de int à String
    private String nom, prenom, email, mdp, telephone, adresse;

    public CompteUtilisateur(String prenom, String nom, String email, String mdp, String telephone, String adresse, int age) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.mdp = mdp;
        this.telephone = telephone;
        this.adresse = adresse;
        this.age = age;
    }

    public CompteUtilisateur() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { // ✅ changé en String
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}