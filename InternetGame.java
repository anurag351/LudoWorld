package com.example.ludoworld;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

import static android.widget.Toast.LENGTH_SHORT;

public class InternetGame extends AppCompatActivity {
    EditText playerID;
    String playerName;
    Button button;
    FirebaseDatabase database;
    DatabaseReference playerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        button = ((Button)findViewById(R.id.login));
        playerID = findViewById(R.id.playerID);
        database = FirebaseDatabase.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("Player",0);
        playerName = sharedPreferences.getString("playerName", "");
        /*if(!playerName.equals("")){
            playerRef = database.getReference("player/"+playerName);
            playerRef.setValue("");
            addEventListener();
        }*/
    }
    public void goBackInternet(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void loginNow(View view) {

        playerName = playerID.getText().toString();
        playerID.setText("");
        if(!playerName.equals("")){
            ((Button)findViewById(R.id.login)).setText("Logging in");
            ((Button)findViewById(R.id.login)).setEnabled(false);
            playerRef = database.getReference("player/"+playerName);
            addEventListener();
            playerRef.setValue("");
        }
    }
    private void addEventListener(){
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!playerName.equals("")){
                    SharedPreferences sharedPreferences  = getSharedPreferences("Player",0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("playerName",playerName);
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(),CreateORSelectRoom.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                button.setText("LOG IN");
                button.setEnabled(true);
                Toast.makeText(InternetGame.this,"Error!",Toast.LENGTH_LONG).show();

            }
        });
    }
}
