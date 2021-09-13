package com.example.primerdesafio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor acelerometro;
    private long ultimoRegistro = 0;
    private float ultimo_x, ultimo_y, ultimo_z;
    private static final int SHAKE_THRESHOLD = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor miSensor = sensorEvent.sensor;

        if(miSensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long tiempoActual = System.currentTimeMillis();

            if ((tiempoActual - ultimoRegistro) > 100){
                long difTiempo = (tiempoActual - ultimoRegistro);
                ultimoRegistro = tiempoActual;

                float velocidad = Math.abs(x + y + z - ultimo_x - ultimo_y - ultimo_z)/difTiempo * 10000;

                if (velocidad > SHAKE_THRESHOLD){
                    llamarMapa();
                }
                ultimo_x = x;
                ultimo_y = y;
                ultimo_z = z;


            }


            }
        }
    private void llamarMapa(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
