package com.example.dominik.alkotest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dominik.alkotest.nawigacja.Home;
import com.example.dominik.alkotest.nawigacja.Main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Objects;

/**
 * Klasa odpowiadająca za obsługę usuwanie konta
 */

public class Usun extends AppCompatActivity {

    TextView usunText;
    EditText usunInput;
    String random;
    private String TAG = "Log." + this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usun);
        usunInput = findViewById(R.id.usunInput);
        usunText = findViewById(R.id.usunText);
        usunText.setText(random());
        Button button = findViewById(R.id.usuwanie);
        button.setOnClickListener(
                v -> {
                    if (usunInput.getText().toString().equals(random)) {
                        deleteData();
                        Toast.makeText(Usun.this, "Usunieto uzytkownika oraz dane.",
                                Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(v.getContext(), Main.class);
                        startActivity(myIntent);

                    } else {
                        Toast.makeText(Usun.this, "Błędnie podana wartosc.",
                                Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(v.getContext(), Home.class);
                        startActivity(myIntent);
                    }
                }
        );

    }

    private String random() {
        random = RandomStringUtils.random(6, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        return random;
    }

    private void deleteUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Log.i(TAG, "CurrentUser" + user);
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "Usunieto uzytkownika.");

                        }
                    });
        }
    }

    private void deleteData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("scores").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Dane usuniete!");
                    deleteUser();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Blad podczas usuwania dokumentow", e);
                    Toast.makeText(Usun.this, "Blad podczas usuwania.",
                            Toast.LENGTH_SHORT).show();
                });
    }
}
