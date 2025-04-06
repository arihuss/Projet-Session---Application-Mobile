package com.yassine_roma_ariane.ray.vues;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.yassine_roma_ariane.ray.R;
import com.yassine_roma_ariane.ray.modeles.CompteUtilisateur;
import com.yassine_roma_ariane.ray.viewModel.CompteUtilisateurViewModel;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtPrenom, edtNom, edtTelephone, edtAdresse, edtCourriel, edtMdp, edtMdpConfirme, edtAge;
    private Button btnInscrire;
    private TextView txtRetour;
    private CompteUtilisateurViewModel viewModel;

    private String courrielTemp, mdpTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Vue
        edtPrenom = findViewById(R.id.edtPrenom);
        edtNom = findViewById(R.id.edtNom);
        edtCourriel = findViewById(R.id.edtCourriel);
        edtTelephone = findViewById(R.id.edtTelephone);
        edtAdresse = findViewById(R.id.edtAdresse);
        edtAge = findViewById(R.id.edtAge);
        edtMdp = findViewById(R.id.edtMdp);
        edtMdpConfirme = findViewById(R.id.edtMdpConfirme);
        btnInscrire = findViewById(R.id.btnInscrire);
        txtRetour = findViewById(R.id.txtRetour);

        btnInscrire.setOnClickListener(this);
        txtRetour.setOnClickListener(this);

        // ViewModel
        viewModel = new ViewModelProvider(this).get(CompteUtilisateurViewModel.class);

        // Observer les messages du ViewModel
        viewModel.getMessage().observe(this, message -> {
            Toast.makeText(SignInActivity.this, message, Toast.LENGTH_SHORT).show();
            if ("Compte créé avec succès".equals(message)) {
                if (courrielTemp != null && mdpTemp != null) {
                    viewModel.authentifierCompte(courrielTemp, mdpTemp);
                }
            }
        });

        // Observer utilisateur connecté pour rediriger
        viewModel.getUtilisateurConnecte().observe(this, utilisateur -> {
            if (utilisateur != null) {
                // Sauvegarde ID utilisateur connecté
                getSharedPreferences("session", MODE_PRIVATE)
                        .edit()
                        .putString("clientId", utilisateur.getId())
                        .apply();

                // Aller à l'accueil
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnInscrire) {
            String prenom = edtPrenom.getText().toString().trim();
            String nom = edtNom.getText().toString().trim();
            String courriel = edtCourriel.getText().toString().trim();
            String strAge = edtAge.getText().toString().trim();
            String tel = edtTelephone.getText().toString().trim();
            String adresse = edtAdresse.getText().toString().trim();
            String mdp = edtMdp.getText().toString().trim();
            String mdpConfirme = edtMdpConfirme.getText().toString().trim();

            boolean isValid = true;
            int age = -1;

            if (prenom.isEmpty()) {
                edtPrenom.setError("Prénom obligatoire");
                isValid = false;
            }

            if (nom.isEmpty()) {
                edtNom.setError("Nom obligatoire");
                isValid = false;
            }

            if (courriel.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(courriel).matches()) {
                edtCourriel.setError("Courriel invalide");
                isValid = false;
            }

            if (strAge.isEmpty()) {
                edtAge.setError("Âge obligatoire");
                isValid = false;
            } else {
                try {
                    age = Integer.parseInt(strAge);
                } catch (NumberFormatException e) {
                    edtAge.setError("Âge invalide");
                    isValid = false;
                }
            }

            if (tel.isEmpty() || !Patterns.PHONE.matcher(tel).matches()) {
                edtTelephone.setError("Téléphone invalide");
                isValid = false;
            }

            if (adresse.isEmpty()) {
                edtAdresse.setError("Adresse obligatoire");
                isValid = false;
            }

            if (mdp.isEmpty()) {
                edtMdp.setError("Mot de passe obligatoire");
                isValid = false;
            } else if (mdp.length() < 8) {
                edtMdp.setError("Minimum 8 caractères");
                isValid = false;
            }

            if (mdpConfirme.isEmpty()) {
                edtMdpConfirme.setError("Confirmation requise");
                isValid = false;
            } else if (!mdp.equals(mdpConfirme)) {
                edtMdpConfirme.setError("Les mots de passe ne correspondent pas");
                isValid = false;
            }

            if (isValid) {
                courrielTemp = courriel;
                mdpTemp = mdp;
                CompteUtilisateur compte = new CompteUtilisateur(prenom, nom, courriel, mdp, tel, adresse, age);
                viewModel.creerCompte(compte);
            }
        }

        if (v == txtRetour) {
            finish();
        }
    }
}
