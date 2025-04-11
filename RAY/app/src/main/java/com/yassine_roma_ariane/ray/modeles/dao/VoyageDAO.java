package com.yassine_roma_ariane.ray.modeles.dao;

/**
 * Ce DAO constitue de fonctions qui appellent ceux du HttpJsonService en lien avec les voyages
 */

import com.yassine_roma_ariane.ray.modeles.Voyage;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class VoyageDAO {

    public static List<Voyage> getVoyages() throws IOException, JSONException {
        return new HttpJsonService().getVoyages();
    }
    public static List<Voyage> getVoyagesByFilter(String destination, String type, String date, double budget) throws IOException, JSONException {
        return new HttpJsonService().getVoyagesByFilter(destination, type, date, budget);
    }
}
