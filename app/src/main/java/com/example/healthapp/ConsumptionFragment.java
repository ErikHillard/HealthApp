package com.example.healthapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Color;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ConsumptionFragment extends Fragment {

    ArrayAdapter<String> adapter;
    CustomJson cj;
    View view;
    private AutoCompleteTextView addFood;
    private Button addOtherFoodItemButton, addExistingFoodItemButton;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    // Other Food Stuff
    private EditText foodName, servings, calories, sodium, sugar, protein;
    private Button addOtherFoodSave, addOtherFoodCancel;

    // Existing Food Stuff
    private String selectedFood = "";
    private TextView caloriesOverview, sodiumOverview, sugarOverview, proteinOverview, general;
    private Button addExistingFoodSave, addExistingFoodCancel;

    private ArrayList<HashMap<String, String>> foodData;
    private String[] FOODS;

    public ConsumptionFragment(File files_dir) {
        String files = files_dir.toString();
        cj = new CustomJson(new File(files, "data.json"));

        populateFoods();
    }

    private void populateFoods() {
        foodData = cj.getFoodData();
        FOODS = new String[foodData.size()];

        for (int i = 0; i < foodData.size(); i ++) {
            FOODS[i] = foodData.get(i).get("Name");
            Log.e("myTag", FOODS[i]);
        }

        if (adapter != null) {
            adapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, FOODS);
            addFood.setAdapter(adapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consumption, container, false);

        addFood = (AutoCompleteTextView) view.findViewById(R.id.addFoodItem);
        adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, FOODS);
        addFood.setThreshold(1);
        addFood.setAdapter(adapter);
        addFood.setTextColor(Color.RED);

        addFood.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                selectedFood = (String) parent.getItemAtPosition(pos);
                addExistingFoodItemButton.setText("Add " + selectedFood);
            }
        });

        addExistingFoodItemButton = (Button) view.findViewById(R.id.addExistingFoodItem);
        addExistingFoodItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectedFood.equals("")) {
                    createExistingFoodDiaglog();
                }
            }
        });

        addOtherFoodItemButton = (Button) view.findViewById(R.id.addOtherFoodItem);
        addOtherFoodItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAddOtherFoodDialog();
            }
        });

        return view;
    }

    // Want params for preexisting nutritional info
    public void createExistingFoodDiaglog() {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View addExistingFoodView = getLayoutInflater().inflate(R.layout.add_existing_food_dialog, null);
        general = (TextView) addExistingFoodView.findViewById(R.id.general);
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

    public void createNewAddOtherFoodDialog() {
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

                Log.d("myTag", foodNameText);
                Log.d("myTag", "servings " + servingsNum);
                Log.d("myTag", "calories:" + caloriesNum);
                Log.d("myTag", "sodium:" + sodiumNum);
                Log.d("myTag", "sugar:" + sugarNum);
                Log.d("myTag", "protein:" + proteinNum);

                HashMap<String, String> newFood = new HashMap<String, String>();
                newFood.put("Name", foodNameText);
                newFood.put("Calories", String.valueOf(caloriesNum));
                newFood.put("Protein", String.valueOf(proteinNum));

                cj.saveFood(newFood);
                populateFoods();

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