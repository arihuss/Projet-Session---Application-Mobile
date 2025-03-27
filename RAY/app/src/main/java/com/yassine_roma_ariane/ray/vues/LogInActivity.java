package com.yassine_roma_ariane.ray.vues;

import android.content.Intent;
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

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtCourriel;
    private EditText edtMdp;
    private Button btnSeConnecter;
    private TextView txtCreerCompte;
    private TextView txtPasDeCompte;

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
    }

    @Override
    public void onClick(View v) {
        if (v==btnSeConnecter){

        }
        if (v==txtCreerCompte || v==txtPasDeCompte){
            Intent intent = new Intent(LogInActivity.this, SignInActivity.class);
            startActivity(intent);
        }
    }
}