package com.example.taxiapp.ui.profile_info;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.activity.result.contract.ActivityResultContracts;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.auth.forgot_password.ForgotPasswordFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import model.DriverProfileChangeRequest;
import model.DriverProfileResponse;
import model.UpdateUserProfileRequest;
import model.UserProfileResponse;
import retrofit2.Call;
import retrofit2.Response;
import service.AuthService;
import service.ProfileService;
import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private ImageView ivDriverProfilePic;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    private TextInputEditText etFirstName, etLastName, etAddress, etPhone, etEmail;

    private TextInputLayout tilFirstName, tilLastName, tilAddress, tilPhone, tilEmail;

    private String initialFirstName, initialLastName, initialAddress, initialPhone, initialEmail;
    private Drawable initialProfileImage;

    private ProfileService profileService;

    private Uri selectedImageUri;
    private String initialProfileImageUrl;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DriverProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        selectedImageUri = result.getData().getData();
                        ivDriverProfilePic.setImageURI(selectedImageUri);
                    }
                }
        );


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profileService = ProfileService.getInstance();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_info, container, false);

        // image

        ivDriverProfilePic = view.findViewById(R.id.ivDriverProfilePic);

        ivDriverProfilePic.setOnClickListener(v-> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        // input editText

        etFirstName = view.findViewById(R.id.etDriverFirstName);
        etLastName = view.findViewById(R.id.etDriverLastName);
        etAddress = view.findViewById(R.id.etDriverAddress);
        etPhone = view.findViewById(R.id.etDriverPhone);
        etEmail = view.findViewById(R.id.etDriverEmail);

        // input layouti

        tilFirstName = view.findViewById(R.id.tilDriverFirstName);
        tilLastName = view.findViewById(R.id.tilDriverLastName);
        tilAddress = view.findViewById(R.id.tilDriverAddress);
        tilPhone = view.findViewById(R.id.tilDriverPhone);
        tilEmail = view.findViewById(R.id.tilDriverEmail);

        enableEditOnPencilClick(tilFirstName, etFirstName);
        enableEditOnPencilClick(tilLastName, etLastName);
        enableEditOnPencilClick(tilAddress, etAddress);
        enableEditOnPencilClick(tilPhone, etPhone);
        enableEditOnPencilClick(tilEmail, etEmail);

        // values for reset
        initialFirstName = etFirstName.getText().toString();
        initialLastName = etLastName.getText().toString();
        initialAddress = etAddress.getText().toString();
        initialPhone = etPhone.getText().toString();
        initialEmail = etEmail.getText().toString();
        initialProfileImage = ivDriverProfilePic.getDrawable();

        Button btnReset = view.findViewById(R.id.btnDriverResetChanges);
        btnReset.setOnClickListener(v -> resetFields());

        Button btnSaveChanges = view.findViewById(R.id.btnDriverSaveChanges);
        Long userId = AuthService.getInstance().getLoggedInUserId(requireContext());
        btnSaveChanges.setOnClickListener(v -> {

            if (!validateInputs()) {
                Snackbar.make(view,
                                "Please fix validation errors",
                                Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }


            if (AuthService.getInstance().isDriver(requireContext())) {
                UpdateUserProfileRequest request = new UpdateUserProfileRequest();
                request.setFirstName(etFirstName.getText().toString().trim());
                request.setLastName(etLastName.getText().toString().trim());
                request.setAddress(etAddress.getText().toString().trim());
                request.setPhone(etPhone.getText().toString().trim());
                request.setEmail(etEmail.getText().toString().trim());
                request.setProfileImage(initialProfileImageUrl);

                profileService.submitDriverProfileChange(userId, request, new retrofit2.Callback<DriverProfileChangeRequest>() {
                    @Override
                    public void onResponse(Call<DriverProfileChangeRequest> call, Response<DriverProfileChangeRequest> response) {
                        if(response.isSuccessful() && response.body() != null) {
                            Snackbar.make(view, "Profile change request submitted", Snackbar.LENGTH_SHORT).show();

                            // Zaključavanje polja
                            etFirstName.setEnabled(false);
                            etLastName.setEnabled(false);
                            etAddress.setEnabled(false);
                            etPhone.setEnabled(false);
                            etEmail.setEnabled(false);
                        } else {

                            try {
                                String errorBody = response.errorBody() != null ? response.errorBody().string() : "empty";
                                Log.e("PROFILE_REQUEST", "Server je vratio gresku! Code: " + response.code() + ", Body: " + errorBody);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Snackbar.make(view, "Failed to submit change request", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DriverProfileChangeRequest> call, Throwable t) {
                        Snackbar.make(view, "Error: " + t.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });



                return;
            }



            UpdateUserProfileRequest request = new UpdateUserProfileRequest();
            request.setFirstName(etFirstName.getText().toString().trim());
            request.setLastName(etLastName.getText().toString().trim());
            request.setAddress(etAddress.getText().toString().trim());
            request.setPhone(etPhone.getText().toString().trim());
            request.setEmail(etEmail.getText().toString().trim());
            request.setProfileImage(initialProfileImageUrl);



            profileService.updateUserProfile(userId, request, new retrofit2.Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    Snackbar.make(view,
                                    "Profile updated successfully",
                                    Snackbar.LENGTH_SHORT)
                            .show();


                    initialFirstName = etFirstName.getText().toString();
                    initialLastName = etLastName.getText().toString();
                    initialAddress = etAddress.getText().toString();
                    initialPhone = etPhone.getText().toString();
                    initialEmail = etEmail.getText().toString();

                    etFirstName.setEnabled(false);
                    etLastName.setEnabled(false);
                    etAddress.setEnabled(false);
                    etPhone.setEnabled(false);
                    etEmail.setEnabled(false);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Snackbar.make(view,
                                    "Failed to update profile",
                                    Snackbar.LENGTH_SHORT)
                            .show();
                }
            });
        });


        loadProfile();

        // change password

        TextView tcChangePassword = view.findViewById(R.id.tvChangePassword);

        tcChangePassword.setOnClickListener(v -> {

            ForgotPasswordFragment fragment = new ForgotPasswordFragment();

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .addToBackStack(null)
                    .commit();

        });

        return view;

    }

    private void enableEditOnPencilClick(TextInputLayout layout, TextInputEditText editText){
        layout.setEndIconOnClickListener(v -> {
            editText.setEnabled(true);
            editText.requestFocus();
            editText.setSelection(editText.getText().length());
        });
    }

    private void resetFields(){
        etFirstName.setText(initialFirstName);
        etLastName.setText(initialLastName);
        etAddress.setText(initialAddress);
        etPhone.setText(initialPhone);
        etEmail.setText(initialEmail);

        etFirstName.setEnabled(false);
        etLastName.setEnabled(false);
        etAddress.setEnabled(false);
        etPhone.setEnabled(false);
        etEmail.setEnabled(false);

        selectedImageUri = null;

        loadProfileImage(initialProfileImageUrl);

    }

    private boolean validateInputs() {
        boolean isValid = true;


        tilFirstName.setError(null);
        tilLastName.setError(null);
        tilAddress.setError(null);
        tilPhone.setError(null);
        tilEmail.setError(null);


        String firstName = etFirstName.getText().toString().trim();
        if (firstName.isEmpty()) {
            tilFirstName.setError("First name is required");
            isValid = false;
        } else if (firstName.length() < 2 || firstName.length() > 50) {
            tilFirstName.setError("First name must be between 2 and 50 characters");
            isValid = false;
        }


        String lastName = etLastName.getText().toString().trim();
        if (lastName.isEmpty()) {
            tilLastName.setError("Last name is required");
            isValid = false;
        } else if (lastName.length() < 2 || lastName.length() > 50) {
            tilLastName.setError("Last name must be between 2 and 50 characters");
            isValid = false;
        }


        String address = etAddress.getText().toString().trim();
        if (address.isEmpty()) {
            tilAddress.setError("Address is required");
            isValid = false;
        } else if (address.length() > 100) {
            tilAddress.setError("Address can be at most 100 characters long");
            isValid = false;
        }


        String phone = etPhone.getText().toString().trim();
        if (phone.isEmpty()) {
            tilPhone.setError("Phone number is required");
            isValid = false;
        } else if (!phone.matches("^\\+?[0-9]{9,15}$")) {
            tilPhone.setError("Invalid phone number");
            isValid = false;
        }


        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            tilEmail.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Email must be valid");
            isValid = false;
        }

        return isValid;
    }


    private void loadProfile() {
        Long userId = AuthService.getInstance().getLoggedInUserId(requireContext());

        if (AuthService.getInstance().isDriver(requireContext())) {

            profileService.getDriverProfile(userId, new retrofit2.Callback<DriverProfileResponse>() {
                @Override
                public void onResponse(Call<DriverProfileResponse> call, Response<DriverProfileResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        DriverProfileResponse profile = response.body();
                        populateProfileFields(profile.getFirstName(), profile.getLastName(),
                                profile.getAddress(), profile.getPhone(), profile.getEmail(), profile.getProfileImage());
                    } else {
                        Snackbar.make(getView(), "Failed to load driver profile", Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DriverProfileResponse> call, Throwable t) {
                    Snackbar.make(getView(), "Error loading driver profile: " + t.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            });
        } else {

            profileService.getUserProfile(userId, new retrofit2.Callback<UserProfileResponse>() {
                @Override
                public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserProfileResponse profile = response.body();
                        populateProfileFields(profile.getFirstName(), profile.getLastName(),
                                profile.getAddress(), profile.getPhone(), profile.getEmail(), profile.getProfileImage());
                    } else {
                        Snackbar.make(getView(), "Failed to load user profile", Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                    Snackbar.make(getView(), "Error loading user profile: " + t.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void populateProfileFields(String firstName, String lastName,
                                       String address, String phone, String email, String profileImageUrl) {
        etFirstName.setText(firstName);
        etLastName.setText(lastName);
        etAddress.setText(address);
        etPhone.setText(phone);
        etEmail.setText(email);

        initialFirstName = firstName;
        initialLastName = lastName;
        initialAddress = address;
        initialPhone = phone;
        initialEmail = email;

        initialProfileImageUrl = profileImageUrl;
        loadProfileImage(profileImageUrl);
        initialProfileImage = ivDriverProfilePic.getDrawable();
    }


    private void loadProfileImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(ivDriverProfilePic);
        } else {
            ivDriverProfilePic.setImageResource(R.drawable.profile);
        }
    }

}