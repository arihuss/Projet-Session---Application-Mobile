package com.yassine_roma_ariane.ray.vues;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.SharedPreferences;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yassine_roma_ariane.ray.R;
import com.yassine_roma_ariane.ray.modeles.Reservation;
import com.yassine_roma_ariane.ray.modeles.Voyage;
import com.yassine_roma_ariane.ray.viewModel.ReservationViewModel;
import com.yassine_roma_ariane.ray.vues.adaptateurs.HistoriqueAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoriqueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoriqueFragment extends Fragment implements AdapterView.OnItemClickListener {
    // Déclaration des variables
    private ListView lvReservations;
    private HistoriqueAdapter adaptateur;
    private ReservationViewModel viewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoriqueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoriqueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoriqueFragment newInstance(String param1, String param2) {
        HistoriqueFragment fragment = new HistoriqueFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historique, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Liaison avec la vue (fragment_historique)
        lvReservations = view.findViewById(R.id.lvReservations);

        // Mettre l'adaptateur
        adaptateur = new HistoriqueAdapter(getActivity(), R.layout.activity_historique_adapter);
        lvReservations.setAdapter(adaptateur);

        // Set-up du viewModel
        viewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        viewModel.getReservations().observe(getViewLifecycleOwner(), new Observer<List<Reservation>>() {
            @Override
            public void onChanged(List<Reservation> reservations) {
                if (reservations != null){
                    adaptateur.setReservations(reservations);
                    //setListViewHeightBasedOnChildren(lvVoyages);
                    Toast.makeText(getActivity(), reservations.size() + " réservations chargés", Toast.LENGTH_SHORT).show();
                }else {
                    //Si la liste est nulle, un Toast indique une erreur de chargement.
                    Toast.makeText(getActivity(), "Erreur de chargement des réservations", Toast.LENGTH_SHORT).show();
                }
            }
        });


        String clientId = getSharedPreferences("session", MODE_PRIVATE).getString("clientId", null);
        viewModel.loadReservationsPourClient(clientId);


    }


    // TODO: Gestion du click sur un item de la liste, pour amener à la page de l'événement ??
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}