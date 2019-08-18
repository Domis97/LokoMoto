package com.example.dominik.alkotest.nawigacja;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.dominik.alkotest.R;
import com.example.dominik.alkotest.firebase.Wynik;
import com.example.dominik.alkotest.testy.Test1;
import com.example.dominik.alkotest.testy.Test2;

/**
 * Klasa odpowiadająca za obsługę testow po wypiciu alkoholu testow porownanwczych
 */
public class TestyPorownawcze extends AppCompatActivity {

    protected String wartosc = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_po);
    }

    /**
     * przejscie do test1
     *
     * @param view widok
     */

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
        int requestCode = 2;
        startActivityForResult(myIntent, requestCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        wartosc = data.getStringExtra("someValue");
    }

    protected void logowanie(View view) {
        long temp = Long.valueOf(wartosc);//zeby nie wchodzil do buttona
        if (temp != 0) {
            Intent myIntent = new Intent(view.getContext(), Wynik.class);
            myIntent.putExtra("wartosc", wartosc);
            startActivity(myIntent);
        }
    }
}
