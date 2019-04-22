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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Thread.sleep;

/**
 * Klasa odpowiadająca porownanie wynikow testow
 */


public class Wynik extends AppCompatActivity {

    ScoreInfo scoreInfo = new ScoreInfo();
    VariableInfo variableInfo = new VariableInfo();
    private String wybor;
    private String wynik1;
    private String wynik2;
    private String TAG = "Porownanie wynikow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wynik);


        Bundle temp = getIntent().getExtras();//przekazani wynik2
        if (temp != null) {
            wynik2 = (String) temp.get("wartosc");
        }

        Button button = findViewById(R.id.porownaj);
        spinner();
        button.setOnClickListener(
                new Button.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    public void onClick(View v) {
                        wybor();
                    }
                }
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
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
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
            }
        });
    }


    /**
     * pobranie wartosci z testu pierwszego dla danego uzytkownika
     */


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
                        wynik1= (scoreInfo).getScore2();
                        TextView textView = findViewById(R.id.wynik_text);
                        textView.setText("Wynik 1 to: " + wynik1);

                        TextView textView1 = findViewById(R.id.wynik_Po);
                        textView1.setText("Wynik 2 to: " + wynik2);
                        porwownaj();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * metoda stworzona do porownania wynikow z testu przed i testu po
     * porownanie wynikow i koncowe stwierdzenie stanu badanego oraz wyswietlenie tej informacji na ekran
     *
     */
    public void porwownaj(){
        Log.d(TAG,"wynik1 to : "+wynik1);
        long wynik = Long.valueOf(wynik1) / Long.valueOf(wynik2);
        TextView textView = findViewById(R.id.wynik_koncowy);
        if (wynik > Long.valueOf(variableInfo.getHigh())/10) {
            textView.setText("Stan krytyczny!");
            Toast.makeText(Wynik.this, "UWAGA MOGŁEŚ ULEC WYPADKOWI.",
                    Toast.LENGTH_SHORT).show();
        } else if (wynik > Long.valueOf(variableInfo.getMid())/10) {
            textView.setText("Stan problematyczny");
        } else {
            textView.setText("Stan poprawny");
        }
    }


}
