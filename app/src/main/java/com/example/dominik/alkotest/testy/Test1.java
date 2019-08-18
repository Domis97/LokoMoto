package com.example.dominik.alkotest.testy;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.dominik.alkotest.R;


/**
 * Klasa odpowiadająca za obsługę test1
 */

//TODO logika dla pobierania wartosci, zapis wynikow

public class Test1 extends AppCompatActivity implements SensorEventListener {

    private static String TAG = "Test1.class";
    TextView textView;
    TextView textViewX;
    TextView textViewY;
    TextView textViewZ;
    Button buttonStart;
    private float xValueAvg, yValueAvg, zValueAvg;
    private Sensor mySensor;
    private SensorManager SM;
    private long i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        buttonStart = findViewById(R.id.startTest1);
        textView = findViewById(R.id.Counter);
        textViewX = findViewById(R.id.xText);
        textViewY = findViewById(R.id.yText);
        textViewZ = findViewById(R.id.zText);

        buttonStart.setOnClickListener(v -> testStart());


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void testStart() {

        count();

        // Create our Sensor Manager
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Register sensor Listener
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void end() {
        SM.unregisterListener(this);
        show(xValueAvg, yValueAvg, zValueAvg);
        i = 0;
    }


    /**
     * pobieranie oraz sumowanie wartosci z sensorow
     * oraz iteracja sluzaca do tego by wiedziec ile razy zmienila sie wartosc na sensorze
     *
     * @param event zmiana
     */

    @Override
    public void onSensorChanged(SensorEvent event) {

        float xText, yText, zText, xValue = 0, yValue = 0, zValue = 0;

        i++;
        Log.v(TAG, "Ilosc wartosci to: " + i);

        xText = event.values[0];
        xValue = xValue + xText;
        xValueAvg = xValue / i;
        yText = event.values[1];
        yValue = yValue + yText;
        yValueAvg = yValue / i;
        zText = event.values[2];
        zValue = zValue + zText;
        zValueAvg = zValue / i;
    }

    private void show(float x, float y, float z) {
        textViewX.setText(String.valueOf(x));
        textViewY.setText(String.valueOf(y));
        textViewZ.setText(String.valueOf(z));
    }

    private void count() {

        final Handler handler = new Handler();
        int[] count = {10};

        final Runnable runnable = new Runnable() {
            public void run() {
                textView.setText(String.valueOf(count[0]));
                Log.d(TAG, "Run test count: " + count[0]);
                if (count[0]-- > 0) {
                    handler.postDelayed(this, 1000);
                }
                if (count[0] == 0) {
                    end();
                }
            }
        };

        handler.post(runnable);
    }

}