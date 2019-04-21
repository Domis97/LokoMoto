package com.example.dominik.alkotest.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dominik.alkotest.nawigacja.Home;
import com.example.dominik.alkotest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Klasa odpowiadająca za logowanie użytkowników aplikacji
 */


public class Logowanie extends AppCompatActivity {

    private String TAG = "Logowanie";
    private FirebaseAuth mAuth;
    private Button log;
    private String emailPatternString = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    private String passowrdPatternString = "\\w{6,}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logowanie);
        log = findViewById(R.id.zaloguj);
        mAuth = FirebaseAuth.getInstance();
        setUpButtonListeners();
    }


    public void setUpButtonListeners() {
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logowanieFirebase();

            }
        });
    }


    /**
     * @param pattern
     * @param match
     * @return
     * metoda sprawdza czy poprawnie podano dane do logowania oraz zwraca true lub false zależnie od wyniku
     */
    private boolean validation(String pattern, String match) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(match);
        return m.matches();
    }

    /**
     * metoda logująca użytkownika do firebase
     */
    private void logowanieFirebase() {
        EditText editText = findViewById(R.id.emailLog);
        String login = editText.getText().toString();
        editText = findViewById(R.id.hasloLog);
        String haslo = editText.getText().toString();
        if ((validation(emailPatternString, login)) && (validation(passowrdPatternString, haslo))) {
            mAuth.signInWithEmailAndPassword(login, haslo)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent home = new Intent(Logowanie.this, Home.class);
                                startActivity(home);
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(Logowanie.this, "Błędne parametry logowania.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Błędne parametry logowania!",
                    Toast.LENGTH_LONG).show();
        }
    }
}
