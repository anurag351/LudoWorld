package com.example.ludoworld;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class SelectColorONInternetGame extends AppCompatActivity {
    String playerName;
    Button button;
    String playerList[];
    String roomName = "";
    String role = "";
    String message;
    FirebaseDatabase database;
    DatabaseReference messageRef;
    DatabaseReference roomRef;
    Boolean startGame;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_naming);
        playerList = new String[4];
        button = (Button)findViewById(R.id.start);
        playerList[0] = "";
        playerList[1] = "";
        playerList[2] = "";
        playerList[3] = "";
        database = FirebaseDatabase.getInstance();
        preferences = getSharedPreferences("Player",0);
        playerName = preferences.getString("playerName", "");
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            roomName = extras.getString("roomName", "");
            if (roomName.equals(playerName)) {
                role = "host";
                button.setText("START");
            } else {
                role = "guest";
                button.setText("READY");
            }
        }
        roomRef = database.getReference("rooms");
        messageRef = database.getReference("rooms/"+roomName+"/message");
        messageRef.setValue("000000");
        addRoomEventListener();
        addStartGameEventListener();
    }

    private void addRoomEventListener() {
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(String.class).contains("1")){
                    ((ImageView)findViewById(R.id.img1)).setImageResource(R.drawable.redc);
                    playerList[0] = snapshot.getValue(String.class).substring(1);
                    ((EditText)findViewById(R.id.player1)).setText(playerList[0]);
                    ((EditText)findViewById(R.id.player1)).setEnabled(false);
                }
                else if(snapshot.getValue(String.class).contains("2")){
                    ((ImageView)findViewById(R.id.img2)).setImageResource(R.drawable.greenc);
                    playerList[1] = snapshot.getValue(String.class).substring(1);
                    ((EditText)findViewById(R.id.player2)).setText(playerList[1]);
                    ((EditText)findViewById(R.id.player2)).setEnabled(false);
                }
                else if(snapshot.getValue(String.class).contains("3")){
                    ((ImageView)findViewById(R.id.img3)).setImageResource(R.drawable.yellowc);
                    playerList[2] = snapshot.getValue(String.class).substring(1);
                    ((EditText)findViewById(R.id.player3)).setText(playerList[2]);
                    ((EditText)findViewById(R.id.player3)).setEnabled(false);
                }
                else if(snapshot.getValue(String.class).contains("4")){
                    ((ImageView)findViewById(R.id.img4)).setImageResource(R.drawable.bluec);
                    playerList[3] = snapshot.getValue(String.class).substring(1);
                    ((EditText)findViewById(R.id.player4)).setText(playerList[3]);
                    ((EditText)findViewById(R.id.player4)).setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //messageRef.setValue(message);
            }
        });
    }

    public void getColor(View view){
        view.setEnabled(false);
        playerName = preferences.getString("playerName","");
        if(Integer.parseInt(view.getTag().toString()) == 1){
           // messageRef(((ImageView) view).setImageResource(R.drawable.redc));
            //messageRef.setValue(message);
            message ="1" + playerName ;
        }
        else if(Integer.parseInt(view.getTag().toString()) == 2){
            //messageRef.setValue("2");
            message ="2" + playerName ;
        }
        else if(Integer.parseInt(view.getTag().toString()) == 3){
            //messageRef.setValue("3");
            message ="3" + playerName ;
        }
        else if(Integer.parseInt(view.getTag().toString()) == 4){
            //messageRef.setValue("4");
            message ="4" + playerName ;
        }
        messageRef.setValue(message);
        addRoomEventListener();
    }

    public void playLocalGameNow(View view) {
        startGame = true;
        messageRef.setValue("true");
        addStartGameEventListener();
    }

    private void addStartGameEventListener() {
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(String.class).contains("true")) {
                        Intent intent = new Intent(getApplicationContext(), PlayInternetGame.class);
                        intent.putExtra("roomName", roomName);
                        intent.putExtra("playerName[0]", playerList[0]);
                        intent.putExtra("playerName[1]", playerList[1]);
                        intent.putExtra("playerName[2]", playerList[2]);
                        intent.putExtra("playerName[3]", playerList[3]);
                        startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void goBackLocal(View view) {
        Intent intent=new Intent(this,CreateORSelectRoom.class);
        startActivity(intent);
    }
}
