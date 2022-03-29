package com.example.healthapp;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class ExerciseFragment extends Fragment {

    private AutoCompleteTextView addExcercise;
    private Button addOtherExcerciseButton;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText excerciseName, calories, length;
    private Button addOtherExcerciseSave, addOtherExcerciseCancel;

    View view;

    public String[] workouts = {
            "Running",
            "Weight Lifting",
            "Walk",
            "Climbing",
            "Hiking",
            "Jogging"
    };

    public ExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_exercise, container, false);

        addExcercise = (AutoCompleteTextView) view.findViewById(R.id.addExcerciseItem);
        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, workouts);

        //Getting the instance of AutoCompleteTextView
        addExcercise.setThreshold(1);//will start working from first character
        addExcercise.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        addExcercise.setTextColor(Color.RED);


        return view;






    }
}