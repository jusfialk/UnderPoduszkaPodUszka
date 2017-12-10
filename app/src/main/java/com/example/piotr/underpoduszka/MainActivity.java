package com.example.piotr.underpoduszka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setImage();
    }

    private void setImage() {
        ImageView pillow = (ImageView) findViewById(R.id.pillow);
    }

    public void startMonitor(View view) {
        Intent intent = new Intent(this, MonitoringActivity.class);
        startActivity(intent);
    }

    public void analyze(View view){
        Intent intent = new Intent(this, AnalysisActivity.class);
        startActivity(intent);
    }
}
