package com.example.taxiapp.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.driver_additional_info.DriverAdditionalInfoFragment;
import com.example.taxiapp.ui.driver_profile.DriverProfileFragment;
import com.example.taxiapp.ui.estimate_route.EstimateRouteFragment;
import com.example.taxiapp.ui.home.HomeFragment;
import com.example.taxiapp.ui.login.LoginFragment;
import com.example.taxiapp.ui.register.RegisterFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        findViewById(R.id.menu_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.END)
        );

        setupMenu();
        loadFragment(new HomeFragment(), false);
    }

    private void setupMenu() {
        navigationView.getMenu().clear();

        if (isLoggedIn) {
            navigationView.inflateMenu(R.menu.drawer_menu);
        } else {
            navigationView.inflateMenu(R.menu.drawer_menu_guest);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_estimate) {
                loadFragment(new EstimateRouteFragment(), true);

            } else if (id == R.id.nav_login) {
                loadFragment(new LoginFragment(), true);

            } else if (id == R.id.nav_register) {
                loadFragment(new RegisterFragment(), true);

            } else if (id == R.id.nav_home) {
                loadFragment(new HomeFragment(), true);

            } else if (id == R.id.nav_logout) {
                isLoggedIn = false;
                setupMenu();
                loadFragment(new HomeFragment(), false);
            } else if (id == R.id.nav_profile){
                loadFragment(new DriverAdditionalInfoFragment(), true);
            }

            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    public void onLoginSuccess() {
        isLoggedIn = true;
        setupMenu();
        loadFragment(new HomeFragment(), false);
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack) {
        if (addToBackStack) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commit();
        }
    }
}
