package com.example.taxiapp.ui.driver_additional_info;

import android.app.Instrumentation;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.driver_profile.DriverProfileFragment;
import android.content.Intent;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverAdditionalInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverAdditionalInfoFragment extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverAdditionalInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment driverAdditionalInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverAdditionalInfoFragment newInstance(String param1, String param2) {
        DriverAdditionalInfoFragment fragment = new DriverAdditionalInfoFragment();
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





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_additional_info, container, false);

        Button btnGoToProfile = view.findViewById(R.id.btnChangeInfo);

        btnGoToProfile.setOnClickListener(v -> {
            Fragment fragment = new DriverProfileFragment();

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .addToBackStack(null)
                    .commit();

        });

        return view;

    }
}