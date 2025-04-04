package com.yassine_roma_ariane.ray.vues;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.yassine_roma_ariane.ray.R;

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

        }
        if(v == txtRetour) {

        }
    }
}