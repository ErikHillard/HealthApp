package com.example.healthapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView guessPlayer01;
    TextView guessPlayer02;
    TextView labelPlayer01;
    TextView labelPlayer02;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        guessPlayer01 = ((TextView)findViewById(R.id.labelComputerGuess01));
        guessPlayer02 = ((TextView)findViewById(R.id.labelComputerGuess02));
        labelPlayer01 = ((TextView)findViewById(R.id.labelPlayer01));
        labelPlayer02 = ((TextView)findViewById(R.id.labelPlayer02));

        /*
        Button setPlayer0101 = findViewById(R.id.buttonPlayer01Color01);
        setPlayer0101.setOnClickListener(new View.OnClickListener() {public void onClick(View v) {clearComputerGuesses();}});
        Button setPlayer0102 = findViewById(R.id.buttonPlayer01Color02);
        setPlayer0102.setOnClickListener(new View.OnClickListener() {public void onClick(View v) {clearComputerGuesses();}});
        */
        RadioGroup setPlayer01 = findViewById(R.id.buttonsPlayer01);
        setPlayer01.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup group,int checkedId){
                clearComputerGuesses();
                if (checkedId == R.id.buttonPlayer01Color01) {
                    labelPlayer01.setTextColor(Color.RED);
                } else {
                    labelPlayer01.setTextColor(Color.BLUE);
                }
            }
        });


        RadioGroup setPlayer02 = findViewById(R.id.buttonsPlayer02);
        setPlayer02.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup group,int checkedId){
                clearComputerGuesses();
                if (checkedId == R.id.buttonPlayer02Color01) {
                    labelPlayer02.setTextColor(Color.RED);
                } else {
                    labelPlayer02.setTextColor(Color.BLUE);
                }
            }
        });


        Button buttonComputerGuess = findViewById(R.id.buttonComputerGuess);
        buttonComputerGuess.setTextColor(getResources().getColor(R.color.white));
    }


    public void clearComputerGuesses() {
        guessPlayer01.setText("");
        guessPlayer02.setText("");
    }

    public void setComputerGuesses(View view) {
        String guess01, guess02;

        int colorGuess01 = Color.GRAY;
        int colorGuess02 = Color.GRAY;

        //Player One will guess they are wearing the same hat color as Player Two
//        Spinner setPlayer02 = (Spinner)findViewById(R.id.spinnerPlayer02);
//        guess01 = getResources().getStringArray(R.array.colors_array)[setPlayer02.getSelectedItemPosition()];

        RadioGroup setPlayer02 = (RadioGroup)findViewById(R.id.buttonsPlayer02);
        if (setPlayer02.getCheckedRadioButtonId() == R.id.buttonPlayer02Color01) {
            guess01 = getString(R.string.color01);
        }
        else {
            guess01 = getString(R.string.color02);
        }

        //Player Two will guess they are wearing the other hat color than Player One
        RadioGroup setPlayer01 = (RadioGroup)findViewById(R.id.buttonsPlayer01);
        if (setPlayer01.getCheckedRadioButtonId() == R.id.buttonPlayer01Color01) {
            guess02 = getString(R.string.color02);
        }
        else {

            guess02 = getString(R.string.color01);
        }



        guessPlayer01.setText("My hat color is " + guess01 + ".");
        guessPlayer02.setText("My hat color is " + guess02 + ".");
        guessPlayer01.setTextColor(Color.GRAY);
        guessPlayer02.setTextColor(Color.GRAY);

        if ((setPlayer01.getCheckedRadioButtonId() == R.id.buttonPlayer01Color01
                && guess01.equals("Red")) ||
            (setPlayer01.getCheckedRadioButtonId() == R.id.buttonPlayer01Color02
                && guess01.equals("Blue"))) {
            guessPlayer01.setTextColor(Color.GREEN);
        }

        if ((setPlayer02.getCheckedRadioButtonId() == R.id.buttonPlayer02Color01
                && guess02.equals("Red")) ||
            (setPlayer02.getCheckedRadioButtonId() == R.id.buttonPlayer02Color02
                && guess02.equals("Blue"))) {
            guessPlayer02.setTextColor(Color.GREEN);
        }
    }
}
