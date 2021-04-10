package com.example.ludoworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class PlayerNamingComputer extends AppCompatActivity {
    public boolean[] colorChoice ={false,false,false,false};
    int no_player=4;
    int tag;
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
        if(i == 0) {
            ImageView img = (ImageView)view;
            tag = Integer.parseInt(img.getTag().toString());
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
            setPlayerName(tag);
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
    public void setPlayerName(int t){
        setColorChoice();
        EditText ed;
        if(colorChoice[0] && t != 1 ){
            ed = findViewById(R.id.player1);
            ((ImageView)findViewById(R.id.img1)).setImageResource(R.drawable.redc);
            playerName[0] = ed.getText().toString();
            if(playerName[0].matches("")){
                playerName[0]="Computer 1";
                ((EditText)findViewById(R.id.player1)).setText(playerName[0]);
                ((EditText)findViewById(R.id.player1)).setEnabled(false);
            }

        }
        if(colorChoice[1] && t != 2){
            ed = findViewById(R.id.player2);
            ((ImageView)findViewById(R.id.img2)).setImageResource(R.drawable.greenc);
            playerName[1]=ed.getText().toString();
            if(playerName[1].matches("")){
                playerName[1]="Computer 2";
                ((EditText)findViewById(R.id.player2)).setText(playerName[1]);
                ((EditText)findViewById(R.id.player2)).setEnabled(false);
            }
        }
        if(colorChoice[2] && t != 3){
            ed = findViewById(R.id.player3);
            ((ImageView)findViewById(R.id.img3)).setImageResource(R.drawable.yellowc);
            playerName[2]=ed.getText().toString();
            if(playerName[2].matches("")){
                playerName[2]="Computer 3";
                ((EditText)findViewById(R.id.player3)).setText(playerName[2]);
                ((EditText)findViewById(R.id.player3)).setEnabled(false);
            }
        }
        if(colorChoice[3] && t != 4){
            ed = findViewById(R.id.player4);
            ((ImageView)findViewById(R.id.img4)).setImageResource(R.drawable.bluec);
            playerName[3]=ed.getText().toString();
            if(playerName[3].matches("")){
                playerName[3]="Computer 4";
                ((EditText)findViewById(R.id.player4)).setText(playerName[3]);
                ((EditText)findViewById(R.id.player4)).setEnabled(false);
            }
        }
        //String State = playerName[0]+playerName[1]+playerName[2]+playerName[3];
        //Toast.makeText(getApplicationContext(),State,Toast.LENGTH_SHORT).show();
    }
    public void playLocalGameNow(View view) {
        setPlayerName(5);
        Intent intent=new Intent(this,PlayComputerGame.class);
        intent.putExtra("playerName[0]",playerName[0]);
        intent.putExtra("playerName[1]",playerName[1]);
        intent.putExtra("playerName[2]",playerName[2]);
        intent.putExtra("playerName[3]",playerName[3]);
        intent.putExtra("realPlayer",tag-1);
        //Bundle extras = new Bundle();
        //extras.putStringArray("playerName", playerName);
        startActivity(intent);
    }

    public void goBackLocal(View view) {
        Intent intent=new Intent(this,ComputerGame.class);
        startActivity(intent);
    }
}
