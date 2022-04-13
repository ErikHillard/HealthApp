package com.example.healthapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ArrayList<NumbersView> arrayList = new ArrayList<NumbersView>();

        // add all the values from 1 to 15 to the arrayList
        // the items are of the type NumbersView
        arrayList.add(new NumbersView(R.drawable.ic_launcher_foreground, "1", "One"));
        arrayList.add(new NumbersView(R.drawable.ic_launcher_foreground, "2", "Two"));
        arrayList.add(new NumbersView(R.drawable.ic_launcher_foreground, "3", "Three"));
        arrayList.add(new NumbersView(R.drawable.ic_launcher_foreground, "4", "Four"));
        arrayList.add(new NumbersView(R.drawable.ic_launcher_foreground, "5", "Five"));
        arrayList.add(new NumbersView(R.drawable.ic_launcher_foreground, "6", "Six"));
        arrayList.add(new NumbersView(R.drawable.ic_launcher_foreground, "7", "Seven"));
        arrayList.add(new NumbersView(R.drawable.ic_launcher_foreground, "8", "Eight"));
        arrayList.add(new NumbersView(R.drawable.ic_launcher_foreground, "9", "Nine"));
        arrayList.add(new NumbersView(R.drawable.ic_launcher_foreground, "10", "Ten"));
        arrayList.add(new NumbersView(R.drawable.ic_launcher_foreground, "11", "Eleven"));
        arrayList.add(new NumbersView(R.drawable.ic_launcher_foreground, "12", "Twelve"));
        arrayList.add(new NumbersView(R.drawable.ic_launcher_foreground, "13", "Thirteen"));
        arrayList.add(new NumbersView(R.drawable.ic_launcher_foreground, "14", "Fourteen"));
        arrayList.add(new NumbersView(R.drawable.ic_launcher_foreground, "15", "Fifteen"));

        // Now create the instance of the NumebrsViewAdapter and pass
        // the context and arrayList created above
        NumbersViewAdapter numbersArrayAdapter = new NumbersViewAdapter(getActivity().getApplicationContext(), arrayList);

        // create the instance of the ListView to set the numbersViewAdapter
        ListView numbersListView = getActivity().findViewById(R.id.listView);

        // set the numbersViewAdapter for ListView
        numbersListView.setAdapter(numbersArrayAdapter);
    }
}