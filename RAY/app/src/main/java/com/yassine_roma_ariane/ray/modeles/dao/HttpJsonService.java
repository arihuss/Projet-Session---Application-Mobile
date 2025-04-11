package com.yassine_roma_ariane.ray.modeles.dao;

import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yassine_roma_ariane.ray.modeles.CompteUtilisateur;
import com.yassine_roma_ariane.ray.modeles.Trip;
import com.yassine_roma_ariane.ray.modeles.Voyage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpJsonService {

    private static String URL_POINT_ENTREE = "http://10.0.2.2:3000";

    //REQUETES COMPTES

    /**
     * Recupere tous les comptes du Json
     * @return une liste des comptes
     * @throws IOException
     * @throws JSONException
     */
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

    /**
     * Creer un compte et le rajoute dans le Json lors de l'inscription
     * @param utilisateur
     * @return true si l'inscription a été faite avec succes, et false si une erreur s'est passée
     * @throws IOException
     * @throws JSONException
     */
    public boolean creerCompte(CompteUtilisateur utilisateur) throws IOException,JSONException {
        //creer un compte et le mettre dans le JSON
        OkHttpClient client = new OkHttpClient();
        JSONObject obj = new JSONObject();


        obj.put("prenom", utilisateur.getPrenom());
        obj.put("nom", utilisateur.getNom());
        obj.put("age", utilisateur.getAge());
        obj.put("email", utilisateur.getEmail());
        obj.put("adresse", utilisateur.getAdresse());
        obj.put("telephone", utilisateur.getTelephone());
        obj.put("mdp", utilisateur.getMdp());

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(String.valueOf(obj), JSON);
        Request request = new Request.Builder()
                .url(URL_POINT_ENTREE + "/clients")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        return response.isSuccessful();
    }

    /**
     * Authentifier le compte qui retourne un Compte afin d'avoir acces a l'id du compte lors dune reservation
     * @param courriel courriel de l'utilisateur
     * @param mdp mdp de l'utilisateur
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public CompteUtilisateur authentifierCompte(String courriel, String mdp) throws IOException, JSONException {
        List<CompteUtilisateur> comptes = getComptes();

        for (CompteUtilisateur compte : comptes) {
            if (compte.getEmail().equalsIgnoreCase(courriel) && compte.getMdp().equals(mdp)) {
                return compte;
            }
        }
        return null;
    }


    //REQUETES VOYAGES

    /**
     * Récupère les voyages du Json
     * @return une liste des voyages
     * @throws IOException
     * @throws JSONException
     */
    public List<Voyage> getVoyages() throws IOException, JSONException{
        //get tous les voyages pour la prochaine fonction
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL_POINT_ENTREE+ "/voyages")
                .build();

        Response response = okHttpClient.newCall(request).execute();

        ResponseBody responseBody = response.body();
        String jsonStr = responseBody.string();
        List<Voyage> voyages = null;


        if(jsonStr.length()>0){
            ObjectMapper mapper = new ObjectMapper();
            try {
                voyages = Arrays.asList(mapper.readValue(jsonStr,Voyage[].class));
            }catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return voyages;
        }
        return null;
    }

    /**
     * Récupère les voyages filtrés selon les filtres rentrées
     * @param destination filtre de destination
     * @param type filtre de type
     * @param date filtre de date
     * @param budget filtre de budget
     * @return Une liste des voyages filtrés
     * @throws IOException
     * @throws JSONException
     */
    public List<Voyage> getVoyagesByFilter(String destination, String type, String date, double budget) throws IOException, JSONException {
        List<Voyage> tousLesVoyages = getVoyages(); // récupère tous les voyages
        List<Voyage> voyagesFiltres = new ArrayList<>();

        if (tousLesVoyages == null) {
            return voyagesFiltres; // retourne une liste vide
        }

        for (Voyage voyage : tousLesVoyages) {
            boolean correspond = true;

            // Vérifier chaque filtre un par un, s'il est défini
            if (destination != null && !destination.isEmpty()) {
                if (!voyage.getDestination().equalsIgnoreCase(destination)) {
                    correspond = false;
                }
            }

            if (type != null && !type.isEmpty()) {
                if (!voyage.getType_de_voyage().equalsIgnoreCase(type)) {
                    correspond = false;
                }
            }

            if (date != null && !date.isEmpty()) {
                boolean dateTrouvee = false;

                for (Trip trip : voyage.getTrips()) {
                    if (trip.getDate().equals(date)) { // Vérifie si un des trips correspond à la date
                        dateTrouvee = true;
                        break; // On arrête dès qu'on trouve une correspondance
                    }
                }

                if (!dateTrouvee) {
                    correspond = false; // Exclut ce voyage si aucun trip ne correspond
                }
            }


                if (voyage.getPrix() > budget) {
                    correspond = false;
                }


            // Si tous les filtres passent, ajouter à la liste
            if (correspond) {
                voyagesFiltres.add(voyage);
            }
        }

        return voyagesFiltres;
    }

    /**
     * Changer le nombre de places disponibles d'un voyage
     * @param voyage Le voyage à changer
     * @return true si le changement a été fait avec success, false sinon
     * @throws IOException
     * @throws JSONException
     */
    public boolean updateVoyage(Voyage voyage) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(voyage);
        RequestBody requestBody = RequestBody.create(json, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url("http://10.0.2.2:3000/voyages/" + voyage.getId())
                .put(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        return response.isSuccessful();
    }
}
