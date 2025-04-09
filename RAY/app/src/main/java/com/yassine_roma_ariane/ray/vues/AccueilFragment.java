package com.yassine_roma_ariane.ray.vues;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
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
public class AccueilFragment extends Fragment implements View.OnClickListener {

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
        lvVoyages.setNestedScrollingEnabled(false);
        btnRechercher.setOnClickListener(this);
        txtPrix.setText(sbBudget.getProgress()+"$");



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
                    lvVoyages.post(new Runnable() {
                        @Override
                        public void run() {
                            setListViewHeightBasedOnChildren(lvVoyages);
                        }
                    });
                    Toast.makeText(getActivity(), voyages.size() + " voyages chargés", Toast.LENGTH_SHORT).show();
                }else {
                    //Si la liste est nulle, un Toast indique une erreur de chargement.
                    Toast.makeText(getActivity(), "Erreur de chargement des voyages", Toast.LENGTH_SHORT).show();
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

        viewModel.getDestinations().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> destinationList) {
                ArrayAdapter<String> autoAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, destinationList);
                autoTxtDestination.setAdapter(autoAdapter);
                autoTxtDestination.setThreshold(1);
            }
        });

        viewModel.getTypesVoyages().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> typesList) {
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, typesList);
                spType.setAdapter(spinnerAdapter);
                spType.setSelection(0);
            }
        });

        viewModel.getDatesVoyages().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> datesList) {
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, datesList);
                spDate.setAdapter(spinnerAdapter);
                spDate.setSelection(0);
            }
        });

        sbBudget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // This method is called whenever the progress level is changed
                // Update the TextView with the current progress value
                txtPrix.setText(String.valueOf(progress)+"$");

                // If you want to format the value (e.g., as currency):
                // valueTextView.setText("$" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Called when the user starts interacting with the SeekBar
                // Optional implementation
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Called when the user stops interacting with the SeekBar
                // Optional implementation
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
        viewModel.findDestinations();
        viewModel.findTypesVoyages();
        viewModel.findDatesVoyages();


    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() -350;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onClick(View v) {

        double budget = sbBudget.getProgress();
        String destination = null;
        String type = null;
        String date = null;
        if (!autoTxtDestination.getText().toString().isEmpty()){
        destination = autoTxtDestination.getText().toString();}
        date = spDate.getSelectedItem().toString();
        type = spType.getSelectedItem().toString();
        if ("Tous".equals(type)) type = null;
        if ("Tous".equals(date)) date = null;

        viewModel.filterVoyages(destination,type,date,budget);
    }
}

