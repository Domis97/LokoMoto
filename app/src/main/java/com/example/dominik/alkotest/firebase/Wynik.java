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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Klasa odpowiadająca porownanie wynikow testow
 */


public class Wynik extends AppCompatActivity {

    ScoreInfo scoreInfo = new ScoreInfo();
    VariableInfo variableInfo = new VariableInfo();
    private String wybor;
    TextView textWynik_text;
    TextView textWynik_po;
    private String test2Wynik;
    private String test2PoWynik;
    private ArrayList<Double> test1Wynik;
    private ArrayList<Double> test1PoWynik;
    private String TAG = "Log." + this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wynik);

        textWynik_text = findViewById(R.id.wynik_text);
        textWynik_po = findViewById(R.id.wynik_Po);

        Bundle extras = getIntent().getExtras();//przekazanie wyników
        if (extras != null) {
            test1PoWynik = (ArrayList<Double>) extras.get("wynikTest1");
            test2PoWynik = (String) extras.get("wynikTest2");

        }

        Button button = findViewById(R.id.porownaj);
        spinner();
        button.setOnClickListener(
                v -> wybor()
        );

    }

    /**
     * wypisanie listy utworonych przez administratora wartosci porównawczych
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


    /**
     * Pobranie globalnych wartosci porównawczych z bazy firebase
     */
    public void wybor() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("variable").document(wybor);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data variable: " + document.getData());
                    variableInfo = document.toObject(VariableInfo.class);
                    profil();
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }


    /**
     * pobranie wartosci z testu pierwszego dla danego uzytkownika
     */


    @SuppressLint("SetTextI18n")
    private void profil() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("scores").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data scores: " + document.getData());
                    scoreInfo = document.toObject(ScoreInfo.class);
                    if (scoreInfo != null) {
                        test1Wynik = (scoreInfo).getScore1();
                        test2Wynik = (scoreInfo).getScore2();


                        textWynik_text.setText("Wynik bazowy to: \n" + showTest1Wynik(test1Wynik) + "  " + test2Wynik);


                        textWynik_po.setText("Wynik porownawczy to \n:" + showTest1Wynik(test1PoWynik) + "  " + test2PoWynik);
                        porwownaj();
                    } else
                        textWynik_text.setText("Blad przy pobieraniu;");
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    /**
     * metoda stworzona do porownania wynikow z testu przed i testu po
     * porownanie wynikow i koncowe stwierdzenie stanu badanego oraz wyswietlenie tej informacji na ekran
     */
    public void porwownaj() {
        long wynik = Long.valueOf(test2Wynik) / Long.valueOf(test2PoWynik);
        TextView textView = findViewById(R.id.wynik_koncowy);
        if (wynik > Long.valueOf(variableInfo.getHigh()) / 10) {
            textView.setText("Stan krytyczny!");
            Toast.makeText(Wynik.this, "UWAGA MOGŁEŚ ULEC WYPADKOWI.",
                    Toast.LENGTH_SHORT).show();
        } else if (wynik > Long.valueOf(variableInfo.getMid()) / 10) {
            textView.setText("Stan problematyczny");
        } else {
            textView.setText("Stan poprawny");
        }
    }

    private String showTest1Wynik(ArrayList<Double> wynik) {
        ArrayList<String> show = new ArrayList<>();
        for (Double dou : wynik) {
            show.add(dou.toString().substring(0, 5));
        }

        return show.toString();
    }


}
