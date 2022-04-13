package com.example.healthapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Color;
import androidx.fragment.app.Fragment;

import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.internal.LinkedTreeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ConsumptionHomeFragment extends Fragment {

    CustomJson cj;
    View view;

    // Variables to create dialogs
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    // Consumption Home assets
    private Button globalAddFoodButton, seeGraphButton;
    private ListView foodEaten;
    private ArrayAdapter<String> adapterFoodEaten;
    private String[] foodsEaten;
    private int currentDay;
    private ProgressBar caloriePB, proteinPB, sodiumPB;
    private TextView caloriePBLabel, proteinPBLabel, sodiumPBLabel;

    // AddFood Home assets
    private AutoCompleteTextView addExistingFoodAutoComplete;
    private ArrayAdapter<String> adapterExistingFood;
    private ArrayList<HashMap<String, String>> foodData;
    private String[] FOODS;
    private String selectedFood = "";
    private Button addOtherFoodItemButton, addExistingFoodItemButton;

    // ExistingFood Assets
    private TextView caloriesOverview, sodiumOverview, sugarOverview, proteinOverview, general;
    private EditText numServings;
    private Button addExistingFoodSave, addExistingFoodCancel;

    // OtherFood Assets
    private EditText foodName, servings, calories, sodium, sugar, protein;
    private Button addOtherFoodSave, addOtherFoodCancel;

    // Graph Assets
    private LineChart caloriesOverTime;

    // Constructor
    public ConsumptionHomeFragment(File files_dir) {
        String files = files_dir.toString();
        cj = new CustomJson(new File(files, "data.json"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consumption_home, container, false);

        currentDay = cj.getFoodLastDay(); // spaghetti since we are prepopulating

        globalAddFoodButton = (Button) view.findViewById(R.id.addFoodButton);
        globalAddFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) { createAddFoodDialog(); }
        });

        seeGraphButton = (Button) view.findViewById(R.id.seeGraphButton);
        seeGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { createGraphDialog(); }
        });

        foodEaten = (ListView) view.findViewById(R.id.foodEaten);
        populateFoodsEaten();
        adapterFoodEaten = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, foodsEaten);
        foodEaten.setAdapter(adapterFoodEaten);

        // Progress Bar
        caloriePB = (ProgressBar) view.findViewById(R.id.calorieProgressBar);
        caloriePBLabel = (TextView) view.findViewById(R.id.caloriePBLabel);
        proteinPB = (ProgressBar) view.findViewById(R.id.proteinProgressBar);
        proteinPBLabel = (TextView) view.findViewById(R.id.proteinPBLabel);
        sodiumPB = (ProgressBar) view.findViewById(R.id.sodiumProgressBar);
        sodiumPBLabel = (TextView) view.findViewById(R.id.sodiumPBLabel);

        updateProgressBars();

        return view;
    }

    private void updateProgressBars() {
        HashMap<String, String> foodOnLastDay = cj.getFoodDay(currentDay);
        foodData = cj.getFoodData();

        int caloriesTotal = 0;
        int proteinTotal = 0;
        int sodiumTotal = 0;

        // Dear programming gods this is O(n^2) and I know it's horrible but i promise
        // it's not my fault. So please forgive me just this once :((
        for (String food: foodOnLastDay.keySet()) {
            int servings = Integer.parseInt(foodOnLastDay.get(food));

            for (int i = 0; i < foodData.size(); i ++) {
                HashMap<String, String> currFoodData = foodData.get(i);
                if (currFoodData.get("Name").equals(food)) {

                    caloriesTotal += Integer.parseInt(currFoodData.get("Calories")) * servings;
                    proteinTotal += Integer.parseInt(currFoodData.get("Protein")) * servings;
                    sodiumTotal += Integer.parseInt(currFoodData.get("Sodium")) * servings;

                    break;
                }
            }
        }

        // TODO: POPULATE WITH VALUES FROM GOALS. THESE ARE JUST DUMMY (2000)
        caloriePB.setMax(2000);
        caloriePB.setProgress(caloriesTotal);
        caloriePBLabel.setText(caloriesTotal + "/2000");

        proteinPB.setMax(2000);
        proteinPB.setProgress(proteinTotal);
        proteinPBLabel.setText(proteinTotal + "/2000");

        sodiumPB.setMax(2000);
        sodiumPB.setProgress(sodiumTotal);
        sodiumPBLabel.setText(sodiumTotal + "/2000");
    }

    private void populateFoodsEaten() {
        HashMap<String, String> foodOnLastDay = cj.getFoodDay(currentDay);

        ArrayList<String> foodsEatenAL = new ArrayList<>();
        for (String food: foodOnLastDay.keySet()) {
            foodsEatenAL.add(food + ": " + foodOnLastDay.get(food) + " servings");
        }
        foodsEaten = foodsEatenAL.toArray(new String[foodsEatenAL.size()]);

        if (adapterFoodEaten != null) {
            adapterFoodEaten = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, foodsEaten);
            foodEaten.setAdapter(adapterFoodEaten);
        }
    }

    private void populateFoods() {
        foodData = cj.getFoodData();
        FOODS = new String[foodData.size()];

        for (int i = 0; i < foodData.size(); i ++) {
            FOODS[i] = foodData.get(i).get("Name");
        }

        // I know this is jank
        if (adapterExistingFood != null) {
            adapterExistingFood = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, FOODS);
            addExistingFoodAutoComplete.setAdapter(adapterExistingFood);
        }
    }

    private void createGraphDialog() {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View graphView = getLayoutInflater().inflate(R.layout.consumption_graph, null);

        caloriesOverTime = (LineChart) graphView.findViewById(R.id.caloriesOverTimeChart);

        // Get Entries
        ArrayList<Entry> values = new ArrayList<>();
        // This is hardcoded
        ArrayList<HashMap<String, String>> graphCalories = cj.getGraphStats();

        for (int i = 0; i < graphCalories.size(); i ++) {
            HashMap<String, String> curr = graphCalories.get(i);

            values.add(new Entry(i+1, Integer.parseInt(curr.get("Calories"))));
        }

        LineDataSet set1 = new LineDataSet(values, "Calories");
        set1.setDrawCircles(true);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);

        caloriesOverTime.setData(data);
        caloriesOverTime.getAxisLeft().setDrawGridLines(false);
        caloriesOverTime.getXAxis().setDrawGridLines(false);
        caloriesOverTime.getDescription().setEnabled(false);
        caloriesOverTime.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        dialogBuilder.setView(graphView);
        dialog = dialogBuilder.create();
        dialog.show();
    }
    private void createAddFoodDialog() {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View addFoodView = getLayoutInflater().inflate(R.layout.fragment_consumption, null);

        // AutoCompleteTextView
        addExistingFoodAutoComplete = (AutoCompleteTextView) addFoodView.findViewById(R.id.addFoodItem);
        populateFoods();
        adapterExistingFood = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, FOODS);
        addExistingFoodAutoComplete.setThreshold(1);
        addExistingFoodAutoComplete.setAdapter(adapterExistingFood);
        addExistingFoodAutoComplete.setTextColor(Color.RED);


        addExistingFoodAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                selectedFood = (String) parent.getItemAtPosition(pos);
                addExistingFoodItemButton.setText("Add " + selectedFood);
            }
        });

        addExistingFoodItemButton = (Button) addFoodView.findViewById(R.id.addExistingFoodItem);
        addExistingFoodItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectedFood.equals("")) {
                    createExistingFoodDialog();
                }
            }
        });

        addOtherFoodItemButton = (Button) addFoodView.findViewById(R.id.addOtherFoodItem);
        addOtherFoodItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAddOtherFoodDialog();
            }
        });


        dialogBuilder.setView(addFoodView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void createExistingFoodDialog() {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View addExistingFoodView = getLayoutInflater().inflate(R.layout.add_existing_food_dialog, null);

        general = (TextView) addExistingFoodView.findViewById(R.id.general);
        numServings = (EditText) addExistingFoodView.findViewById(R.id.numServings);
        caloriesOverview = (TextView) addExistingFoodView.findViewById(R.id.caloriesOverview);
        sodiumOverview = (TextView) addExistingFoodView.findViewById(R.id.sodiumOverview);
        sugarOverview = (TextView) addExistingFoodView.findViewById(R.id.sugarOverview);
        proteinOverview = (TextView) addExistingFoodView.findViewById(R.id.proteinOverview);

        general.setText("How many servings of " + selectedFood + " did you have? " +
                "\n(Values are per serving)");

        for (int i = 0; i < foodData.size(); i ++) {
            HashMap<String, String> curr = foodData.get(i);

            if (curr.get("Name").equals(selectedFood)) {
                String calories = curr.get("Calories");
                String sodium = curr.getOrDefault("Sodium", "N/A");
                String sugar = curr.getOrDefault("Sugar", "N/A");
                String protein = curr.get("Protein");

                caloriesOverview.setText("Calories: " + calories);
                sodiumOverview.setText("Sodium: " + sodium);
                sugarOverview.setText("Sugar: " + sugar);
                proteinOverview.setText("Protein: " + protein);

                break;
            }
        }

        addExistingFoodSave = (Button) addExistingFoodView.findViewById(R.id.addExistingFoodSave);
        addExistingFoodSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String foodNameInput = selectedFood;
                String servingsInput = numServings.getText().toString();

                cj.addFoodForDay(foodNameInput, servingsInput, currentDay);

                populateFoodsEaten();
                updateProgressBars();

                dialog.dismiss();
            }
        });

        addExistingFoodCancel = (Button) addExistingFoodView.findViewById(R.id.addExistingFoodCancel);
        addExistingFoodCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialogBuilder.setView(addExistingFoodView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void createAddOtherFoodDialog() {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View addOtherFoodView = getLayoutInflater().inflate(R.layout.add_other_food_dialog, null);

        foodName = (EditText) addOtherFoodView.findViewById(R.id.foodName);
        servings = (EditText) addOtherFoodView.findViewById(R.id.servings);
        calories = (EditText) addOtherFoodView.findViewById(R.id.calories);
        sodium = (EditText) addOtherFoodView.findViewById(R.id.sodium);
        sugar = (EditText) addOtherFoodView.findViewById(R.id.sugar);
        protein = (EditText) addOtherFoodView.findViewById(R.id.protein);

        addOtherFoodSave = (Button) addOtherFoodView.findViewById(R.id.addOtherFoodItemSave);
        addOtherFoodCancel = (Button) addOtherFoodView.findViewById(R.id.addOtherFoodItemCancel);

        addOtherFoodSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Might want to make it so that if no food name or calories we cant accept
                String foodNameText = foodName.getText().toString();
                int caloriesNum = Integer.parseInt(calories.getText().toString());
                int servingsNum = Integer.parseInt(servings.getText().toString());

                int sodiumNum;
                if (sodium.getText().toString().trim().length() == 0) {
                    sodiumNum = -1;
                }
                else {
                    sodiumNum = Integer.parseInt(sodium.getText().toString());
                }

                int sugarNum;
                if (sugar.getText().toString().trim().length() == 0) {
                    sugarNum = -1;
                }
                else {
                    sugarNum = Integer.parseInt(sugar.getText().toString());
                }

                int proteinNum;
                if (protein.getText().toString().trim().length() == 0) {
                    proteinNum = -1;
                }
                else {
                    proteinNum = Integer.parseInt(protein.getText().toString());
                }

//                Log.d("myTag", foodNameText);
//                Log.d("myTag", "servings " + servingsNum);
//                Log.d("myTag", "calories:" + caloriesNum);
//                Log.d("myTag", "sodium:" + sodiumNum);
//                Log.d("myTag", "sugar:" + sugarNum);
//                Log.d("myTag", "protein:" + proteinNum);

                HashMap<String, String> newFood = new HashMap<String, String>();
                newFood.put("Name", foodNameText);
                newFood.put("Calories", String.valueOf(caloriesNum));
                newFood.put("Protein", String.valueOf(proteinNum));
                newFood.put("Sodium", String.valueOf(sodiumNum));

                if (sugarNum != -1) {
                    newFood.put("Sugar", String.valueOf(sugarNum));
                }

                cj.saveFood(newFood);  //add food to dict
                cj.addFoodForDay(foodNameText, String.valueOf(servingsNum), currentDay); //add food that was eaten today

                populateFoods();  // For Autocompletetextview to remember
                populateFoodsEaten(); // For listview of eaten items
                updateProgressBars();

                dialog.dismiss();
            }
        });

        addOtherFoodCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialogBuilder.setView(addOtherFoodView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        cj.writeFile();
    }
}
