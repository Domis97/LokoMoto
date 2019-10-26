package com.example.dominik.alkotest.nawigacja;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dominik.alkotest.R;
import com.example.dominik.alkotest.Usun;
import com.example.dominik.alkotest.firebase.ScoreInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Home extends AppCompatActivity {

    private Button before;
    private Button after;
    private String TAG = "Log." + this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        before = findViewById(R.id.before);
        after = findViewById(R.id.after);
        setUpButtonListeners();
    }

    /**
     * Przelaczenie do ekranu usuwania konta przez przycisk
     */

    public void usun(View view) {

        android.content.Intent myIntent = new android.content.Intent(view.getContext(), Usun.class);
        startActivity(myIntent);

    }

    public void setUpButtonListeners() {
        before.setOnClickListener(v -> {
            Intent testy = new Intent(Home.this, Testy.class);
            startActivity(testy);
        });

        after.setOnClickListener(v -> sprwadz());
    }

    /**
     * sprawdzenie wartosci z pierwszgo testu
     */
    private void sprwadz(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("scores").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    ScoreInfo scoreInfo = document.toObject(ScoreInfo.class);
                    if (scoreInfo.getScore2() == null || scoreInfo.getScore1() == null) {
                        Toast.makeText(Home.this, "Nastąpił bład przy pobieraniu wyników testu.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Intent testy = new Intent(Home.this, TestyPorownawcze.class);
                        startActivity(testy);
                    }
                } else {
                    Log.d(TAG, "No such user");
                    Toast.makeText(Home.this, "Najpierw wykonaj test.",
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "LOG2");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }
}
