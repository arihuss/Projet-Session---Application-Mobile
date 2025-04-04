package com.yassine_roma_ariane.ray.vues;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yassine_roma_ariane.ray.R;
import com.yassine_roma_ariane.ray.modeles.Voyage;
import com.yassine_roma_ariane.ray.viewModel.CompteUtilisateurViewModel;
import com.yassine_roma_ariane.ray.viewModel.VoyageViewModel;
import com.yassine_roma_ariane.ray.vues.adaptateurs.VoyageAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccueilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccueilFragment extends Fragment {

    private AutoCompleteTextView autoTxtDestination;
    private Spinner spType;
    private Spinner spDate;
    private SeekBar sbBudget;
    private TextView txtPrix;
    private Button btnRechercher;
    private ListView lvVoyages;
    private VoyageAdapter adaptateur;
    private VoyageViewModel viewModel;

    private ActivityResultLauncher<Intent> goToDetails;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccueilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccueilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccueilFragment newInstance(String param1, String param2) {
        AccueilFragment fragment = new AccueilFragment();
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
        return inflater.inflate(R.layout.fragment_accueil, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        autoTxtDestination = view.findViewById(R.id.autoTxtDestination); // Replace with your actual ID
        spType = view.findViewById(R.id.spType); // Replace with your actual ID
        spDate = view.findViewById(R.id.spDate); // Replace with your actual ID
        sbBudget = view.findViewById(R.id.sbBudget); // Replace with your actual ID
        txtPrix = view.findViewById(R.id.txtPrix); // Replace with your actual ID
        btnRechercher = view.findViewById(R.id.btnRechercher); // Replace with your actual ID
        lvVoyages = view.findViewById(R.id.lvVoyages); // Replace with your actual ID

        goToDetails =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {

                    }
                });
        adaptateur = new VoyageAdapter(getActivity(),R.layout.activity_voyage_adapter);

        lvVoyages.setAdapter(adaptateur);

        viewModel = new ViewModelProvider(this).get(VoyageViewModel.class);
        viewModel.getVoyages().observe(getViewLifecycleOwner(), new Observer<List<Voyage>>() {
            @Override
            public void onChanged(List<Voyage> voyages) {
                if (voyages != null){
                    adaptateur.setVoyages(voyages);
                    Toast.makeText(getActivity(), voyages.size() + " voyages chargés", Toast.LENGTH_SHORT).show();
                }else {
                    //Si la liste est nulle, un Toast indique une erreur de chargement.
                    Toast.makeText(getActivity(), "Erreur de chargement des comptes", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            //Chaque fois qu'un nouveau message est publié comme LiveData, onChanged est appelée.
            @Override
            public void onChanged(String message) {
                if (message != null) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        lvVoyages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Voyage voyage = adaptateur.getItem(position);
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("ID_VOYAGE", voyage.getId());
                goToDetails.launch(intent);
            }
        });

        viewModel.getVoyages();
    }

}