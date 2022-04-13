package com.example.healthapp;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.series.DataPoint;
//import com.jjoe64.graphview.series.LineGraphSeries;




public class ExerciseFragment extends Fragment{

    private AutoCompleteTextView addExcercise;
    private Button addOtherExcerciseButton;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText excerciseName, calories, length, days;
    private Button addOtherExcerciseSave, addOtherExcerciseCancel;
    private EditText lengthWorkout, caloriesBurned;
    private AutoCompleteTextView excercise;
    private Handler hdlr = new Handler();
    private TextView progessUpdate;

    private ArrayList<HashMap<String, String>> excerciseData;
    private String[] Excercises;
    ArrayAdapter<String> adapter;
    private ProgressBar pgsBar;
    private int i;
    private String excerciseInput;
//    private GraphView graphView;

    View view;

    CustomJson cj;

    public String[] workouts = {
            "Badminton",
            "Baseball",
            "BasketBall",
            "Bicycling",
            "Boxing",
            "Burpees",
            "Climbing",
            "Crunches",
            "Football",
            "Golf",
            "Gymnastics",
            "Hiking",
            "Hockey",
            "Jogging",
            "Jump Rope",
            "Jumping Jacks",
            "Lunges",
            "Mountain Climbers",
            "Mountain Climbers",
            "Planks",
            "Push ups",
            "Soccer",
            "Squats",
            "Stair Climbs",
            "Swimming",
            "Tennis",
            "Volleyball",
            "Walk",
            "Weight Lifting",
            "Running",
    };

    public ExerciseFragment(File files_dir) {
        // Required empty public constructor
        String files = files_dir.toString();
        cj = new CustomJson(new File(files, "data.json"));
    }

    private void populateExcercise() {
        excerciseData = cj.getExerciseData();
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
        lengthWorkout = (EditText) addOtherExcerciseView.findViewById(R.id.workoutType);
        days = (EditText) addOtherExcerciseView.findViewById(R.id.Days);



        addOtherExcerciseSave = (Button) addOtherExcerciseView.findViewById(R.id.addOtherExcerciseItemSave);
        addOtherExcerciseCancel = (Button) addOtherExcerciseView.findViewById(R.id.addOtherExcerciseItemCancel);

        dialogBuilder.setView(addOtherExcerciseView);
        dialog = dialogBuilder.create();
        dialog.show();



        String caloriesBurnt = "";
        excercise.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                excerciseInput = (String)parent.getItemAtPosition(position);
            }
        });

        addOtherExcerciseSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                HashMap<String, String> newExcercise = new HashMap<String, String>();
                String caloriesBurnt = caloriesBurned.getText().toString();
                Integer workout_amount = Integer.parseInt(lengthWorkout.getText().toString());
                Integer day = Integer.parseInt(days.getText().toString());
                newExcercise.put("Name", excerciseInput);

                if(caloriesBurnt  == "") {
                    if (excerciseInput == "Running") {
                        caloriesBurnt = String.valueOf(120 * workout_amount);
                    }
                    if (excerciseInput == "Jogging") {
                        caloriesBurnt = String.valueOf(80 * workout_amount);
                    }
                    if (excerciseInput == "Walking") {
                        caloriesBurnt = String.valueOf(80 * workout_amount);
                    }
                    if (excerciseInput == "Push Ups") {
                        caloriesBurnt = String.valueOf(0.4 * workout_amount);
                    }
                    if (excerciseInput == "Bicycling") {
                        caloriesBurnt = String.valueOf(30 * workout_amount);
                    }





                }

                newExcercise.put("Calories", caloriesBurnt);

                //caculate calories


                cj.saveExercise(newExcercise);

//                cj.addExerciseForDay(excerciseInput, caloriesBurnt, 1);

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
    public void onStop() {
        super.onStop();
        cj.writeFile();
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

        addOtherExcerciseButton = (Button) view.findViewById(R.id.addOtherExcerciseItem);
        progessUpdate = (TextView) view.findViewById(R.id.progress_number);

        pgsBar = (ProgressBar) view.findViewById(R.id.pBar);
        i = pgsBar.getProgress();


        new Thread(new Runnable() {
            public void run() {
                while (i < 250) {
                    i += 1;


                    // Update the progress bar and display the current value in text view
                    hdlr.post(new Runnable() {
                        public void run() {
                            pgsBar.setProgress(i);
                            //get the actual values here
                            progessUpdate.setText(i+"/"+ 250);
                            init();

                        }
                    });
                    try {
                        // Sleep for 100 milliseconds to show the progress slowly.
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //get code for
//        GraphView graph = (GraphView) view.findViewById(R.id.graph);
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph.addSeries(series);



        return view;

    }

    //Graph data
    public void init() {


        TextView textView = (TextView) view.findViewById(R.id.test_text);
        ArrayList<HashMap<String,String>>  allExcercise = cj.getExerciseData();
        String listOfExcercise = "";
        String calories_burned = "";
        for (int i=0;i< allExcercise.size();i++){
            ArrayList<String> string_elements = new ArrayList<>();
            HashMap<String,String> temp = allExcercise.get(i);
            for(String k: temp.keySet()){
                string_elements.add(k);
                string_elements.add(temp.get(k));
                listOfExcercise += k + " " + temp.get(k) + " ";
            }
            listOfExcercise += "\n";

            //parse the string_elements for the important parts
        }

        textView.setText(listOfExcercise);








    }




}