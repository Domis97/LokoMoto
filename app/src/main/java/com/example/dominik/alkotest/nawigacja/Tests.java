package com.example.dominik.alkotest.nawigacja;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dominik.alkotest.R;
import com.example.dominik.alkotest.testy.Test1;
import com.example.dominik.alkotest.testy.Test2;
import com.example.dominik.alkotest.testy.Test2Gra;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

/**
 * Klasa odpowiadająca za obsługę dwóch testów oraz zapisu wyniku
 */


public class Tests extends AppCompatActivity {
    protected Map<String, Object> score = new HashMap<>();
    private Test2Gra test2Gra;
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);
        Button save = findViewById(R.id.zapisz);
        score.put("uID", FirebaseAuth.getInstance().getUid());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

    }

    public void test1(View view) {

        Intent myIntent = new Intent(view.getContext(), Test1.class);
        startActivity(myIntent);
    }

    /**
     * przejscie do test2 oraz request na wynik tego testu
     *
     * @param view widok
     */
    public void test2(View view) {

        Intent myIntent = new Intent(view.getContext(), Test2.class);
        int requestCode = 1;
        startActivityForResult(myIntent, requestCode);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        value = data.getStringExtra("someValue");
        score.put("test1", value);
    }

    /**
     * zapis wyniku do firebase
     */

    private void save() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("scores").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .set(score)
                .addOnCompleteListener(Tests.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "update:success");

                            Toast.makeText(Tests.this, "Dane zostały wysłane.",
                                    Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(Tests.this, Home.class);
                            startActivity(homeIntent);
                        } else {
                            Log.w(TAG, "update:failure", task.getException());
                            Toast.makeText(Tests.this, "Dane nie zostały wysłane.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}