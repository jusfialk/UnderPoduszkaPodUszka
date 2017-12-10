package com.example.piotr.underpoduszka;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MonitoringActivity extends AppCompatActivity {
    private final int Period = /*15 * 60 **/ 10000;
    private RestService restService = new RestService();
    private Handler handler = new Handler();
    boolean stop = true;
    Sensor gyroscopeSensor;
    Sensor accelerometerSensor;
    SensorManager manager ;
    Button button;
    SensorEventListener gyroscopeListener;
    SensorEventListener accelerometerListener;
    Float accelerometerMeasurement = (float) 0;
    Float gyroscopeMeasurement = (float) 0;
    String UserName;
    String City;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_input, null);
        builder.setView(dialogView);
        final EditText name = (EditText) dialogView.findViewById(R.id.username);
        final EditText city = (EditText) dialogView.findViewById(R.id.city);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                City = city.getText().toString();
                UserName = name.getText().toString();
            }
        }).create().show();
    }

    private void GyroscopeListener() {
        gyroscopeSensor = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        gyroscopeListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {

                if((event.values[0] + event.values[1] + event.values[2]) / 3 > accelerometerMeasurement)
                    gyroscopeMeasurement = (event.values[0] + event.values[1] + event.values[2]) / 3;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        manager.registerListener(gyroscopeListener,gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void AcelerometerListener() {
        accelerometerSensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometerListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                    if((event.values[0] + event.values[1] + event.values[2] - 9.8) / 3 > accelerometerMeasurement)
                    accelerometerMeasurement = (float) (event.values[0] + event.values[1] + event.values[2] - 9.8) / 3;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        manager.registerListener(accelerometerListener,accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);

    }

    public void StartStop(View view) {
        button = (Button) findViewById(R.id.startstop);
        if(button.getText() == "STOP"){
            stop = true;
            button.setText("START");
            manager.unregisterListener(gyroscopeListener, gyroscopeSensor);
            manager.unregisterListener(accelerometerListener, accelerometerSensor);
        }
        else {
            stop = false;
            manager = (SensorManager) getSystemService(SENSOR_SERVICE);
            button.setText("STOP");
            GyroscopeListener();
            AcelerometerListener();
            SendPackagesEvery15minutes();

        }
    }

    private void SendPackagesEvery15minutes() {
    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            if(!stop)
            ProcessData();
            handler.postDelayed(this, Period);
        }
    }, Period);
    }

    private void ProcessData() {
        manager.unregisterListener(gyroscopeListener, gyroscopeSensor);
        manager.unregisterListener(accelerometerListener, accelerometerSensor);
        DataModel model = new DataModel(String.valueOf(gyroscopeMeasurement), String.valueOf(accelerometerMeasurement), UserName, City);
        Toast.makeText(getApplicationContext(), "Przesyłam dane...", Toast.LENGTH_SHORT).show();
        new ProcessDataToServer(model).execute();
        gyroscopeMeasurement = (float)0;
        accelerometerMeasurement = (float)0;
        manager.registerListener(gyroscopeListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
        manager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }



    public void Back(View view) {
        AlertDialog.Builder wantBack = new AlertDialog.Builder(MonitoringActivity.this).setMessage("Chcesz zakończyć monitoring?");
        wantBack.setCancelable(false).setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               finish();
            }
        }).setNegativeButton("NIE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        }).show();
    }


}
