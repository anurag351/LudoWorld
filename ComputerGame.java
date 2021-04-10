package com.example.ludoworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class ComputerGame extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_xml);
    }
    public void player4Game(View view) {
        int i = 4;
        Intent intent=new Intent(this,PlayerNamingComputer.class);
        intent.putExtra("no_player",i);
        startActivity(intent);
    }

    public void player3Game(View view) {
        int i = 3;
        Intent intent=new Intent(this,PlayerNamingComputer.class);
        //Bundle extras = new Bundle();
        //extras.putInt("no_player",i);
        intent.putExtra("no_player",i);
        startActivity(intent);
    }

    public void player2Game(View view) {
        int i = 2;
        Intent intent=new Intent(this,PlayerNamingComputer.class);
        //Bundle extras = new Bundle();
        intent.putExtra("no_player",i);
        startActivity(intent);
    }

    public void goBackMain(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
