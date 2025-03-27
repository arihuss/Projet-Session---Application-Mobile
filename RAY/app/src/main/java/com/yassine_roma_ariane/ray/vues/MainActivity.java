package com.yassine_roma_ariane.ray.vues;

import static androidx.navigation.Navigation.findNavController;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.yassine_roma_ariane.ray.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                // Vérification des éléments sélectionnés
                if (item.getItemId() == R.id.accueilFragment) {
                    selectedFragment = new AccueilFragment();
                } else if (item.getItemId() == R.id.historiqueFragment) {
                    selectedFragment = new HistoriqueFragment();
                } else if (item.getItemId() == R.id.deconnexion) {
                    Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish(); // Ferme MainActivity après déconnexion
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