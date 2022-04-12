package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class RegisterFragment extends Fragment {
    MaterialButton registerButton;
    TextView username;
    TextView password;

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
        username = view.findViewById(R.id.newUsername);
        password = view.findViewById(R.id.newPassword);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new LoginFragment();
                Bundle bundle = new Bundle();

                bundle.putString("username", username.getText().toString());
                bundle.putString("password", password.getText().toString());
                bundle.putBoolean("registered", true);
                newFragment.setArguments(bundle);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.slide_out);
                transaction.replace(R.id.fragment_login_container, newFragment);
                transaction.commit();

//                Intent switchActivity = new Intent(getActivity(), MainActivity.class);
//                switchActivity.putExtra("NewUser", false);
//                startActivity(switchActivity);
            }
        });
    }
}