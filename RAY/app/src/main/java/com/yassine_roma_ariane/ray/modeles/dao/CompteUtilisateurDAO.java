package com.yassine_roma_ariane.ray.modeles.dao;

import com.yassine_roma_ariane.ray.modeles.CompteUtilisateur;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class CompteUtilisateurDAO {
    public static List<CompteUtilisateur> getComptes() throws IOException, JSONException{
        return new HttpJsonService().getComptes();
    }

    public static CompteUtilisateur authentifierCompte(String courriel, String mdp) throws IOException, JSONException {
        return new HttpJsonService().authentifierCompte(courriel, mdp);
    }

    public static boolean creerCompte(CompteUtilisateur compte) throws IOException,JSONException{
        return new HttpJsonService().creerCompte(compte);
    }


}
