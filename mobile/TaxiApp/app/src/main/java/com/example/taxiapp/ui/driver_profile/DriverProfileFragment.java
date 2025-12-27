package com.example.taxiapp.ui.driver_profile;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.activity.result.contract.ActivityResultContracts;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.change_password.ChangePasswordFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverProfileFragment extends Fragment {

    private ImageView ivDriverProfilePic;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    private TextInputEditText etFirstName, etLastName, etAddress, etPhone, etEmail;

    private TextInputLayout tilFirstName, tilLastName, tilAddress, tilPhone, tilEmail;

    private String initialFirstName, initialLastName, initialAddress, initialPhone, initialEmail;
    private Drawable initialProfileImage;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverProfileFragment() {
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
    public static DriverProfileFragment newInstance(String param1, String param2) {
        DriverProfileFragment fragment = new DriverProfileFragment();
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

                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {

                            ivDriverProfilePic.setImageURI(imageUri);
                        }
                    }
                }
        );


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_profile, container, false);

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

        // save changes button
        Button btnSaveChanges = view.findViewById(R.id.btnDriverSaveChanges);
        btnSaveChanges.setOnClickListener(v -> {

            if (!validateInputs()) {
                Snackbar.make(view,
                                "Please fill in all required fields",
                                Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }


            Snackbar.make(view,
                            "Changes saved successfully",
                            Snackbar.LENGTH_SHORT)
                    .show();


            initialFirstName = etFirstName.getText().toString();
            initialLastName = etLastName.getText().toString();
            initialAddress = etAddress.getText().toString();
            initialPhone = etPhone.getText().toString();
            initialEmail = etEmail.getText().toString();
            initialProfileImage = ivDriverProfilePic.getDrawable();
        });


        // change password

        TextView tcChangePassword = view.findViewById(R.id.tvChangePassword);

        tcChangePassword.setOnClickListener(v -> {

            ChangePasswordFragment fragment = new ChangePasswordFragment();

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

        ivDriverProfilePic.setImageDrawable(initialProfileImage);

    }

    private boolean validateInputs(){

        boolean isValid = true;

        tilFirstName.setError(null);
        tilLastName.setError(null);
        tilAddress.setError(null);
        tilPhone.setError(null);
        tilEmail.setError(null);

        if (etFirstName.getText().toString().trim().isEmpty()) {
            tilFirstName.setError("First name is required");
            isValid = false;
        }

        if (etLastName.getText().toString().trim().isEmpty()) {
            tilLastName.setError("Last name is required");
            isValid = false;
        }

        if (etAddress.getText().toString().trim().isEmpty()) {
            tilAddress.setError("Address is required");
            isValid = false;
        }

        if (etPhone.getText().toString().trim().isEmpty()) {
            tilPhone.setError("Phone number is required");
            isValid = false;
        }

        if (etEmail.getText().toString().trim().isEmpty()) {
            tilEmail.setError("Email is required");
            isValid = false;
        }

        return isValid;

    }


}