package com.example.ludoworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
public class PlayerNaming extends AppCompatActivity {
    public boolean[] colorChoice ={false,false,false,false};
    int no_player=4;
    public String[] playerName={"","","",""};
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_naming);

    }
    public void getColor(View view){
        Intent receive = getIntent();
        //Bundle extras = receive.getExtras();
        //no_player=extras.getInt("no_player");
        no_player = receive.getIntExtra("no_player",0);

        //no_player =2;
        if(i<no_player) {
            ImageView img = (ImageView)view;
            int tag = Integer.parseInt(img.getTag().toString());
            if (tag == 1){
                if(!colorChoice[0]) {
                    img.setImageResource(R.drawable.redc);
                    colorChoice[0] = true;
                    i += 1;
                }else{
                    img.setImageResource(0);
                    colorChoice[0] = false;
                    i -= 1;
                }
            } else if (tag == 2 ) {
                if(!colorChoice[1]) {
                    img.setImageResource(R.drawable.greenc);
                    colorChoice[1] = true;
                    i += 1;
                }else{
                    img.setImageResource(0);
                    colorChoice[1] = false;
                    i -= 1;
                }
            } else if (tag == 3 ) {
                if(!colorChoice[2]) {
                    img.setImageResource(R.drawable.yellowc);
                    colorChoice[2] = true;
                    i += 1;
                }else{
                    img.setImageResource(0);
                    colorChoice[2] = false;
                    i -= 1;
                }
            } else if (tag == 4) {
                if(!colorChoice[3]) {
                    img.setImageResource(R.drawable.bluec);
                    colorChoice[3] = true;
                    i += 1;
                }else{
                    img.setImageResource(0);
                    colorChoice[3] = false;
                    i -= 1;
                }
            }
        }
    }
    public void setColorChoice(){
        if(no_player == 2){
            if(colorChoice[0]){
                colorChoice[2] = true;
            }
            else if(colorChoice[1]){
                colorChoice[3] = true;
            }
            else if(colorChoice[2]){
                colorChoice[0] = true;
            }
            else {
                colorChoice[1] = true;
            }
        }
        else {
            for (int j = 0; i < no_player; j++) {
                if (!colorChoice[j]) {
                    colorChoice[j] = true;
                    i += 1;
                }
            }
        }
    }
    public void setPlayerName(){
        setColorChoice();
        EditText ed;
        if(colorChoice[0]){
            ed = findViewById(R.id.player1);
            playerName[0] = ed.getText().toString();
            if(playerName[0].matches("")){
                playerName[0]="Player 1";
            }

        }
        if(colorChoice[1]){
            ed = findViewById(R.id.player2);
            playerName[1]=ed.getText().toString();
            if(playerName[1].matches("")){
                playerName[1]="Player 2";
            }
        }
        if(colorChoice[2]){
            ed = findViewById(R.id.player3);
            playerName[2]=ed.getText().toString();
            if(playerName[2].matches("")){
                playerName[2]="Player 3";
            }
        }
        if(colorChoice[3]){
            ed = findViewById(R.id.player4);
            playerName[3]=ed.getText().toString();
            if(playerName[3].matches("")){
                playerName[3]="Player 4";
            }
        }
        //String State = playerName[0]+playerName[1]+playerName[2]+playerName[3];
        //Toast.makeText(getApplicationContext(),State,Toast.LENGTH_SHORT).show();
    }
    public void playLocalGameNow(View view) {
        setPlayerName();
        Intent intent=new Intent(this,PlayLocalGame.class);
        intent.putExtra("playerName[0]",playerName[0]);
        intent.putExtra("playerName[1]",playerName[1]);
        intent.putExtra("playerName[2]",playerName[2]);
        intent.putExtra("playerName[3]",playerName[3]);
        //Bundle extras = new Bundle();
        //extras.putStringArray("playerName", playerName);
        startActivity(intent);
    }

    public void goBackLocal(View view) {
        Intent intent=new Intent(this,LocalGame.class);
        startActivity(intent);
    }
}

