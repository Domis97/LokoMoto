package com.example.dominik.alkotest.firebase;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dominik.alkotest.R;
import com.example.dominik.alkotest.ScoreInfo;
import com.example.dominik.alkotest.VariableInfo;
import com.example.dominik.alkotest.nawigacja.Testy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa odpowiadająca porownanie wynikow testow
 */


public class Wynik extends AppCompatActivity {

    public String zmienna;
    ScoreInfo scoreInfo = new ScoreInfo();
    VariableInfo variableInfo = new VariableInfo();
    private String wybor;
    private String wynik1;
    private String wynik2;
    private long wynik1L;
    private long wynik2L;
    private long wynik;
    private String TAG = "Porownanie wynikow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wynik);


        Bundle temp = getIntent().getExtras();//przekazani wynik2
        if (temp != null) {
            wynik2 = (String) temp.get("wartosc");
            wynik2L = Long.valueOf(wynik2);
        }

        Button button = findViewById(R.id.porownaj);
        spinner();
        button.setOnClickListener(
                new Button.OnClickListener() {
                    /**
                     * metoda stworzona do porownania wynikow z testu przed i testu po
                     * porownanie wynikow i koncowe stwierdzenie stanu badanego oraz wyswietlenie tej informacji na ekran
                     *
                     * @param v widok
                     */
                    @SuppressLint("SetTextI18n")
                    public void onClick(View v) {
                        pick();
                        Log.d(TAG,"po"+zmienna);

                        profil();
                        wynik1L = Long.valueOf(wynik1);
                        wynik = wynik1L/wynik2L;
                        TextView textView = findViewById(R.id.wynik_koncowy);
                        if (wynik > Long.valueOf(variableInfo.getHigh())) {
                            textView.setText("Stan krytyczny!");
                            Toast.makeText(Wynik.this, "UWAGA MOGŁEŚ ULEC WYPADKOWI.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (wynik > Long.valueOf(variableInfo.getMid())) {
                            textView.setText("Stan problematyczny");
                        } else {
                            textView.setText("Stan poprawny");
                        }
                    }
                }
        );

    }

    /**
     * Pobranie globalnych wartosci porównawczych z bazy firebase
     */
    public void pick() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("variable").document(wybor);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data variable: " + document.getData());
                        variableInfo = document.toObject(VariableInfo.class);
                        zmienna = variableInfo.getHigh();
                        Log.d(TAG,"przed"+zmienna);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        Log.d(TAG,"po"+zmienna);
    }


    /**
     * wypisanie listy utworonych wczesniej kont
     */

    private void spinner() {

        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("own");
        spinnerArray.add("notown");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = findViewById(R.id.skala);
        sItems.setAdapter(adapter);

        sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wybor = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @SuppressLint("SetTextI18n")
    private void profil() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("scores").document(FirebaseAuth.getInstance().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data scores: " + document.getData());
                        scoreInfo = document.toObject(ScoreInfo.class);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        wynik1=scoreInfo.getScore1();
        TextView textView = findViewById(R.id.wynik_text);
        textView.setText("Wynik 1 to: " + wynik1);

        TextView textView1 = findViewById(R.id.wynik_Po);
        textView1.setText("Wynik 2 to: " + wynik2);

    }


}
