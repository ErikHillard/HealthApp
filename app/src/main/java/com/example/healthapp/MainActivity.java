package com.example.healthapp;

import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Home");

        getFragment(new ConsumptionHomeFragment(getFilesDir()));
        getFragment(new HomeFragment());
        Bundle extras = getIntent().getExtras();

        // TODO: Write these to json only if needed.
        if (extras != null) {
            String value = extras.getString("key");
        } else {
            // TODO: Set default parameters for a user.
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportActionBar().setTitle("Home");
                        return getFragment(new HomeFragment());

                    case R.id.consumption:
                        getSupportActionBar().setTitle("Consumption");
                        return getFragment(new ConsumptionHomeFragment(getFilesDir()));

                    case R.id.run:
                        getSupportActionBar().setTitle("Exercise");
                        return getFragment(new ExerciseFragment(getFilesDir()));

                    case R.id.idea:
                        getSupportActionBar().setTitle("Ideas");
                        return getFragment(new IdeaFragment(getFilesDir()));
                }

                return false;
            }
        });
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, HomeFragment.class, null)
                    .commit();
        }


    }

    private boolean getFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, fragment);
        fragmentTransaction.commit();
        return true;
    }


}
