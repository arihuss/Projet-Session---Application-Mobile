package com.yassine_roma_ariane.ray.modeles.dao;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yassine_roma_ariane.ray.modeles.CompteUtilisateur;
import com.yassine_roma_ariane.ray.modeles.Voyage;

import org.json.JSONException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpJsonService {

    private static String URL_POINT_ENTREE = "http://10.0.2.2:3000";

    //REQUETES COMPTES
    public List<CompteUtilisateur> getComptes() throws IOException, JSONException{
        //get les comptes utilisateurs du JSON
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL_POINT_ENTREE+ "/clients")
                .build();

        Response response = okHttpClient.newCall(request).execute();

        ResponseBody responseBody = response.body();
        String jsonStr = responseBody.string();
        List<CompteUtilisateur> comptes = null;

        Log.d("HttpJsonService:getComptes",jsonStr);

        if(jsonStr.length()>0){
            ObjectMapper mapper = new ObjectMapper();
            try {
                comptes = Arrays.asList(mapper.readValue(jsonStr,CompteUtilisateur[].class));
            }catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return comptes;
        }
        return null;
    }

    public boolean creerCompte(CompteUtilisateur utilisateur) throws IOException,JSONException {
        //creer un compte et le mettre dans le JSON
        return true;
    }

    public boolean authentifierCompte(String courriel, String mdp) throws IOException, JSONException{
        List<CompteUtilisateur> comptes = getComptes();

        for (CompteUtilisateur compte : comptes) {
            if (compte.getEmail().equals(courriel) && compte.getMdp().equals(mdp)){
                return true;
            }
        }
        return false;
    }

    //REQUETES VOYAGES
    public List<Voyage> getVoyages() throws IOException, JSONException{
        //get tout les voyages pour l'afficher a l'accueil
        return null;
    }


    //finir requetes voyages

}
