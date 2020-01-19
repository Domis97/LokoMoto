package com.example.dominik.alkotest.nawigacja;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.dominik.alkotest.R;
import com.example.dominik.alkotest.firebase.Logowanie;
import com.example.dominik.alkotest.firebase.Rejestracja;

/**
 * Klasa odpowiadająca za obsługę ekranu startowego
 */

public class Main extends AppCompatActivity {

    private String TAG = "Log." + this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Przelaczenie do ekranu rejestrowania nowego konta przez przycisk
     */

    public void rejestruj (View view){

        android.content.Intent myIntent = new android.content.Intent(view.getContext(), Rejestracja.class);
        startActivity(myIntent);


    }

    /**
     * Przelaczenie do ekranu loguj przez przycisk
     */

    public void loguj (View view){

        android.content.Intent myIntent = new android.content.Intent(view.getContext(), Logowanie.class);
        startActivity(myIntent);

    }

}
