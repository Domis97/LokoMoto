package com.example.dominik.alkotest.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dominik.alkotest.R;
import com.example.dominik.alkotest.nawigacja.Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa odpowiadająca za obsługę rejestracji
 */

public class Rejestracja extends AppCompatActivity {

    private String TAG = "Log." + this.getClass().getName();
    private FirebaseAuth mAuth;
    private Button rej;
    private String emailPatternString = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    private String passowrdPatternString = "\\w{6,}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);
        rej = findViewById(R.id.zarejestruj);
        mAuth = FirebaseAuth.getInstance();
        setUpButtonListeners();
    }

    public void setUpButtonListeners() {
        rej.setOnClickListener(v -> rejestracjaFirebase());
    }


    /**
     * @param pattern
     * @param match
     * @return metoda sprawdza czy poprawnie podano dane do rejestracji oraz zwraca true lub false zależnie od wyniku
     */
    private boolean validation(String pattern, String match) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(match);
        return m.matches();
    }


    /**
     * metoda rjestrująca użytkownika w firebase
     */

    private void rejestracjaFirebase() {
        EditText editText = findViewById(R.id.email);
        String login = editText.getText().toString();
        editText = findViewById(R.id.haslo);
        String haslo = editText.getText().toString();
        if ((validation(emailPatternString, login)) && (validation(passowrdPatternString, haslo))) {
            mAuth.createUserWithEmailAndPassword(login, haslo).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(Rejestracja.this, "Konto zostało utworzone.",
                                        Toast.LENGTH_SHORT).show();
                                Intent homeIntent = new Intent(Rejestracja.this, Home.class);
                                startActivity(homeIntent);
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Rejestracja.this, "Konto nie zostało utworzone.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
            );
        } else {
            Toast.makeText(this, "Błędne parametry rejestracji!",
                    Toast.LENGTH_LONG).show();
        }
    }
}
