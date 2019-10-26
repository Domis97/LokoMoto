package com.example.dominik.alkotest.nawigacja;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.dominik.alkotest.R;
import com.example.dominik.alkotest.firebase.Wynik;
import com.example.dominik.alkotest.testy.Test1;
import com.example.dominik.alkotest.testy.Test2;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Klasa odpowiadająca za obsługę testow po wypiciu alkoholu testow porownanwczych
 */
public class TestyPorownawcze extends AppCompatActivity {

    public String wynikTest2;
    protected ArrayList<Double> wynikTest1 = new ArrayList<>();
    private String TAG = "Log." + this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_po);

        Button clickButton = findViewById(R.id.compare);
        clickButton.setOnClickListener(v -> score_result(v));
    }

    /**
     * przejscie do test1 oraz request na wynik tego testu
     *
     * @param view widok
     */

    public void test1(View view) {

        Intent myIntent = new Intent(view.getContext(), Test1.class);
        startActivityForResult(myIntent, 3);
    }

    /**
     * przejscie do test2 oraz request na wynik tego testu
     *
     * @param view widok
     */
    public void test2(View view) {

        Intent myIntent = new Intent(view.getContext(), Test2.class);
        startActivityForResult(myIntent, 4);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {

                Double[] doubleArray = ArrayUtils.toObject(data.getDoubleArrayExtra("scoreValue"));
                wynikTest1 = new ArrayList<>(Arrays.asList(doubleArray));
                Log.i(TAG, wynikTest1.toString());

            }
            if (requestCode == 4) {
                wynikTest2 = data.getStringExtra("scoreValue2");
                Log.i(TAG, wynikTest2);
            }
        }
    }

    protected void score_result(View view) {
        long temp;
        if (wynikTest1 != null && wynikTest2 != null) {
            temp = Long.valueOf(wynikTest2);
            ArrayList<Double> doubles = new ArrayList<>();
            for (Double k : wynikTest1) {
                doubles.add(k);
            }
            if (condition(doubles, temp)) {
                Intent myIntent = new Intent(view.getContext(), Wynik.class);
                myIntent.putExtra("wynikTest1", wynikTest1);
                myIntent.putExtra("wynikTest2", wynikTest2);
                startActivity(myIntent);
            }
        }
    }

    private boolean condition(ArrayList<Double> ar, Long f) {
        if (ar.size() == 3)
            if (f != 0)
                for (Double k : ar)
                    if (k > 0)
                        return true;

        return false;
    }
}
