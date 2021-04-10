package com.example.ludoworld;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class CreateORSelectRoom extends AppCompatActivity {
    ListView listView;
    Button button;
    List<String> roomsList;

    String playerName = "";
    String roomName = "";

    FirebaseDatabase database;
    DatabaseReference roomRef;
    DatabaseReference messRef;
    DatabaseReference roomsRef;
    int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectroom);
        button = ((Button)findViewById(R.id.createRoom));
        database = FirebaseDatabase.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("Player",0);
        playerName = sharedPreferences.getString("playerName", "");
        //roomName = playerName;
        listView = findViewById(R.id.list);
        roomsList = new ArrayList<>();

        messRef = database.getReference("rooms/" + roomName + "/message");
        messRef.setValue("aaa");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                roomName = roomsList.get(i);

                roomRef = database.getReference("rooms/" + roomName + "/"+playerName);
                addRoomEventListener();
                /*Intent intent = new Intent(getApplicationContext(),SelectColorONInternetGame.class);
                intent.putExtra("roomName",roomName);
                startActivity(intent);*/
                roomRef.setValue(playerName);

            }
        });
        addRoomsEventListener();
    }

    private void addRoomsEventListener() {
        roomsRef = database.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomsList.clear();
                Iterable<DataSnapshot> rooms = snapshot.getChildren();
                for(DataSnapshot snapshot1: rooms){
                    roomsList.add(snapshot1.getKey());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateORSelectRoom.this, android.R.layout.simple_list_item_1,roomsList);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createRoomFun(View view){
        button.setText("Creating Room");
        button.setEnabled(false);
        roomName = playerName;
        roomRef = database.getReference("rooms/"+roomName+"/"+playerName);
        addRoomEventListener();
        roomRef.setValue(playerName);
    }

    private void addRoomEventListener() {
        //roomRef = database.getReference("rooms/"+roomName+"/player1");
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                button.setText("CREATE ROOM");
                button.setEnabled(true);
                Intent intent = new Intent(getApplicationContext(),SelectColorONInternetGame.class);
                intent.putExtra("roomName",roomName);
                startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                button.setText("CREATE ROOM");
                button.setEnabled(true);
                Toast.makeText(CreateORSelectRoom.this,"Error!",Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void goBackInternet(View view) {
        Intent intent=new Intent(this,InternetGame.class);
        startActivity(intent);
    }
}
