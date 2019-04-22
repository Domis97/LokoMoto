package com.example.dominik.alkotest.nawigacja;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dominik.alkotest.R;
import com.example.dominik.alkotest.ScoreInfo;
import com.example.dominik.alkotest.VariableInfo;
import com.example.dominik.alkotest.firebase.Wynik;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Home extends AppCompatActivity {

    private Button before;
    private Button after;
    private String TAG = "HOME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        before = findViewById(R.id.before);
        after = findViewById(R.id.after);
        setUpButtonListeners();
    }

    public void setUpButtonListeners() {
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testy = new Intent(Home.this, Testy.class);
                startActivity(testy);
            }
        });

        after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sprwadz();
            }
        });
    }

    /**
     * sprawdzenie wartosci z pierwszgo testu
     */
    private void sprwadz(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("scores").document(FirebaseAuth.getInstance().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ScoreInfo scoreInfo = document.toObject(ScoreInfo.class);
                        if(scoreInfo.getScore2() == null){
                            Toast.makeText(Home.this, "Nastąpił bład przy pobieraniu wyników pierwszego testu.",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Intent testy = new Intent(Home.this, TestyPo.class);
                            startActivity(testy);
                        }
                    } else {
                        Log.d(TAG, "No such user");
                        Toast.makeText(Home.this, "Najpierw wykonaj pierwszy test.",
                                Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "LOG2");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
