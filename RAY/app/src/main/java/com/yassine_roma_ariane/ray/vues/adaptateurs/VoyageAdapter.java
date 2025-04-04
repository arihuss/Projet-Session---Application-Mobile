package com.yassine_roma_ariane.ray.vues.adaptateurs;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.yassine_roma_ariane.ray.R;

public class VoyageAdapter extends AppCompatActivity {

    private TextView txtTitre;
    private TextView txtHeaderPrix;
    private ImageView imgVoyage;
    private TextView txtDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_voyage_adapter);

        txtTitre = findViewById(R.id.txtTitre);
        txtHeaderPrix = findViewById(R.id.txtHeaderPrix);
        imgVoyage = findViewById(R.id.imgVoyage);
        txtDesc = findViewById(R.id.txtDesc);
    }
}