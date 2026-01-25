package com.example.taxiapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.admin.AdminHomeFragment;
import com.example.taxiapp.ui.admin.AdminRideHistoryFragment;
import com.example.taxiapp.ui.auth.login.LoginFragment;
import com.example.taxiapp.ui.auth.register.RegisterFragment;
import com.example.taxiapp.ui.auth.reset_password.ResetPasswordFragment;
import com.example.taxiapp.ui.driver.DriverHomeFragment;
import com.example.taxiapp.ui.driver_additional_info.DriverAdditionalInfoFragment;
import com.example.taxiapp.ui.estimate_route.EstimateRouteFragment;
import com.example.taxiapp.ui.guest.GuestHomeFragment;
import com.example.taxiapp.ui.passenger.PassengerHomeFragment;
import com.example.taxiapp.ui.passenger.PassengerRideHistoryFragment;
import com.example.taxiapp.ui.shared.RideHistoryFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_TYPE = "userType";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private boolean isLoggedIn;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clearUserState(); // clear user state on each launch

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        findViewById(R.id.menu_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.END)
        );

        loadUserState();
        setupMenu();
        loadStartFragment();
    }

    // STATE

    private void loadUserState() {
        SharedPreferences prefs =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false);
        userType = prefs.getString(KEY_USER_TYPE, "guest");
    }

    private void clearUserState() {
        SharedPreferences prefs =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().clear().apply();

        isLoggedIn = false;
        userType = "guest";
    }

    // MENU

    private void setupMenu() {
        navigationView.getMenu().clear();

        if ("ADMIN".equals(userType)) {
            navigationView.inflateMenu(R.menu.drawer_menu_admin);
        } else if ("DRIVER".equals(userType)) {
            navigationView.inflateMenu(R.menu.drawer_menu_driver);
        } else if ("PASSENGER".equals(userType)) {
            navigationView.inflateMenu(R.menu.drawer_menu_passenger);
        }
        else {
            navigationView.inflateMenu(R.menu.drawer_menu_guest);
        }

        navigationView.setNavigationItemSelectedListener(this::onMenuItemSelected);
    }

    private boolean onMenuItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragmentToLoad = null;

        // COMMON
        if (id == R.id.nav_home) {
            fragmentToLoad = getHomeFragment();
        } else if (id == R.id.nav_estimate) {
            fragmentToLoad = new EstimateRouteFragment();
        }

        // GUEST
        else if (id == R.id.nav_login && !isLoggedIn) {
            fragmentToLoad = new LoginFragment();
        } else if (id == R.id.nav_register && !isLoggedIn) {
            fragmentToLoad = new RegisterFragment();
        } else if (id == R.id.reset_password && !isLoggedIn) {
            fragmentToLoad = new ResetPasswordFragment();
        }

        // DRIVER
        else if (id == R.id.nav_ride_history && "DRIVER".equals(userType)) {
            fragmentToLoad = new RideHistoryFragment();
        } else if (id == R.id.nav_profile && "DRIVER".equals(userType)) {
            fragmentToLoad = new DriverAdditionalInfoFragment();
        }

        // ADMIN
        else if (id == R.id.nav_admin_ride_history && "ADMIN".equals(userType)) {
            fragmentToLoad = new AdminRideHistoryFragment();
        }

        // PASSENGER
        else if (id == R.id.nav_ride_history && "PASSENGER".equals(userType)) {
            fragmentToLoad = new PassengerRideHistoryFragment();
        }

        // LOGOUT
        else if (id == R.id.nav_logout) {
            // if role is driver - cant logout while active
            clearUserState();
            setupMenu();
            fragmentToLoad = new GuestHomeFragment();
        }

        if (fragmentToLoad != null) {
            loadFragment(fragmentToLoad, id != R.id.nav_home);
        }

        drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    // NAVIGATION
    private void loadStartFragment() {
        loadFragment(getHomeFragment(), false);
    }

    private Fragment getHomeFragment() {
        if ("ADMIN".equals(userType)) {
            return new AdminHomeFragment();
        } else if ("DRIVER".equals(userType)) {
            return new DriverHomeFragment();
        } else if ("PASSENGER".equals(userType)) {
            return new PassengerHomeFragment();
        }
        else {
            return new GuestHomeFragment();
        }
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        if (addToBackStack) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commit();
        }
    }

    public void onAdminLoginSuccess() {
        reloadAfterLogin("ADMIN");
    }

    public void onDriverLoginSuccess() {
        reloadAfterLogin("DRIVER");
    }

    public void onPassengerLoginSuccess() {
        reloadAfterLogin("PASSENGER");
    }

    public void setLogoutEnabled(boolean enabled, String title) {
        if (navigationView == null) return;

        MenuItem logoutItem = navigationView
                .getMenu()
                .findItem(R.id.nav_logout);

        if (logoutItem != null) {
            logoutItem.setEnabled(enabled);
            logoutItem.setTitle(title);
        }
    }

    private void reloadAfterLogin(String role) {
        SharedPreferences prefs =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        prefs.edit()
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .putString(KEY_USER_TYPE, role)
                .apply();

        loadUserState();
        setupMenu();
        loadStartFragment();
    }
}
