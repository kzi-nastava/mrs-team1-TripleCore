package com.example.taxiapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.admin.AdminHomeFragment;
import com.example.taxiapp.ui.admin.DriversRequestsFragment;
import com.example.taxiapp.ui.admin.PricingFormFragment;
import com.example.taxiapp.ui.auth.login.LoginFragment;
import com.example.taxiapp.ui.auth.register.RegisterFragment;
import com.example.taxiapp.ui.live_support.AdminChatFragment;
import com.example.taxiapp.ui.driver.DriverHomeFragment;
import com.example.taxiapp.ui.driver_additional_info.DriverAdditionalInfoFragment;
import com.example.taxiapp.ui.estimate_route.EstimateRouteFragment;
import com.example.taxiapp.ui.guest.GuestHomeFragment;
import com.example.taxiapp.ui.live_support.ChatFragment;
import com.example.taxiapp.ui.live_support.UserChatFragment;
import com.example.taxiapp.ui.passenger.NotificationListFragment;
import com.example.taxiapp.ui.passenger.PassengerHomeFragment;
import com.example.taxiapp.ui.profile_info.ProfileFragment;
import com.example.taxiapp.ui.review.ReviewsFragment;
import com.example.taxiapp.ui.ride_history.RideHistoryFragment;
import com.google.android.material.navigation.NavigationView;

import service.AuthService;

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
        }

        // DRIVER
        else if (id == R.id.nav_ride_history && "DRIVER".equals(userType)) {
            Long userId = AuthService.getInstance().getLoggedInUserId(this);
            fragmentToLoad = RideHistoryFragment.newInstanceForDriver(userId);
        } else if (id == R.id.nav_profile && "DRIVER".equals(userType)) {
            fragmentToLoad = new DriverAdditionalInfoFragment();
        } else if (id == R.id.nav_reviews && "DRIVER".equals(userType)) {
            fragmentToLoad = new ReviewsFragment();
        } else if (id == R.id.nav_user_live_support && "DRIVER".equals(userType)) {
            fragmentToLoad = new UserChatFragment();
        }

        // ADMIN
        else if (id == R.id.nav_admin_ride_history && "ADMIN".equals(userType)) {
            Long userId = AuthService.getInstance().getLoggedInUserId(this);
            fragmentToLoad = RideHistoryFragment.newInstanceForAdmin(userId);
        } else if (id == R.id.nav_profile && "ADMIN".equals(userType)) {
            fragmentToLoad = new ProfileFragment();
        } else if (id == R.id.nav_drivers_requests && "ADMIN".equals(userType)) {
            fragmentToLoad = new DriversRequestsFragment();
        } else if (id == R.id.nav_admin_live_support && "ADMIN".equals(userType)) {
            fragmentToLoad = new AdminChatFragment();
        } else if (id == R.id.nav_admin_pricing && "ADMIN".equals(userType)) {
            fragmentToLoad = new PricingFormFragment();
        }

        // PASSENGER
        else if (id == R.id.nav_ride_history && "PASSENGER".equals(userType)) {
            Long userId = AuthService.getInstance().getLoggedInUserId(this);
            fragmentToLoad = RideHistoryFragment.newInstanceForPassenger(userId);
        } else if (id == R.id.nav_profile && "PASSENGER".equals(userType)) {
            fragmentToLoad = new ProfileFragment();
        } else if (id == R.id.nav_reviews && "PASSENGER".equals(userType)) {
            fragmentToLoad = new ReviewsFragment();
        } else if (id == R.id.nav_user_live_support && "PASSENGER".equals(userType)) {
            fragmentToLoad = new UserChatFragment();
        } else if (id == R.id.nav_user_notifications && "PASSENGER".equals(userType)) {
            fragmentToLoad = new NotificationListFragment();
        }


        // LOGOUT
        else if (id == R.id.nav_logout) {
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
