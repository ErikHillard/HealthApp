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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.series.DataPoint;
//import com.jjoe64.graphview.series.LineGraphSeries;




public class ExerciseFragment extends Fragment{

    private AutoCompleteTextView addExcercise;
    private Button addOtherExcerciseButton;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText excerciseName, calories, length, days;
    private Button addOtherExcerciseSave, addOtherExcerciseCancel, seeGraphButton, excerciseExplainButton;
    private EditText lengthWorkout, caloriesBurned;
    private AutoCompleteTextView excercise;
    private Handler hdlr = new Handler();
    private TextView progessUpdate, explainExcercise;
    private String[] ExcercisesDoneArray;


    private ArrayList<HashMap<String, String>> excerciseData;
    private String[] Excercises;
    ArrayAdapter<String> adapter;
    private ProgressBar pgsBar;
    private int i;
    private String excerciseInput;
//    private GraphView graphView;

    private LineChart excerciseChart;
    private ListView excerciseDone;
    private ArrayAdapter<String> adapterExcerciseDone;

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
//        days = (EditText) addOtherExcerciseView.findViewById(R.id.Days);



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
//                Integer workout_amount = Integer.parseInt(lengthWorkout.getText().toString());
//                Integer day = Integer.parseInt(days.getText().toString());
                newExcercise.put("Name", excerciseInput);
                newExcercise.put("Calories", caloriesBurnt);

                //caculate calories


//                cj.saveExercise(newExcercise);
                cj.addExerciseForDay(excerciseInput, caloriesBurnt, 1);

//                cj.addExerciseForDay(excerciseInput, caloriesBurnt, 1);

                populateExcercise();
                updateProgressBar();
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

        excerciseDone = (ListView) view.findViewById(R.id.exerciseList);
        populateExcercise();
        adapterExcerciseDone = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, ExcercisesDoneArray);
        excerciseDone.setAdapter(adapterExcerciseDone);

        seeGraphButton = (Button) view.findViewById(R.id.seeGraphButton);
        seeGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { createGraphDialog(); }
        });


        excerciseExplainButton = (Button) view.findViewById(R.id.ExplainButton);
        excerciseExplainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { createExcerciseExplainDialog(); }
        });


        pgsBar = (ProgressBar) view.findViewById(R.id.pBar);
        i = pgsBar.getProgress();


        Integer caloriesTotal = 0;

        HashMap<String, String> excerciseForDay = cj.getExerciseDay(1);

        ArrayList<String> excercisesDoneH = new ArrayList<>();
        for (String excercise: excerciseForDay.keySet()) {
//            excercisesDoneH.add(excercise + ": " + excerciseForDay.get(excercise));
            caloriesTotal += Integer.parseInt(excerciseForDay.get(excercise));
        }

        HashMap<String,String> caloriesGoalHash = cj.getExerciseGoals();
        Integer caloriesGoal = Integer.parseInt(caloriesGoalHash.get("Calories"));

        pgsBar.setMax(caloriesGoal);
        pgsBar.setProgress(caloriesTotal);
        progessUpdate.setText(caloriesTotal.toString() + "/" + caloriesGoal.toString());



        return view;
    }

    private void createExcerciseExplainDialog(){
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View graphView = getLayoutInflater().inflate(R.layout.excercise_explanation, null);

        explainExcercise = (TextView) view.findViewById(R.id.ExplainTheExcercise);

//        explainExcercise.setText(
//                "Pushup:  Get down on all wider than your shoulders. Lower and raise your body up"
//        );



        dialogBuilder.setView(graphView);
        dialog = dialogBuilder.create();
        dialog.show();



    }

    private void createGraphDialog() {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View graphView = getLayoutInflater().inflate(R.layout.excercise_graph, null);


        excerciseChart = (LineChart) graphView.findViewById(R.id.caloriesOverTimeChartE);

        int[] val = new int[5];
        val[0] = 200;
        val[1] = 300;
        val[2] = 40;
        val[3] = 500;
        val[4] = 300;

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < val.length; i ++) {
            values.add(new Entry(i+1, val[i]));
        }

        LineDataSet set1 = new LineDataSet(values, "Calories");
        set1.setDrawCircles(true);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);

        excerciseChart.setData(data);
        excerciseChart.getAxisLeft().setDrawGridLines(false);
        excerciseChart.getXAxis().setDrawGridLines(false);
        excerciseChart.getDescription().setEnabled(false);
        excerciseChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        dialogBuilder.setView(graphView);
        dialog = dialogBuilder.create();
        dialog.show();

    }

    private void populateExcercise(){
        HashMap<String, String> excerciseForDay = cj.getExerciseDay(1);

        ArrayList<String> excercisesDoneH = new ArrayList<>();
        for (String excercise: excerciseForDay.keySet()) {
            excercisesDoneH.add(excercise + ": " + excerciseForDay.get(excercise));
        }

        ExcercisesDoneArray = excercisesDoneH.toArray(new String[excercisesDoneH.size()]);

        if (adapterExcerciseDone != null) {
            adapterExcerciseDone = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, ExcercisesDoneArray);
            excerciseDone.setAdapter(adapterExcerciseDone);
        }


    }

    private void updateProgressBar(){

        Integer caloriesTotal = 0;

        HashMap<String, String> excerciseForDay = cj.getExerciseDay(1);

        ArrayList<String> excercisesDoneH = new ArrayList<>();
        for (String excercise: excerciseForDay.keySet()) {
//            excercisesDoneH.add(excercise + ": " + excerciseForDay.get(excercise));
            caloriesTotal += Integer.parseInt(excerciseForDay.get(excercise));
        }

        HashMap<String,String> caloriesGoalHash = cj.getExerciseGoals();
        Integer caloriesGoal = Integer.parseInt(caloriesGoalHash.get("Calories"));

        pgsBar.setMax(caloriesGoal);
        pgsBar.setProgress(caloriesTotal);
        progessUpdate.setText(caloriesTotal.toString() + "/" + caloriesGoal.toString());




    }






}