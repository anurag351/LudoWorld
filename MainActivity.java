package com.example.ludoworld;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void playOnInternet(View view) {
        Intent intent=new Intent(this,InternetGame.class);
        startActivity(intent);
    }

    public void startLocalGame(View view) {
        Intent intent=new Intent(this,LocalGame.class);
        startActivity(intent);
    }
    public void startComputerGame(View view) {
        Intent intent=new Intent(this,ComputerGame.class);
        startActivity(intent);
    }
    public void playOnWiFi(View view) {
        Intent intent=new Intent(this,WifiGame.class);
        startActivity(intent);
    }

    public void quitGame(View view) {
    }
}
