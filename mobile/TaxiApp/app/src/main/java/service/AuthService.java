package service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import model.LoginRequest;
import model.LoginResponse;
import model.RegisterRequest;
import model.RegisterResponse;
import network.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AuthService {
    private static final String TAG = "AuthService";
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_ROLE = "userRole";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_FIRST_NAME = "userFirstName";
    private static final String KEY_USER_LAST_NAME = "userLastName";

    private static AuthService instance;
    private SharedPreferences prefs;

    private AuthService() {}

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    // Session management

    public void saveLoginSession(Context context, LoginResponse loginResponse) {
        if (context == null || loginResponse == null) {
            Log.e(TAG, "Cannot save session: context or loginResponse is null");
            return;
        }

        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putLong(KEY_USER_ID, loginResponse.getId());
        editor.putString(KEY_USER_ROLE, loginResponse.getRole().name());
        editor.putString(KEY_USER_EMAIL, loginResponse.getEmail());
        editor.putString(KEY_USER_FIRST_NAME, loginResponse.getFirstName());
        editor.putString(KEY_USER_LAST_NAME, loginResponse.getLastName());

        editor.apply();

        Log.d(TAG, "Login session saved for user: " + loginResponse.getEmail());
    }

    public boolean isLoggedIn(Context context) {
        if (context == null) return false;

        if (prefs == null) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public Long getLoggedInUserId(Context context) {
        if (context == null) return -1L;

        if (prefs == null) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        return prefs.getLong(KEY_USER_ID, -1L);
    }

    public String getLoggedInUserRole(Context context) {
        if (context == null) return "";

        if (prefs == null) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        return prefs.getString(KEY_USER_ROLE, "");
    }

    public boolean isDriver(Context context) {
        return "DRIVER".equals(getLoggedInUserRole(context));
    }

    public boolean isPassenger(Context context) {
        return "PASSENGER".equals(getLoggedInUserRole(context));
    }

    public boolean isAdmin(Context context) {
        return "ADMIN".equals(getLoggedInUserRole(context));
    }

    public String getUserEmail(Context context) {
        if (context == null) return "";

        if (prefs == null) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        return prefs.getString(KEY_USER_EMAIL, "");
    }

    public String getUserFullName(Context context) {
        if (context == null) return "";

        if (prefs == null) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        String firstName = prefs.getString(KEY_USER_FIRST_NAME, "");
        String lastName = prefs.getString(KEY_USER_LAST_NAME, "");

        return firstName + " " + lastName;
    }

    public void logout(Context context) {
        if (context == null) return;

        if (prefs == null) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Log.d(TAG, "User logged out");
    }

    // api

    public void login(String email, String password, Callback<LoginResponse> callback) {
        LoginRequest request = new LoginRequest(email, password);
        RetrofitClient.getApiService().login(request).enqueue(callback);
    }

    public void register(RegisterRequest request, Callback<RegisterResponse> callback) {
        RetrofitClient.getApiService().register(request).enqueue(callback);
    }

    public void forgotPassword(String email, Callback<ResponseBody> callback) {
        RetrofitClient.getApiService().forgotPassword(email).enqueue(callback);
    }
}