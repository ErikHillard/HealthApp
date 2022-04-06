package com.example.healthapp;

import android.app.AlertDialog;
import android.content.Context;
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
import android.content.Context;



public class ExerciseFragment extends Fragment{

    private AutoCompleteTextView addExcercise;
    private Button addOtherExcerciseButton;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText excerciseName, calories, length;
    private Button addOtherExcerciseSave, addOtherExcerciseCancel;
    private EditText excercise, lengthWorkout, caloriesBurned;

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

    public void createNewAddOtherExcerciseDialog() {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View addOtherExcerciseView = getLayoutInflater().inflate(R.layout.add_other_excerise, null);

        excercise = (EditText) addOtherExcerciseView.findViewById(R.id.workoutType);
        lengthWorkout = (EditText) addOtherExcerciseView.findViewById(R.id.lengthWork);
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


    private void writeToFile(String message, Context context)
    {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("todolist.txt",
                    Context.MODE_PRIVATE));
            outputStreamWriter.write(message);
            outputStreamWriter.close();

        } catch(FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFromFile(Context context) throws IOException {
        String result = "";
        InputStream inputStream = context.openFileInput("todolist.txt");
        if(inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String temp = "";
            StringBuilder stringBuilder = new StringBuilder();

            while((temp = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(temp);
                stringBuilder.append("\n");
            }

            inputStream.close();
            result = stringBuilder.toString();
        }
        return result;
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