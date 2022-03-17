package com.example.healthapp;

import android.app.AlertDialog;
import android.os.Bundle;

import android.graphics.Color;


import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;



public class ConsumptionFragment extends Fragment {

    View view;
    private AutoCompleteTextView addFood;
    private Button addOtherFoodItemButton;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText foodName, calories, sodium, sugar;
    private Button addOtherFoodSave, addOtherFoodCancel;


    private String[] FOODS = {
            "Chicken Breast",
            "Chicken Parmesan",
            "Chicken Sandwich (Popeyes)",
            "Chicken Thigh"
    };

    public ConsumptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_consumption, container, false);


        addFood = (AutoCompleteTextView) view.findViewById(R.id.addFoodItem);
        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, FOODS);
        //Getting the instance of AutoCompleteTextView
        addFood.setThreshold(1);//will start working from first character
        addFood.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        addFood.setTextColor(Color.RED);


        addOtherFoodItemButton = (Button) view.findViewById(R.id.addOtherFoodItem);
        addOtherFoodItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAddOtherFoodDialog();
            }
        });

        return view;


    }

    public void createNewAddOtherFoodDialog() {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View addOtherFoodView = getLayoutInflater().inflate(R.layout.add_other_food_dialog, null);

        foodName = (EditText) addOtherFoodView.findViewById(R.id.foodName);
        calories = (EditText) addOtherFoodView.findViewById(R.id.calories);
        sodium = (EditText) addOtherFoodView.findViewById(R.id.sugar);
        sugar = (EditText) addOtherFoodView.findViewById(R.id.sugar);

        addOtherFoodSave = (Button) addOtherFoodView.findViewById(R.id.addOtherFoodItemSave);
        addOtherFoodCancel = (Button) addOtherFoodView.findViewById(R.id.addOtherFoodItemCancel);

        dialogBuilder.setView(addOtherFoodView);
        dialog = dialogBuilder.create();
        dialog.show();

        addOtherFoodSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // define save button.
            }
        });

        addOtherFoodCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }



}