package com.example.healthapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class RegisterFragment extends Fragment {
    MaterialButton registerButton;
    MaterialButton returnButton;
    TextView username;
    TextView password;
    TextView height;
    TextView weight;
    ArrayList <String> fields;
    ArrayList <Boolean> fieldExists;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerButton = (MaterialButton) view.findViewById(R.id.registerButton);
        returnButton = (MaterialButton) view.findViewById(R.id.returnButton);
        username = view.findViewById(R.id.newUsername);
        password = view.findViewById(R.id.newPassword);
        height = view.findViewById(R.id.height);
        weight = view.findViewById(R.id.weight);

        fields = new ArrayList<>();
        fields.add("username");
        fields.add("password");
        fields.add("height");
        fields.add("weight");
        fieldExists = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++) {
            fieldExists.add(true);
        }


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("")) {
                    fieldExists.set(0, false);
                }
                if (password.getText().toString().equals("")) {
                    fieldExists.set(1, false);
                }
                if (height.getText().toString().equals("")) {
                    fieldExists.set(2, false);
                }
                if (weight.getText().toString().equals("")) {
                    fieldExists.set(3, false);
                }

                int missing = 0;
                for (Boolean bool : fieldExists) {
                    if (!bool) {
                        missing++;
                    }
                }

                if (missing > 0) {
                    String missingFields = "Please fill in your ";
                    if (missing == 1) {
                        for (int i = 0; i < fields.size(); i++) {
                            if (!fieldExists.get(i)) {
                                missingFields += fields.get(i);
                                missingFields += " ";
                            }
                        }
                    } else {
                        boolean first = true;
                        int count = 0;
                        for (int i = 0; i < fields.size(); i++) {
                            if (!fieldExists.get(i)) {
                                missingFields += fields.get(i);
                                count ++;
                                if (count + 1 == missing) {
                                    if (missing == 2) {
                                        missingFields += "and";
                                    } else {
                                        missingFields += ", and";
                                    }
                                } else if (missing > 2 && count != missing) {
                                    missingFields += ",";
                                }
                                missingFields += " ";
                            }
                        }
                    }
                    missingFields += "before continuing.";
                    Toast.makeText(getActivity(), missingFields, Toast.LENGTH_LONG).show();
                    for (int i = 0; i < fields.size(); i++) {
                        fieldExists.set(i, true);
                    }
                } else {
                    Fragment newFragment = new LoginFragment();
                    Bundle bundle = new Bundle();

                    bundle.putString("username", username.getText().toString());
                    bundle.putString("password", password.getText().toString());
                    bundle.putString("height", height.getText().toString());
                    bundle.putString("weight", weight.getText().toString());
                    bundle.putBoolean("registered", true);
                    newFragment.setArguments(bundle);

                    returnToLoginFragment(newFragment);
                }
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToLoginFragment(new LoginFragment());
            }
        });
    }

    private void returnToLoginFragment(Fragment newFragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.slide_out);
        transaction.replace(R.id.fragment_login_container, newFragment);
        getActivity().getSupportFragmentManager().popBackStack();
        transaction.commit();
    }
}