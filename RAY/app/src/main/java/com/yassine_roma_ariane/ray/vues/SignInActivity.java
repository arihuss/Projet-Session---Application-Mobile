package com.yassine_roma_ariane.ray.vues;

import android.os.Bundle;
import android.util.Patterns;
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

import com.yassine_roma_ariane.ray.R;
import com.yassine_roma_ariane.ray.modeles.CompteUtilisateur;
import com.yassine_roma_ariane.ray.modeles.dao.CompteUtilisateurDAO;
import com.yassine_roma_ariane.ray.viewModel.CompteUtilisateurViewModel;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText edtPrenom, edtNom, edtTelephone, edtAdresse, edtCourriel, edtMdp, edtMdpConfirme, edtAge;
    private Button btnInscrire;
    private TextView txtRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Liaison avec la vue
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

        // Mettre les écouteurs
        btnInscrire.setOnClickListener(this);
        txtRetour.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnInscrire) {
            // Transformer les entrées en strings
            String prenom = edtPrenom.getText().toString().trim();
            String nom = edtNom.getText().toString().trim();
            String courriel = edtCourriel.getText().toString().trim();
            String strAge = edtAge.getText().toString().trim();
            String tel = edtTelephone.getText().toString().trim();
            String adresse = edtAdresse.getText().toString().trim();
            String mdp = edtMdp.getText().toString().trim();
            String mdpConfirme = edtMdpConfirme.getText().toString().trim();

            // Nous dit si tous les champs sont biens validés
            boolean isValid = true;

            // valider le prenom
            if(prenom.isEmpty()) {
                edtPrenom.setError("Prenom obligatoire");
                isValid = false;
            }

            // valider le nom
            if(nom.isEmpty()) {
                edtNom.setError("Nom obligatoire");
                isValid = false;
            }

            // valider le courriel
            if(courriel.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(courriel).matches()) {
                edtPrenom.setError("Entrer un courriel valide");
                isValid = false;
            }

            // valider l'age
            int age = -1;
            if(strAge.isEmpty()) {
                edtAge.setError("Age obligatoire");
                isValid = false;
            }
            try {
                age = Integer.parseInt(strAge);
            }
            catch(NumberFormatException e) {
                edtAge.setError("Entrer un age valide");
                isValid = false;
            }

            // valider le telephone
            if(tel.isEmpty() || !Patterns.PHONE.matcher(tel).matches()) {
                edtTelephone.setError("Entrer un numéro de téléphone valide");
                isValid = false;
            }

            // valider l'adresse
            if(adresse.isEmpty()) {
                edtAdresse.setError("Adresse obligatoire");
                isValid = false;
            }

            // valider le mot de passe
            if(mdp.isEmpty()) {
                edtMdp.setError("Mot de passe obligatoire");
                isValid = false;
            }
            else if(mdp.length() < 8) {
                edtMdp.setError("Veuillez respecter la longeur minimale de 8 caractères");
                isValid = false;
            }

            // valider la confirmation du mot de passe
            if(mdpConfirme.isEmpty()) {
                edtMdp.setError("Veuillez confirmer votre mot de passe");
                isValid = false;
            }
            // valider que les 2 mots de passes sont pareils
            else if(!mdp.equals(mdpConfirme)) {
                edtMdpConfirme.setError("Les mots de passe ne correspondent pas");
                isValid = false;
            }


            // si toutes les entrées sont validées
            if(isValid) {
                // Création d'un instance compte
                CompteUtilisateur nouvCompte = new CompteUtilisateur(prenom, nom, courriel, mdp, tel, adresse);

                // Création du compte
                CompteUtilisateurViewModel viewModel = new CompteUtilisateurViewModel();
                viewModel.creerCompte(nouvCompte);
                viewModel.refreshComptes();  // besoin de le mettre?
            }
        }
        if(v == txtRetour) {
            finish();
        }
    }
}