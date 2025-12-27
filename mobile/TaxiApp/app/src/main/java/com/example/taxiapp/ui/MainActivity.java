package com.example.taxiapp.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.driver.DriverHomeFragment;
import com.example.taxiapp.ui.estimate_route.EstimateRouteFragment;
import com.example.taxiapp.ui.guest.GuestHomeFragment;
import com.example.taxiapp.ui.auth.login.LoginFragment;
import com.example.taxiapp.ui.auth.register.RegisterFragment;
import com.example.taxiapp.ui.shared.RideHistoryFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_CURRENT_FRAGMENT = "currentFragment";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private boolean isLoggedIn = false;
    private String currentFragmentTag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        findViewById(R.id.menu_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.END)
        );

        if (savedInstanceState != null) {
            isLoggedIn = savedInstanceState.getBoolean(KEY_IS_LOGGED_IN, false);
            currentFragmentTag = savedInstanceState.getString(KEY_CURRENT_FRAGMENT, null);
        }

        setupMenu();

        if (savedInstanceState == null) {
            Fragment startFragment = isLoggedIn ? new DriverHomeFragment() : new GuestHomeFragment();
            loadFragment(startFragment, false);
        }
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

            Fragment fragmentToLoad = null;

            if (id == R.id.nav_estimate) {
                fragmentToLoad = new EstimateRouteFragment();

            } else if (id == R.id.nav_login) {
                fragmentToLoad = new LoginFragment();

            } else if (id == R.id.nav_register) {
                fragmentToLoad = new RegisterFragment();

            } else if (id == R.id.nav_home) {
                fragmentToLoad = new GuestHomeFragment();

            } else if (id == R.id.nav_ride_history) {
                fragmentToLoad = new RideHistoryFragment();

            } else if (id == R.id.nav_logout) {
                isLoggedIn = false;
                setupMenu();
                fragmentToLoad = new GuestHomeFragment();
            }

            if (fragmentToLoad != null) {
                loadFragment(fragmentToLoad, true);
            }

            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    public void onLoginSuccess() {
        isLoggedIn = true;
        setupMenu();
        loadFragment(new DriverHomeFragment(), false);
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack) {
        currentFragmentTag = fragment.getClass().getSimpleName();

        if (addToBackStack) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment, currentFragmentTag)
                    .addToBackStack(currentFragmentTag)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment, currentFragmentTag)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        outState.putString(KEY_CURRENT_FRAGMENT, currentFragmentTag);
    }
}
