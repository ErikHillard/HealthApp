package com.example.healthapp;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;



public class ExerciseFragment extends Fragment{

    private AutoCompleteTextView addExcercise;
    private Button addOtherExcerciseButton;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText excerciseName, calories, length;
    private Button addOtherExcerciseSave, addOtherExcerciseCancel;
    private EditText lengthWorkout, caloriesBurned;
    private AutoCompleteTextView excercise;

    private ArrayList<HashMap<String, String>> excerciseData;
    private String[] Excercises;
    ArrayAdapter<String> adapter;


    View view;

    CustomJson cj;

    public String[] workouts = {
            "Running",
            "Weight Lifting",
            "Walk",
            "Climbing",
            "Hiking",
            "Jogging"
    };

    public ExerciseFragment(File files_dir) {
        // Required empty public constructor
        String files = files_dir.toString();
        cj = new CustomJson(new File(files, "data.json"));
    }



    private void populateExcercise() {
        excerciseData = cj.getFoodData();
        Excercises = new String[excerciseData.size()];

        for (int i = 0; i < excerciseData.size(); i ++) {
            Excercises[i] = excerciseData.get(i).get("Name");
            Log.e("myTag", Excercises[i]);
        }

        if (adapter != null) {
            adapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, Excercises );
            excercise.setAdapter(adapter);
        }
    }

    public void createNewAddOtherExcerciseDialog() {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View addOtherExcerciseView = getLayoutInflater().inflate(R.layout.add_other_excerise, null);


        excercise = (AutoCompleteTextView) addOtherExcerciseView.findViewById(R.id.workoutType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, workouts);
//        //Getting the instance of AutoCompleteTextView
        excercise.setThreshold(1);//will start working from first character
        excercise.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        excercise.setTextColor(Color.RED);

        caloriesBurned = (EditText) addOtherExcerciseView.findViewById(R.id.caloriesBurnt);

        addOtherExcerciseSave = (Button) addOtherExcerciseView.findViewById(R.id.addOtherExcerciseItemSave);
        addOtherExcerciseCancel = (Button) addOtherExcerciseView.findViewById(R.id.addOtherExcerciseItemCancel);

        dialogBuilder.setView(addOtherExcerciseView);
        dialog = dialogBuilder.create();
        dialog.show();

        addOtherExcerciseSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // define save button.
                HashMap<String, String> newExcercise = new HashMap<String, String>();
                newExcercise.put("Name", excercise.toString());
                newExcercise.put("Calories", caloriesBurned.toString());

                cj.saveFood(newExcercise);
                populateExcercise();

                dialog.dismiss();


            }
        });



        addOtherExcerciseCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_exercise, container, false);
        addOtherExcerciseButton = (Button) view.findViewById(R.id.addOtherExcerciseItem);
        addOtherExcerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAddOtherExcerciseDialog();
            }
        });

        return view;

    }
}