package com.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.*;
import android.view.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    private TextView tvScore, tvRandomizedNumber, tvScoreWon, tvWinTitle;
    private EditText etChosenNumber;
    private Button btnRandomizer;
    private View.OnClickListener btnRandomizerListener;
    private View.OnKeyListener etChosenNumberListener;

    private ThreadLocalRandom rand = ThreadLocalRandom.current();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

         btnRandomizerListener = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (etChosenNumber.getText().toString().isEmpty() || !TextUtils.isDigitsOnly(etChosenNumber.getText().toString()) || Integer.parseInt(etChosenNumber.getText().toString()) > 100 || Integer.parseInt(etChosenNumber.getText().toString()) < 1){
                    Toast.makeText(MainActivity.this, "Please choose a proper number", Toast.LENGTH_SHORT).show();
                }
                else{
                    int randomizedNumber = rand.nextInt(1, 101);
                    tvRandomizedNumber.setText(String.valueOf(randomizedNumber));

                    int chosenNumber = Integer.parseInt(etChosenNumber.getText().toString());
                    int scoreWon = Math.abs(randomizedNumber-chosenNumber);

                    if (scoreWon != 0){
                        tvWinTitle.setText("You lost :(");
                        tvScoreWon.setText("-"+String.valueOf(scoreWon));
                        tvScoreWon.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.darkRed));
                        tvScore.setText(String.valueOf(Integer.parseInt(tvScore.getText().toString())-scoreWon));
                    }
                    else{
                        tvWinTitle.setText("You won!!!");
                        scoreWon = randomizedNumber*2;
                        tvScoreWon.setText(String.valueOf(scoreWon));
                        tvScoreWon.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    }
                    if (Integer.parseInt(tvScore.getText().toString()) < 0){
                        Toast.makeText(MainActivity.this, "You lost everything", Toast.LENGTH_SHORT).show();
                        disableInteraction();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                resetGame();
                                enableInteraction();
                            }
                        }, 5000);
                    }
                }
            }
        };

         etChosenNumberListener = new View.OnKeyListener(){
             @Override
             public boolean onKey(View view, int keyCode, KeyEvent event){
                 if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                     btnRandomizerListener.onClick(btnRandomizer);
                     return true;
                 }
                 return false;
             }
         };

         btnRandomizer.setOnClickListener(btnRandomizerListener);
         etChosenNumber.setOnKeyListener(etChosenNumberListener);
    }

    public void init(){
        tvScore = findViewById(R.id.tvScore);
        tvRandomizedNumber = findViewById(R.id.tvRandomizedNumber);
        etChosenNumber = findViewById(R.id.etChosenNumber);
        btnRandomizer = findViewById(R.id.btnRandomizer);
        tvScoreWon = findViewById(R.id.tvScoreWon);
        tvWinTitle = findViewById(R.id.tvWinTitle);
    }

    public void resetGame(){
        tvScore.setText(getString(R.string.tvScoreString));
        etChosenNumber.setText("");
        tvRandomizedNumber.setText(getString(R.string.tvRandomizedNumberString));
        tvWinTitle.setText(getString(R.string.tvWinTitleString));
        tvScoreWon.setText("0");
        tvScoreWon.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
    }

    public void disableInteraction(){
        btnRandomizer.setEnabled(false);
        etChosenNumber.setEnabled(false);
    }
    public void enableInteraction(){
        btnRandomizer.setEnabled(true);
        etChosenNumber.setEnabled(true);
    }

}