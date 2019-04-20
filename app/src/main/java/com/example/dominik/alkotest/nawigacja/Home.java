package com.example.dominik.alkotest.nawigacja;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dominik.alkotest.R;

public class Home extends AppCompatActivity {

    private Button before;
    private Button after;

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
                Intent testy = new Intent(Home.this, Tests.class);
                startActivity(testy);
            }
        });

        after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testy = new Intent(Home.this, TestPo.class);
                startActivity(testy);
            }
        });
    }
}
