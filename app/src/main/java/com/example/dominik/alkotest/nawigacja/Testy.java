package com.example.dominik.alkotest.nawigacja;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dominik.alkotest.R;
import com.example.dominik.alkotest.firebase.ScoreInfo;
import com.example.dominik.alkotest.testy.Test1;
import com.example.dominik.alkotest.testy.Test2;
import com.example.dominik.alkotest.testy.Test2Gra;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * Klasa odpowiadająca za obsługę dwóch testów oraz zapisu wyniku
 */


public class Testy extends AppCompatActivity {

    public String wynikTest2;
    ScoreInfo scoreInfo = new ScoreInfo();
    protected ArrayList<Double> wynikTest1 = new ArrayList<>();
    private String TAG = "Log." + this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);
        Button save = findViewById(R.id.zapisz);
        scoreInfo.setuID(FirebaseAuth.getInstance().getUid());
        save.setOnClickListener(v -> {
            if (scoreInfo.getScore2() != null && scoreInfo.getScore1() != null)
                save();
            else {
                Toast.makeText(Testy.this, "Brak wyników testów.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void test1(View view) {

        Intent myIntent = new Intent(view.getContext(), Test1.class);
        startActivityForResult(myIntent, 1);
    }

    /**
     * przejscie do test2 oraz request na wynik tego testu
     *
     * @param view widok
     */
    public void test2(View view) {

        Intent myIntent = new Intent(view.getContext(), Test2.class);
        startActivityForResult(myIntent, 2);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Double[] doubleArray = ArrayUtils.toObject(data.getDoubleArrayExtra("scoreValue"));
                ArrayList<Double> temp = new ArrayList<>(Arrays.asList(doubleArray));
                wynikTest1 = temp;
                scoreInfo.setScore1(wynikTest1);
                Log.i(TAG, wynikTest1.toString());
            }
            if (requestCode == 2) {
                wynikTest2 = data.getStringExtra("scoreValue2");
                scoreInfo.setScore2(wynikTest2);
                Log.i(TAG, wynikTest2);
            }
        }

    }

    /**
     * zapis wyniku do firebase
     */

    private void save() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("scores").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .set(scoreInfo.getScoreInfo())
                .addOnCompleteListener(Testy.this, task -> {
                    if (task.isSuccessful()) {

                        Log.d(TAG, "update:success" + scoreInfo.toString());

                        Toast.makeText(Testy.this, "Dane zostały wysłane.",
                                Toast.LENGTH_SHORT).show();
                        Intent homeIntent = new Intent(Testy.this, Home.class);
                        startActivity(homeIntent);
                    } else {
                        Log.w(TAG, "update:failure", task.getException());
                        Toast.makeText(Testy.this, "Dane nie zostały wysłane.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}