package com.example.dominik.alkotest.nawigacja;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.dominik.alkotest.firebase.ScoreInfo;
import com.example.dominik.alkotest.firebase.VariableInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Klasa odpowiadająca porownanie wynikow testow
 */


public class Wynik extends AppCompatActivity {

    ScoreInfo scoreInfo = new ScoreInfo();
    VariableInfo variableInfo = new VariableInfo();
    TextView textWynik_text;
    TextView textWynik_po;
    private String wybor;
    private String test2Wynik;
    private String test2PoWynik;
    private ArrayList<Double> test1Wynik;
    private ArrayList<Double> test1PoWynik;
    private String globalPercentScore = "";
    private String TAG = "Log." + this.getClass().getName();
    private List<String> spinnerArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spinnerArray = mozliwosci();
        int SPLASH_TIME_OUT = 2000;
        new Handler().postDelayed(() -> {
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
        }, SPLASH_TIME_OUT);

    }

    /**
     * wypisanie listy utworonych przez administratora wartosci porównawczych
     */

    private void spinner() {

//        List<String> spinnerArray = new ArrayList<>();
//        spinnerArray.add("own");
//        spinnerArray.add("notown");


        Log.d(TAG, spinnerArray.toString());
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


    private ArrayList<String> mozliwosci() {
        ArrayList<String> result = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("variable")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                result.add(document.getId());
                            }
                        } else
                            Log.d(TAG, "FireStore ERROR");
                    }
                });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
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

        TextView textView = findViewById(R.id.wynik_koncowy);
        TextView textView1 = findViewById(R.id.wynik_koncowy_procenty);
        int test1 = porownajTest1();
        int test2 = porownajTest2();
        textView1.setText(globalPercentScore);
        if (test1 == 1 && test2 == 1) {
            textView.setText("Stan krytyczny!");
            Toast.makeText(Wynik.this, "UWAGA MOGŁEŚ ULEC WYPADKOWI.",
                    Toast.LENGTH_SHORT).show();
        } else if (test1 == 1 || test2 == 1) {
            textView.setText("Stan zagrożenia!");
        } else if (test1 == 2 || test2 == 2) {
            textView.setText("Stan problematyczny");
        } else if (test1 == 3 || test2 == 3) {
            textView.setText("Stan poprawny");
        } else if (test1 == 0 || test2 == 0) {
            textView.setText("Bład w obliczeniach");
        }
    }

    private String showTest1Wynik(ArrayList<Double> wynik) {
        ArrayList<String> show = new ArrayList<>();
        for (Double dou : wynik) {
            show.add(dou.toString());
        }

        return show.toString();
    }

    private int porownajTest1() {
        if (test1Wynik.size() == 3 && test1PoWynik.size() == 3) {
            Double high = Double.valueOf(variableInfo.getHighT1());
            Double mid = Double.valueOf(variableInfo.getMidT1());


            Double x = (test1PoWynik.get(0)) - (test1Wynik.get(0));
            x = modulo(x / (test1Wynik.get(0)));

            Double y = (test1PoWynik.get(1)) - (test1Wynik.get(1));
            y = modulo((y) / (test1Wynik.get(1)));

            Double z = (test1PoWynik.get(2)) - (test1Wynik.get(2));
            z = modulo(z / (test1Wynik.get(2)));

            ArrayList<Integer> wyniki = new ArrayList<>();
            wyniki.add(porownaj(x, mid, high));
            globalPercentScore += "Test OLS X: " + String.valueOf(x * 100).substring(0, 4) + "%";
            wyniki.add(porownaj(y, mid, high));
            globalPercentScore += "Test OLS Y: " + String.valueOf(y * 100).substring(0, 4) + "%";
            wyniki.add(porownaj(z, mid, high));
            globalPercentScore += "Test OLS Z: " + String.valueOf(z * 100).substring(0, 4) + "%";

            Log.i(TAG, "Wyniki" + wyniki.toString());


            if (wyniki.contains(1))
                return 1;
            else if (wyniki.contains(2))
                return 2;
            else
                return 3;

        } else
            return 0;
    }


    private int porownajTest2() {
        if (!test2PoWynik.equals("0") || !test2Wynik.equals("0")) {
            Double wynik = Double.valueOf(test2PoWynik) - Double.valueOf(test2Wynik);
            wynik = wynik / Double.valueOf(test2Wynik);
            globalPercentScore += "Test Kule: " + String.valueOf(wynik * 100).substring(0, 4) + "%";
            Log.d(TAG, wynik + " " + variableInfo.getHighT2() + " " + variableInfo.getMidT2());
            return porownaj(wynik, Double.valueOf(variableInfo.getHighT2()), Double.valueOf(variableInfo.getMidT2()));
        }
        return 0;
    }

    private Double modulo(Double temp) {
        if (temp < 0) {
            return -temp;
        } else
            return temp;
    }

    private int porownaj(Double roznica, Double mid, Double high) {
        Log.i(TAG, "porownaj" + roznica.toString());
        if (roznica > 0) {
            if (roznica > high / 100) {
                return 1;
            } else if (roznica > mid / 100) {
                return 2;
            } else {
                return 3;
            }
        } else {
            return 3;
        }
    }

}
