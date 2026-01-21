package com.example.taxiapp.ui;

import static java.sql.DriverManager.println;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.taxiapp.ui.shared.RideHistoryFragment;
import com.google.android.material.navigation.NavigationView;
import androidx.core.splashscreen.SplashScreen;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_TYPE = "userType"; // driver, admin, guest
    private static final String KEY_CURRENT_FRAGMENT = "currentFragment";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private boolean isLoggedIn = false;
    private String userType = "guest"; // "guest", "driver", "admin"
    private String currentFragmentTag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        findViewById(R.id.menu_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.END)
        );

        if (savedInstanceState != null) {
            isLoggedIn = savedInstanceState.getBoolean(KEY_IS_LOGGED_IN, false);
            userType = savedInstanceState.getString(KEY_USER_TYPE, "guest");
            currentFragmentTag = savedInstanceState.getString(KEY_CURRENT_FRAGMENT, null);
        }

        setupMenu();

        if (savedInstanceState == null) {
            Fragment startFragment;
            if (userType.equals("admin")) {
                startFragment = new AdminHomeFragment(); // ADMIN
            } else if (isLoggedIn) {
                startFragment = new DriverHomeFragment(); // DRIVER
            } else {
                startFragment = new GuestHomeFragment(); // GUEST
            }
            loadFragment(startFragment, false);
        }
    }

    private void setupMenu() {
        navigationView.getMenu().clear();

        if (userType.equals("admin")) {
            navigationView.inflateMenu(R.menu.drawer_menu_admin);
        } else if (isLoggedIn) {
            navigationView.inflateMenu(R.menu.drawer_menu_driver);
        } else {
            navigationView.inflateMenu(R.menu.drawer_menu_guest);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment fragmentToLoad = null;

            if (id == R.id.nav_estimate) {
                fragmentToLoad = new EstimateRouteFragment();
            } else if (id == R.id.nav_home) {
                if (userType.equals("admin")) {
                    fragmentToLoad = new AdminHomeFragment();
                } else if (isLoggedIn) {
                    fragmentToLoad = new DriverHomeFragment();
                } else {
                    fragmentToLoad = new GuestHomeFragment();
                }
            }

            // GUEST ONLY
            else if (id == R.id.nav_login && !isLoggedIn) {
                fragmentToLoad = new LoginFragment();
            } else if (id == R.id.nav_register && !isLoggedIn) {
                fragmentToLoad = new RegisterFragment();
            } else if (id == R.id.reset_password && !isLoggedIn) {
                fragmentToLoad = new ResetPasswordFragment();
            }

            // DRIVER ONLY
            else if (id == R.id.nav_ride_history && isLoggedIn && userType.equals("driver")) {
                fragmentToLoad = new RideHistoryFragment();
            } else if (id == R.id.nav_profile && isLoggedIn && userType.equals("driver")) {
                fragmentToLoad = new DriverAdditionalInfoFragment();
            }

            // ADMIN ONLY
            else if (id == R.id.nav_admin_ride_history && userType.equals("admin")) {
                fragmentToLoad = new AdminRideHistoryFragment();
            }

            // LOGOUT
            else if (id == R.id.nav_logout && (isLoggedIn || userType.equals("admin"))) {
                isLoggedIn = false;
                userType = "guest";
                setupMenu();
                fragmentToLoad = new GuestHomeFragment();
            }

            if (fragmentToLoad != null) {
                boolean addToBackStack = id != R.id.nav_home;
                loadFragment(fragmentToLoad, addToBackStack);
            }

            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    public void onAdminLoginSuccess() {
        isLoggedIn = true;
        userType = "admin";
        setupMenu();
        loadFragment(new AdminHomeFragment(), false);
    }

    public void onDriverLoginSuccess() {
        isLoggedIn = true;
        userType = "driver";
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
        outState.putString(KEY_USER_TYPE, userType);
        outState.putString(KEY_CURRENT_FRAGMENT, currentFragmentTag);
    }

    public void setLogoutEnabled(boolean enabled, String title) {
        NavigationView navView = findViewById(R.id.navigation_view);
        if (navView != null) {
            MenuItem logoutItem = navView.getMenu().findItem(R.id.nav_logout);
            if (logoutItem != null) {
                logoutItem.setEnabled(enabled);
                logoutItem.setTitle(title);
            }
        }
    }
}