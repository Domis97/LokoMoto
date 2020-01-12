package com.example.dominik.alkotest.testy;


import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dominik.alkotest.R;

import java.util.Random;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * Klasa odpowiadająca za obsługę test2
 */
public class Test2 extends AppCompatActivity {


    private String TAG = "Log." + this.getClass().getName();
    private Test2Gra test2Gra;
    private Random randomGenerator;
    private Button randomButton;
    private ArrayList<Button> buttonArrayList;
    volatile private long waitingTime;


    private View.OnClickListener clicker = new View.OnClickListener() {
        /**
         * funkcja realizujaca gre w test2 sprwadzenie czy nacisniety przycisk jest
         * wczesniej wybranym przyciskiem oraz zmiana koloru
         * sleep potrzebny by uniknac konczenia przed pobraniem wyniku z watku do wyswietlenia
         *
         * @param v widok
         */
        @Override
        public void onClick(View v) {
            Button clickedButtonID = findViewById(v.getId());
            if (clickedButtonID == randomButton) {
                clickedButtonID.setBackgroundResource(R.drawable.buttonshape);
                test2Gra.setWaitingForClick(false);
            }
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TextView textView = findViewById(R.id.wynik);
            textView.setText(test2Gra.getShow());

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2_pre);
        int SPLASH_TIME_OUT = 4500;
        new Handler().postDelayed(() -> {
            setContentView(R.layout.activity_test2);

            randomGenerator = new Random();
            buttonArrayList = new ArrayList<>();

            buttonArrayList.add(findViewById(R.id.button1));
            buttonArrayList.add(findViewById(R.id.button2));
            buttonArrayList.add(findViewById(R.id.button3));
            buttonArrayList.add(findViewById(R.id.button4));
            buttonArrayList.add(findViewById(R.id.button5));
            buttonArrayList.add(findViewById(R.id.button6));
            buttonArrayList.add(findViewById(R.id.button7));
            buttonArrayList.add(findViewById(R.id.button8));
            buttonArrayList.add(findViewById(R.id.button9));

            for (int i = 0; i < 9; i++) {
                buttonArrayList.get(i).setOnClickListener(clicker);
            }

            runGame();
        }, SPLASH_TIME_OUT);

//        setContentView(R.layout.activity_test2);
//
//        randomGenerator = new Random();
//        buttonArrayList = new ArrayList<>();
//
//        buttonArrayList.add(findViewById(R.id.button1));
//        buttonArrayList.add(findViewById(R.id.button2));
//        buttonArrayList.add(findViewById(R.id.button3));
//        buttonArrayList.add(findViewById(R.id.button4));
//        buttonArrayList.add(findViewById(R.id.button5));
//        buttonArrayList.add(findViewById(R.id.button6));
//        buttonArrayList.add(findViewById(R.id.button7));
//        buttonArrayList.add(findViewById(R.id.button8));
//        buttonArrayList.add(findViewById(R.id.button9));
//
//        for (int i = 0; i < 9; i++) {
//            buttonArrayList.get(i).setOnClickListener(clicker);
//        }
//
//        runGame();


    }

    /**
     * Metoda odpowiadająca za wybranie losowego przycisku oraz zapalenie go na inny kolor
     */

    public void chooseRandomButton() {
        int choosenButtonIndex = randomGenerator.nextInt(buttonArrayList.size());
        randomButton = buttonArrayList.get(choosenButtonIndex);
        Test2.this.runOnUiThread(() -> randomButton.setBackgroundResource(R.drawable.target));

    }


    private void runGame() {
        test2Gra = new Test2Gra(this);
        test2Gra.start();
    }

    /**
     * setter ustawiajacy wynikTest1 na waitingTime
     * @param waitingTime wynikTest1 ustawiana na waitingTime
     */
    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime;
    }

    /**
     * ustawia result intenta (aktywności) na wynikTest1 String przekazana w funkcji
     * @param score2
     * nastepnie metoda konczy aktwynosc test2
     */

    public void finishGame(String score2) {

        Intent intent = new Intent();
        intent.putExtra("scoreValue2", score2);
        setResult(RESULT_OK, intent);

        try {
            sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Test2.this.runOnUiThread(() -> finish());


    }


}