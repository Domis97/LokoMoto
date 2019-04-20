package com.example.dominik.alkotest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.dominik.alkotest.nawigacja.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa odpowiadająca za obsługę usuwanie konta
 */

public class Usun extends AppCompatActivity {

    private String wybor;

    /**
     * usuwanie wybranego konta
     *
     * @param savedInstanceState instncja apk
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usun);
        Button button = findViewById(R.id.usuwanie);
        spinner();
        button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        getApplicationContext().deleteFile(wybor);
                        spinner();
                        Intent myIntent = new Intent(v.getContext(), Main.class);
                        startActivity(myIntent);
                    }
                }
        );

    }

    /**
     * wypisywanie kont mozliwych do usuniecia
     */

    protected void spinner() {

        List<String> spinnerArray = new ArrayList<>();
        for (String s : fileList()) {
            spinnerArray.add(s);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = findViewById(R.id.profile);
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
}
