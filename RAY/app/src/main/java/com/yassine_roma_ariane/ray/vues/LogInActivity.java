package com.yassine_roma_ariane.ray.vues;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.yassine_roma_ariane.ray.R;
import com.yassine_roma_ariane.ray.viewModel.CompteUtilisateurViewModel;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtCourriel;
    private EditText edtMdp;
    private Button btnSeConnecter;
    private TextView txtCreerCompte;
    private TextView txtPasDeCompte;
    private CompteUtilisateurViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        edtCourriel=findViewById(R.id.edtCourriel);
        edtMdp=findViewById(R.id.edtMdp);
        btnSeConnecter=findViewById(R.id.btnSeConnecter);
        txtCreerCompte=findViewById(R.id.txtCreerCompte);
        txtPasDeCompte=findViewById(R.id.txtPasDeCompte);
        btnSeConnecter.setOnClickListener(this);
        txtPasDeCompte.setOnClickListener(this);
        txtCreerCompte.setOnClickListener(this);

        viewModel = new ViewModelProvider(this).get(CompteUtilisateurViewModel.class);

        // Observer les messages d'erreur/succès
        viewModel.getMessage().observe(this, message -> {
            Toast.makeText(LogInActivity.this, message, Toast.LENGTH_SHORT).show();
        });

        // Observer l'état d'authentification
        viewModel.isAuthentifier().observe(this, isAuthenticated -> {
            if (isAuthenticated) {
                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                intent.putExtra("authenticated", true);  // Indiquer que l'authentification a réussi
                startActivity(intent);
                finish();
            }
        });

        // recuperation de lutilisateur connecte pour avoir son id qui sera stocké dans shared preference
        // afin de pouvoir realiser les insertions dans la DB lors dune reservation
        viewModel.getUtilisateurConnecte().observe(this, utilisateur -> {
            if (utilisateur != null) {
                getSharedPreferences("session", MODE_PRIVATE)
                        .edit()
                        .putString("clientId", utilisateur.getId())
                        .apply();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v==btnSeConnecter){
           if (!edtMdp.getText().toString().isEmpty()&&!edtCourriel.getText().toString().isEmpty()){
               String courriel = edtCourriel.getText().toString();
               String mdp = edtMdp.getText().toString();
               viewModel.authentifierCompte(courriel,mdp);

           }else{
               Toast.makeText(LogInActivity.this,"Veuillez remplir les champs",Toast.LENGTH_SHORT).show();
           }
        }
        if (v == txtCreerCompte || v == txtPasDeCompte) {
            Intent intent = new Intent(LogInActivity.this, SignInActivity.class);
            startActivity(intent);
        }

    }
}