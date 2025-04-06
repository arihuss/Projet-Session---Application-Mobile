package com.yassine_roma_ariane.ray.vues;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yassine_roma_ariane.ray.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lire l'ID du client depuis les SharedPreferences
        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        String clientId = prefs.getString("clientId", null);

        // Si aucun client connecté et c'est le premier lancement
        if (clientId == null && savedInstanceState == null) {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // Charger AccueilFragment par défaut
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, new AccueilFragment())
                    .commit();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.accueilFragment);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.accueilFragment) {
                    selectedFragment = new AccueilFragment();
                } else if (item.getItemId() == R.id.historiqueFragment) {
                    selectedFragment = new HistoriqueFragment();
                } else if (item.getItemId() == R.id.deconnexion) {
                    // Efface la session
                    getSharedPreferences("session", MODE_PRIVATE)
                            .edit()
                            .remove("clientId")
                            .apply();

                    Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish(); // Fermer MainActivity après déconnexion
                    return true;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, selectedFragment)
                            .commit();
                }
                return true;
            }
        });
    }
}
