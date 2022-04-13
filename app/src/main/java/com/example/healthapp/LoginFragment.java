package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.File;

public class LoginFragment extends Fragment {

    TextView username;
    TextView password;
    String height;
    String weight;
    String age;
    Boolean registered;
    MaterialButton loginButton;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = view.findViewById(R.id.usernameInput);
        password = view.findViewById(R.id.passwordInput);
        registered = false;
        Bundle bundle = getArguments();
        if (bundle != null) {
            String user = bundle.getString("Username");
            String pass = bundle.getString("Password");
            String height = bundle.getString("Height");
            String weight = bundle.getString("Weight");
            String age = bundle.getString("Age");

            username.setText(user);
            password.setText(pass);

            registered = bundle.getBoolean("registered");

            if (registered != null && registered) {
                proccessLogin();
            }
        }

        loginButton = (MaterialButton) view.findViewById(R.id.loginbutton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proccessLogin();
            }
        });

        MaterialButton registerNavButton = (MaterialButton) view.findViewById(R.id.registerNavButton);

        registerNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new RegisterFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);
                transaction.addToBackStack(null);
                transaction.replace(R.id.fragment_login_container, newFragment);
                transaction.commit();
            }
        });
    }

    private void proccessLogin() {
        CustomJson cj = new CustomJson(new File(getActivity().getFilesDir().toString(), "data.json"));
        String storedUserName = cj.getUserName();
        String storedPassword = cj.getUserPassword();
        if (registered ||
                (username.getText().toString().equals(storedUserName)
                && password.getText().toString().equals(storedPassword))){
            setArguments(null);
            password.setText("");
            if (registered) {
                cj.replaceUser(username.getText().toString(), password.getText().toString(), age, height, weight);
                cj.removeAllData();
                registered = false;
            }
            Intent switchActivity = new Intent(getActivity(), MainActivity.class);
            switchActivity.putExtra("Username", username.getText().toString());
            switchActivity.putExtra("NewUser", false);
            startActivity(switchActivity);
        } else {
            Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
            password.setText("");
        }
    }
}