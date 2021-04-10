package com.example.ludoworld;

        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.speech.tts.TextToSpeech;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;
        import androidx.appcompat.app.AppCompatActivity;

        import java.util.*;

public class PlayComputerGame extends AppCompatActivity {
    /*
        playerId
        0 is Red
        1 is Green
        2 is yellow
        3 is Blue
     */
    int realPlayer;
    int nim;                            //nim is the next value of the coin after move
    int playerId=4;
    boolean cutCoin = false;            //it is true when any coin cut another coin so that player will get an extra roll
    boolean activeOpen = false;         //if true then player can one his coin from box
    boolean activeMove = false;         //if true then player can move their coin
    boolean activeRoll = false;         //if true then player can roll dice on their turn
    List<Integer> list = Arrays.asList(0,8,13,21,26,34,39,47);
    public Set<Integer> defstop = new HashSet<>(list);      //it define position of stops
    //int tapImg = Integer.parseInt(img.getTag().toString());
    public int rd ;                     //store current value of roll dice
    boolean activeGame = false;         //if true then game start
    byte[] home = new byte[4];             //store value of no of coin passed
    boolean[] colorStatus={false,false,false,false};      //it store color status or player status true if any coin open by that player
    boolean[][] coinStatus = {{false,false,false,false},{false,false,false,false}, {false,false,false,false}, {false,false,false,false}};
    //it store identity of coin open in field
    int[][] coinPosition={{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1}};
    int[][] coinDistance={{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1}};
    int[][] coinDanger={{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1}};
    //it store tag number of coin in field
    //static int[] restPosition = {0,8,13,21,26,34,39,47 };
    byte winPosition = 0;
    byte losePosition = 3;
    boolean[] winColor= new boolean[4];
    private TextToSpeech myText;
    private final int MY_DATA=0;
    View rollView;
    //playerName = receive.getStringArrayExtra("playerName");
    String[] playerName = new String[]{"a","b","c","d"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_game);
        Intent receive = getIntent();
        playerName[0]=receive.getStringExtra("playerName[0]");
        playerName[1]=receive.getStringExtra("playerName[1]");
        playerName[2]=receive.getStringExtra("playerName[2]");
        playerName[3]=receive.getStringExtra("playerName[3]");
        realPlayer = receive.getIntExtra("realPlayer",realPlayer);
        myText = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    myText.setLanguage(Locale.UK);
                }
            }
        });

        initialState();
    }
    /*
        ****Initial state****
        #All four colour coin in box;
        #Chance of Red to play the game first;
        #Player is able to roll dice now
        *
        * It is call by tapping any key on screen
        * rollDice or openCoin or moveStep

     */
    public void setPlayerName(){
        TextView t1 = findViewById(R.id.t1);
        TextView t2 = findViewById(R.id.t2);
        TextView t3 = findViewById(R.id.t3);
        TextView t4 = findViewById(R.id.t4);

        t1.setText(playerName[0]);
        t2.setText(playerName[1]);
        t3.setText(playerName[2]);
        t4.setText(playerName[3]);

    }
    void initialState(){



        //((ImageView)findViewById(R.id.dim0)).setImageResource(R.drawable.dice6);
        for(int i=0;i<4;i++) {
            //String State = playerName[i];//+playerName[1]+playerName[2]+playerName[3];
            //Toast.makeText(getApplicationContext(),State,Toast.LENGTH_SHORT).show();
            if(playerName[i].matches("")){
                home[i] = 4;
                winColor[i] = true;

            }else{
                home[i] = 0;
                winColor[i] = false;
                losePosition-=1;
                if(i==0){
                    playerId = 0;
                    if(playerId == 0){
                        ((ImageView)findViewById(R.id.dim0)).setImageResource(R.drawable.dice6);

                    }
                    ((ImageView)findViewById(R.id.fim1)).setImageResource(R.drawable.redc);
                    ((ImageView)findViewById(R.id.fim2)).setImageResource(R.drawable.redc);
                    ((ImageView)findViewById(R.id.fim3)).setImageResource(R.drawable.redc);
                    ((ImageView)findViewById(R.id.fim4)).setImageResource(R.drawable.redc);
                }
                else if(i==1){
                    if(playerId != 0){
                        playerId = 1;
                        ((ImageView)findViewById(R.id.dim1)).setImageResource(R.drawable.dice6);
                    }
                    ((ImageView)findViewById(R.id.fim5)).setImageResource(R.drawable.greenc);
                    ((ImageView)findViewById(R.id.fim6)).setImageResource(R.drawable.greenc);
                    ((ImageView)findViewById(R.id.fim7)).setImageResource(R.drawable.greenc);
                    ((ImageView)findViewById(R.id.fim8)).setImageResource(R.drawable.greenc);
                }
                else if(i==2){
                    if(playerId != 0 && playerId!=1){
                        ((ImageView)findViewById(R.id.dim2)).setImageResource(R.drawable.dice6);
                        playerId = 2;

                    }
                    ((ImageView)findViewById(R.id.fim13)).setImageResource(R.drawable.yellowc);
                    ((ImageView)findViewById(R.id.fim14)).setImageResource(R.drawable.yellowc);
                    ((ImageView)findViewById(R.id.fim15)).setImageResource(R.drawable.yellowc);
                    ((ImageView)findViewById(R.id.fim16)).setImageResource(R.drawable.yellowc);
                }
                else if(i==3){
                    ((ImageView)findViewById(R.id.fim9)).setImageResource(R.drawable.bluec);
                    ((ImageView)findViewById(R.id.fim10)).setImageResource(R.drawable.bluec);
                    ((ImageView)findViewById(R.id.fim11)).setImageResource(R.drawable.bluec);
                    ((ImageView)findViewById(R.id.fim12)).setImageResource(R.drawable.bluec);
                }
            }
        }
        for(int i = 0; i < 52; i++){
            displayCoins(((ImageView)getNewPositionId(i)),new byte[]{0, 0, 0, 0});
        }
        for(int i = 0; i < 6;i++){
            displayCoins(((ImageView)getNewPositionId(i+100)),new byte[]{0, 0, 0, 0});
            displayCoins(((ImageView)getNewPositionId(i+200)),new byte[]{0, 0, 0, 0});
            displayCoins(((ImageView)getNewPositionId(i+300)),new byte[]{0, 0, 0, 0});
            displayCoins(((ImageView)getNewPositionId(i+400)),new byte[]{0, 0, 0, 0});
        }
        ((ImageView)findViewById(R.id.pass1)).setImageResource(0);
        ((ImageView)findViewById(R.id.pass2)).setImageResource(0);
        ((ImageView)findViewById(R.id.pass3)).setImageResource(0);
        ((ImageView)findViewById(R.id.pass4)).setImageResource(0);
        String State = playerName[playerId]+"'s Turn";
        activeGame =  true;
        activeMove = false;
        activeOpen =  false;
        activeRoll = true;
        winPosition = 0;
        cutCoin = false;
        setPlayerName();
        coinPosition = new int[][]{{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1}};
        coinDistance = new int[][]{{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1}};
        coinDanger = new int[][]{{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1}};
        colorStatus = new boolean[]{false,false,false,false};
        coinStatus = new boolean[][]{{false,false,false,false},{false,false,false,false}, {false,false,false,false}, {false,false,false,false}};
        Toast.makeText(getApplicationContext(),State,Toast.LENGTH_SHORT).show();
        myText.setPitch(1f);
        myText.setSpeechRate(1f);
        myText.speak(State,TextToSpeech.QUEUE_FLUSH,null);
        //t1.setText("Red's Turn");
        if(realPlayer != 0 && !winColor[0]) {
            rollDice(findViewById(R.id.dim0));
        }
        else if(realPlayer != 1 && !winColor[1]){
            rollDice(findViewById(R.id.dim1));
        }
        else if(realPlayer != 2 && !winColor[2]){
            rollDice(findViewById(R.id.dim2));
        }
    }

    /*
     *******nextMove() function *********
     * next move is executed only if activeOpen activeMove and activeRoll is false
     * because not to skip chance of active player
     * it changes player id and position of dice to next player for roll

     */
    public void setCurrentStatus(){
        TextView t1 = findViewById(R.id.t1);
        TextView t2 = findViewById(R.id.t2);
        TextView t3 = findViewById(R.id.t3);
        TextView t4 = findViewById(R.id.t4);
        for(int i = 0; i < 4; i++){
            if(!winColor[i] && i!=playerId){
                switch (i){
                    case 0: t1.setText(playerName[0]);
                        break;
                    case 1: t2.setText(playerName[1]);
                        break;
                    case 2: t3.setText(playerName[2]);
                        break;
                    case 3: t4.setText(playerName[3]);
                        break;

                }
            }
        }
    }
    /*private void speakWords(String speech){
        myText.speak(speech,TextToSpeech.QUEUE_FLUSH,null);
    }*/
    public void nextMove(){
        TextView t1 = findViewById(R.id.t1);
        TextView t2 = findViewById(R.id.t2);
        TextView t3 = findViewById(R.id.t3);
        TextView t4 = findViewById(R.id.t4);

        String State;

        if(!activeOpen && !activeMove && !activeRoll){
            playerId = (playerId + 1) % 4;
            if(home[playerId] == 4) {
                nextMove();
            }// % prevent playerID from going beyond available players
            activeRoll = true;
            if(playerId == 1){
                State = playerName[1]+"'s Turns";
                t2.setText(State);
                setCurrentStatus();
                ((ImageView)findViewById(R.id.dim0)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim2)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim3)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim1)).setImageResource(R.drawable.dice6);
                if(realPlayer != 1){
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            View view = (View)findViewById(R.id.dim1);
                            rollDice(view);
                        }
                    },1000);
                }
            }
            else if (playerId == 2){
                State = playerName[2]+"'s Turns";
                t3.setText(State);
                setCurrentStatus();
                ((ImageView)findViewById(R.id.dim1)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim0)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim3)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim2)).setImageResource(R.drawable.dice6);
                if(realPlayer != 2){
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            View view = (View)findViewById(R.id.dim2);
                            rollDice(view);
                        }
                    },1000);
                }
            }
            else if(playerId ==3){
                State = playerName[3]+"'s Turns";
                t4.setText(State);
                setCurrentStatus();
                ((ImageView)findViewById(R.id.dim2)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim1)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim0)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim3)).setImageResource(R.drawable.dice6);
                if(realPlayer != 3){
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            View view = (View)findViewById(R.id.dim3);
                            rollDice(view);
                        }
                    },1000);

                }
            }
            else {
                State = playerName[0]+"'s Turns";
                t1.setText(State);
                setCurrentStatus();
                ((ImageView)findViewById(R.id.dim3)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim1)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim2)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim0)).setImageResource(R.drawable.dice6);
                if(realPlayer != 0){
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            View view = (View)findViewById(R.id.dim0);
                            rollDice(view);
                        }
                    },1000);
                }
            }
            Toast.makeText(getApplicationContext(),State,Toast.LENGTH_SHORT).show();
            myText.setPitch(1f);
            myText.setSpeechRate(1f);
            myText.speak(State,TextToSpeech.QUEUE_FLUSH,null);
        }
    }
    /*
     *********rollDice() function**********
     * it allow only active player to roll dice
     * it utilise random function
     * it executed when activeGame is true and activeRoll is true
     * if player already open their coin then they will able to get a move option by true value of activeMove
     * if dice roll for 6 then player can also get option to open their coin
     * at last nextMove is call because if player did't get 6 and not any open coin then next player get chance
     * if activeMove of activeOpen is true then nextMove did nothing
     */
    public void makeDecision(){
        if(activeOpen && activeMove){

        }
        else {
            
        }
    }
    public void rollDice(View view) {
        if(!activeGame){
            initialState();
        } else if(activeRoll){
            rollView = view;
            ImageView img = (ImageView) view;
            if(Integer.parseInt(img.getTag().toString())== ((playerId+1)*111)) {
                img.setRotationY(-1000f);
                rd = (int) (1 + (6 * Math.random()));
                switch (rd) {
                    case 1:
                        img.setImageResource(R.drawable.dice1);
                        break;
                    case 2:
                        img.setImageResource(R.drawable.dice2);
                        break;
                    case 3:
                        img.setImageResource(R.drawable.dice3);
                        break;
                    case 4:
                        img.setImageResource(R.drawable.dice4);
                        break;
                    case 5:
                        img.setImageResource(R.drawable.dice5);
                        break;
                    case 6:
                        img.setImageResource(R.drawable.dice6);
                        break;
                }
                img.animate().rotationYBy(1000f).setDuration(500);
                //Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myanimation);
                //img.startAnimation(animation);
                activeRoll = false;
                cutCoin = false;
                if(colorStatus[playerId]) {
                    int min = 1000;
                    for (int i=0; i<4 ; i++) {
                        if(coinStatus[playerId][i] == false && rd == 6){
                            activeOpen = true;
                            if(playerId != realPlayer) {
                                openCoin(getNewPositionId(((playerId + 6) * 10) + i));
                                rd = 0;
                                break;
                            }
                        }
                        if (coinPosition[playerId][i] < min && coinPosition[playerId][i] >= 0) {
                            min = coinPosition[playerId][i];
                        }
                    }
                    if(rd!=0) {
                        if (min + rd > 60 && (min + rd) % 100 > 5) {
                            activeMove = false;
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    nextMove();
                                }
                            }, 1000);
                        } else {
                            activeMove = true;
                        }
                        if (playerId != realPlayer) {
                            makeDecision();
                        }
                    }
                }
                else if (rd == 6) {
                    activeOpen = true;
                    if(playerId != realPlayer){
                        if(playerId == 0){
                            openCoin(findViewById(R.id.fim1));
                        }
                        else if(playerId == 1){
                            openCoin(findViewById(R.id.fim5));
                        }
                        else if(playerId == 2){
                            openCoin(findViewById(R.id.fim13));
                        }
                        else{
                            openCoin(findViewById(R.id.fim9));
                        }
                    }
                }
                   /*if (colorStatus[playerId]) {
                       activeMove = true;
                   */
                else {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            nextMove();
                        }
                    },1200);
                   // nextMove();
                }
            }
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(activeOpen && playerId!=realPlayer){
                        for (int i=0; i<4 ; i++) {
                            if(!coinStatus[playerId][i] && rd == 6){
                                    openCoin(getNewPositionId(((playerId + 6) * 10) + i));
                            }
                        }
                    }
                    else if(activeMove && playerId!=realPlayer){
                            int max = Integer.MIN_VALUE;
                            int max1 = Integer.MIN_VALUE;
                            int temp = 0;
                            int temp1 = 0;
                            for(int k = 0; k < 4; k++){

                                if(!defstop.contains(coinPosition[playerId][k]) && max < coinDistance[playerId][k] && max+rd<=56 && coinStatus[playerId][k]){
                                    max = coinDistance[playerId][k];
                                    temp = k;
                                }
                                else if(max1<coinDistance[playerId][k]){
                                    max1 = coinDistance[playerId][k];
                                    temp1 = k;
                                }
                                if(!defstop.contains(coinPosition[playerId][k]) && coinStatus[playerId][k]) {
                                    cutting(getNim(coinPosition[playerId][k]));
                                    if(cutCoin) {
                                        moveStep(getNewPositionId(coinPosition[playerId][k]));
                                    }
                                }
                            }
                            if(!cutCoin) {
                                if (max < 0) {
                                    moveStep(getNewPositionId(coinPosition[playerId][temp1]));
                                } else {
                                    moveStep(getNewPositionId(coinPosition[playerId][temp]));
                                }
                            }
                    }
                }
            },1000);
        }

    }
    /*

     */
    public  void openCoin(View view){
        if(!activeGame){
            initialState();
        }
        else if(activeOpen){
            ImageView img = (ImageView) view;
            int im = Integer.parseInt(img.getTag().toString());
            int key = 10 * (playerId+6);
            if(im >= key && im <key + 4) {
                int pos;
                pos = (im % 10);
                img.setImageResource(0);
                if (coinStatus[playerId][pos] == false) {
                    coinStatus[playerId][pos] = true;
                    // String str1 = valueOf(pos);
                    //Toast.makeText(this,str1,Toast.LENGTH_LONG).show();
                    colorStatus[playerId] = true;
                    activeOpen = false;
                    activeMove = false;
                    activeRoll = true;
                    if (playerId == 0) {
                        //((ImageView) findViewById(R.id.a2)).setImageResource(R.drawable.redc);
                        //coinPosition[playerId][pos] = Integer.parseInt((findViewById(R.id.a2)).getTag().toString());
                        coinPosition[playerId][pos] = 0;
                        coinDistance[playerId][pos] = 0;
                        coinDanger[playerId][pos] = 0;
                        reposition(coinPosition[playerId][pos]);
                        //String str1 = valueOf(coinPosition[playerId][pos]);
                        //Toast.makeText(this,str1,Toast.LENGTH_LONG).show();
                    } else if (playerId == 1) {
                        //((ImageView) findViewById(R.id.b6)).setImageResource(R.drawable.greenc);
                        //coinPosition[playerId][pos] = Integer.parseInt(findViewById(R.id.b6).getTag().toString());
                        coinPosition[playerId][pos] = 13;
                        coinDistance[playerId][pos] = 0;
                        coinDanger[playerId][pos] = 0;
                        reposition(coinPosition[playerId][pos]);
                        //String str1 = valueOf(coinPosition[playerId][pos]);
                        //Toast.makeText(this,str1,Toast.LENGTH_LONG).show();
                    } else if (playerId == 2) {
                        //((ImageView) findViewById(R.id.c17)).setImageResource(R.drawable.yellowc);
                        //coinPosition[playerId][pos] = Integer.parseInt((findViewById(R.id.c17)).getTag().toString());
                        coinPosition[playerId][pos] = 26;
                        coinDistance[playerId][pos] = 0;
                        coinDanger[playerId][pos] = 0;
                        reposition(coinPosition[playerId][pos]);
                        //String str1 = valueOf(coinPosition[playerId][pos]);
                        // Toast.makeText(this,str1,Toast.LENGTH_LONG).show();
                    } else {
                        //((ImageView) findViewById(R.id.d13)).setImageResource(R.drawable.bluec);
                        //coinPosition[playerId][pos] = Integer.parseInt((findViewById(R.id.d13)).getTag().toString());
                        coinPosition[playerId][pos] = 39;
                        coinDistance[playerId][pos] = 0;
                        coinDanger[playerId][pos] = 0;
                        reposition(coinPosition[playerId][pos]);
                        //String str1 = valueOf(coinPosition[playerId][pos]);
                        //Toast.makeText(this,str1,Toast.LENGTH_LONG).show();
                    }
                    //nextMove();
                    if(playerId != realPlayer){
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rollDice(rollView);
                            }
                        },1200);
                    }
                }
            }
        }
    }
    public void setColorStatus(int id){
        colorStatus[id] = false;
        for(int k=0; k<4; k++){
            if(coinStatus[id][k] == true){
                if(coinPosition[id][k] !=105 && coinPosition[id][k] != 205 && coinPosition[id][k] != 305 && coinPosition[id][k] != 405) {
                    colorStatus[id] = true;
                    break;
                }
            }
        }
    }
    /* public void pairingfun(View view){
         int count=0;
         for(int i:coinPosition[playerId]){
             if(i == nim){
                 count+=1;
             }
         }
         if(count == 2){

         }
     }*/
    public  void cutting(int nim){
        for(int i = 0; i<4;i++){
            for(int j = 0;j < 4; j++){
                if(coinPosition[i][j] == nim){
                    if(i != playerId){
                        cutCoin = true;
                        coinPosition[i][j] = -1;
                        coinDistance[i][j] = -1;
                        coinDanger[i][j] = -1;
                        coinStatus[i][j] = false;
                        colorStatus[i] = false;
                        int cl = ((i+6)*10)+j;
                        switch(cl){
                            case 60: ((ImageView)findViewById(R.id.fim4)).setImageResource(R.drawable.redc);
                                break;
                            case 61: ((ImageView)findViewById(R.id.fim1)).setImageResource(R.drawable.redc);
                                break;
                            case 62: ((ImageView)findViewById(R.id.fim2)).setImageResource(R.drawable.redc);
                                break;
                            case 63: ((ImageView)findViewById(R.id.fim3)).setImageResource(R.drawable.redc);
                                break;
                            case 70: ((ImageView)findViewById(R.id.fim8)).setImageResource(R.drawable.greenc);
                                break;
                            case 71: ((ImageView)findViewById(R.id.fim5)).setImageResource(R.drawable.greenc);
                                break;
                            case 72: ((ImageView)findViewById(R.id.fim6)).setImageResource(R.drawable.greenc);
                                break;
                            case 73: ((ImageView)findViewById(R.id.fim7)).setImageResource(R.drawable.greenc);
                                break;
                            case 80: ((ImageView)findViewById(R.id.fim16)).setImageResource(R.drawable.yellowc);
                                break;
                            case 81: ((ImageView)findViewById(R.id.fim13)).setImageResource(R.drawable.yellowc);
                                break;
                            case 82: ((ImageView)findViewById(R.id.fim14)).setImageResource(R.drawable.yellowc);
                                break;
                            case 83: ((ImageView)findViewById(R.id.fim15)).setImageResource(R.drawable.yellowc);
                                break;
                            case 90: ((ImageView)findViewById(R.id.fim12)).setImageResource(R.drawable.bluec);
                                break;
                            case 91: ((ImageView)findViewById(R.id.fim9)).setImageResource(R.drawable.bluec);
                                break;
                            case 92: ((ImageView)findViewById(R.id.fim10)).setImageResource(R.drawable.bluec);
                                break;
                            case 93: ((ImageView)findViewById(R.id.fim11)).setImageResource(R.drawable.bluec);
                                break;
                        }
                        setColorStatus(i);
                          /*for(int k=0; k<4; k++){
                              if(coinStatus[i][k] == true){
                                  if(coinPosition[i][k] !=105 && coinPosition[i][k] != 205 && coinPosition[i][j] != 305 && coinPosition[i][j] != 405) {
                                      colorStatus[i] = true;
                                      break;
                                  }
                              }
                          }*/
                    }
                    else{
                        reposition(nim);
                    }
                }
            }
        }

    }
    public void displayCoins(ImageView img, byte[] count){
        boolean b = (count[0] != 0) && (count[1] != 0) && (count[2] != 0) && (count[3] != 0);
        if(b){
            img.setImageResource(R.drawable.r1g1y1b1);
        }
        else if(count[0]!=0 && count[1]!=0 && count[2]!=0){
            img.setImageResource(R.drawable.r1g1y1);
        }
        else if(count[0]!=0 && count[1]!=0 && count[3]!=0){
            img.setImageResource(R.drawable.r1g1b1);
        }
        else if(count[0]!=0 && count[2]!=0 && count[3] !=0){
            img.setImageResource(R.drawable.r1y1b1);
        }
        else if(count[1]!=0 && count[2]!=0 && count[3] !=0){
            img.setImageResource(R.drawable.g1y1b1);
        }
        else if(count[0] == 3 && count[1] == 1){
            img.setImageResource(R.drawable.r3g1);
        }
        else if(count[0] == 3 && count[2] == 1){
            img.setImageResource(R.drawable.r3y1);
        }
        else if(count[0] == 3 && count[3] == 1){
            img.setImageResource(R.drawable.r3b1);
        }
        else if(count[0] == 2 && count[1] == 1){
            img.setImageResource(R.drawable.r2g1);
        }
        else if(count[0] == 2 && count[2] == 1){
            img.setImageResource(R.drawable.r2y1);
        }
        else if(count[0] == 2 && count[3] == 1){
            img.setImageResource(R.drawable.r2b1);
        }
        else if(count[0] == 2 && count[1] == 2){
            img.setImageResource(R.drawable.r2g2);
        }
        else if(count[0] == 2 && count[2] == 2){
            img.setImageResource(R.drawable.r2y2);
        }
        else if(count[0] == 2 && count[3] == 2){
            img.setImageResource(R.drawable.r2b2);
        }
        else if(count[1] == 3 && count[0] == 1){
            img.setImageResource(R.drawable.r1g3);
        }
        else if(count[1] == 3 && count[2] == 1){
            img.setImageResource(R.drawable.g3y1);
        }
        else if(count[1] == 3 && count[3] == 1){
            img.setImageResource(R.drawable.g3b1);
        }
        else if(count[1] == 2 && count[2] == 2){
            img.setImageResource(R.drawable.g2y2);
        }
        else if(count[1] == 2 && count[3] == 2){
            img.setImageResource(R.drawable.g2b2);
        }
        else if(count[1] == 2 && count[0] == 1){
            img.setImageResource(R.drawable.r1g2);
        }
        else if(count[1] == 2 && count[2] == 1){
            img.setImageResource(R.drawable.g2y1);
        }
        else if(count[1] == 2 && count[3] == 1){
            img.setImageResource(R.drawable.g2b1);
        }
        else if(count[2] == 3 && count[0] == 1){
            img.setImageResource(R.drawable.r1y3);
        }
        else if(count[2] == 3 && count[1] == 1){
            img.setImageResource(R.drawable.g1y3);
        }
        else if(count[2] == 3 && count[3] == 1){
            img.setImageResource(R.drawable.y3b1);
        }
        else if(count[2] == 2 && count[3] == 2){
            img.setImageResource(R.drawable.y2b2);
        }
        else if(count[2] == 2 && count[0] == 1){
            img.setImageResource(R.drawable.r1y2);
        }
        else if(count[2] == 2 && count[1] == 1){
            img.setImageResource(R.drawable.g1y2);
        }
        else if(count[2] == 2 && count[3] == 1){
            img.setImageResource(R.drawable.y2b1);
        }
        else if(count[3] == 3 && count[0] == 1){
            img.setImageResource(R.drawable.r1b3);
        }
        else if(count[3] == 3 && count[1] == 1){
            img.setImageResource(R.drawable.g1b3);
        }
        else if(count[3] == 3 && count[2] == 1){
            img.setImageResource(R.drawable.y1b3);
        }
        else if(count[3] == 2 && count[0] == 1){
            img.setImageResource(R.drawable.r1b2);
        }
        else if(count[3] == 2 && count[1] == 1){
            img.setImageResource(R.drawable.g1b2);
        }
        else if(count[3] == 2 && count[2] == 1){
            img.setImageResource(R.drawable.y1b2);
        }
        else if(count[0] == 1 && count[1] == 1){
            img.setImageResource(R.drawable.r1g1);
        }
        else if(count[0] == 1 && count[2] == 1){
            img.setImageResource(R.drawable.r1y1);
        }
        else if(count[0] == 1 && count[3] == 1){
            img.setImageResource(R.drawable.r1b1);
        }
        else if(count[1] == 1 && count[2] == 1){
            img.setImageResource(R.drawable.g1y1);
        }
        else if(count[1] == 1 && count[3] == 1){
            img.setImageResource(R.drawable.g1b1);
        }
        else if(count[2] == 1 && count[3] == 1){
            img.setImageResource(R.drawable.y1b1);
        }
        else if(count[0] == 4 ){
            img.setImageResource(R.drawable.red4);
        } else if(count[0] == 3){
            img.setImageResource(R.drawable.red3);
        } else if(count[0] == 2) {
            img.setImageResource(R.drawable.red2);
        } else if(count[0] == 1) {
            img.setImageResource(R.drawable.redc);
        }
        else if(count[1] == 4 ){
            img.setImageResource(R.drawable.green4);
        } else if(count[1] == 3){
            img.setImageResource(R.drawable.green3);
        } else if(count[1] == 2) {
            img.setImageResource(R.drawable.green2);
        } else if(count[1] == 1) {
            img.setImageResource(R.drawable.greenc);
        }
        else if(count[2] == 4 ){
            img.setImageResource(R.drawable.yellow4);
        } else if(count[2] == 3){
            img.setImageResource(R.drawable.yellow3);
        } else if(count[2] == 2) {
            img.setImageResource(R.drawable.yellow2);
        } else if(count[2] == 1) {
            img.setImageResource(R.drawable.yellowc);
        }
        else if(count[3] == 4 ){
            img.setImageResource(R.drawable.blue4);
        } else if(count[3] == 3){
            img.setImageResource(R.drawable.blue3);
        } else if(count[3] == 2) {
            img.setImageResource(R.drawable.blue2);
        } else if(count[3] == 1) {
            img.setImageResource(R.drawable.bluec);
        }
        else {
            img.setImageResource(0);
        }
    }
    //Divide this function into 2 function
    public  View getNewPositionId(int tag_no){
        View image;
        switch(tag_no){
            case 0:     image = findViewById(R.id.a2);
                        return image;
            case 8:     image = findViewById(R.id.b7);
                return image;
                        
            case 13:    image = findViewById(R.id.b6);
                return image;
            case 21:    image = findViewById(R.id.c4);
                return image;
            case 26:    image = (findViewById(R.id.c17));
                return image;
            case 34:    image = (findViewById(R.id.d12));
                return image;
            case 39:    image = (findViewById(R.id.d13));
                return image;
                
            case 47:    image = (findViewById(R.id.a15));
                return image;
                     

            case 1:     image = (findViewById(R.id.a3));
                return image;
                        

            case 2:     image = (findViewById(R.id.a4));
                return image;
                       

            case 3:     image = (findViewById(R.id.a5));
                return image;
                    

            case 4:     image = (findViewById(R.id.a6));
                return image;
                                    
                case 5:     image = (findViewById(R.id.b16));
                return image;
                        

            case 6:     image = (findViewById(R.id.b13));
                return image;
                        

            case 7:     image = (findViewById(R.id.b10));
                return image;
        
            case 9:     image = (findViewById(R.id.b4));
                return image;
                       

            case 10:    image = (findViewById(R.id.b1));
                return image;
                        

            case 11:    image = (findViewById(R.id.b2));
                return image;
                

            case 12:    image = (findViewById(R.id.b3));
                return image;
                    

            case 14:    image = (findViewById(R.id.b9));
                return image;
                       

            case 15:    image = (findViewById(R.id.b12));
                return image;
                       

            case 16:    image = (findViewById(R.id.b15));
                return image;
                        

            case 17:    image = (findViewById(R.id.b18));
                return image;
                       

            case 18:    image = (findViewById(R.id.c1));
                return image;
                       

            case 19:    image = (findViewById(R.id.c2));
                return image;
                        

            case 20:    image = (findViewById(R.id.c3));
                return image;
                      

            case 22:    image = (findViewById(R.id.c5));
                return image;
                

            case 23:    image = (findViewById(R.id.c6));
                return image;
                

            case 24:    image = (findViewById(R.id.c12));
                return image;
                

            case 25:    image = (findViewById(R.id.c18));
                return image;
                

            case 27:    image = (findViewById(R.id.c16));
                return image;
                

            case 28:    image = (findViewById(R.id.c15));
                return image;
                

            case 29:    image = (findViewById(R.id.c14));
                return image;
                

            case 30:    image = (findViewById(R.id.c13));
                return image;
                
            case 31:
                image = (findViewById(R.id.d3));
                return image;
                

            case 32:
                image = (findViewById(R.id.d6));
                return image;
                

            case 33:
                image = (findViewById(R.id.d9));
                return image;
                

            case 35:
                image = (findViewById(R.id.d15));
                return image;
                

            case 36:
                image = (findViewById(R.id.d18));
                return image;
            case 37:
                image = (findViewById(R.id.d17));
                return image;
                

            case 38:
                image = (findViewById(R.id.d16));
                return image;
                

            case 40:
                image = (findViewById(R.id.d10));
                return image;
                

            case 41:
                image = (findViewById(R.id.d7));
                return image;
                

            case 42:
                image = (findViewById(R.id.d4));
                return image;
                

            case 43:
                image = (findViewById(R.id.d1));
                return image;
                

            case 44:
                image = (findViewById(R.id.a18));
                return image;
                

            case 45:
                image = (findViewById(R.id.a17));
                return image;
                

            case 46:
                image = (findViewById(R.id.a16));
                return image;
                

            case 48:
                image = (findViewById(R.id.a14));
                return image;
                

            case 49:
                image = (findViewById(R.id.a13));
                return image;
                

            case 50:
                image = (findViewById(R.id.a7));
                return image;
                case 51:
                image = (findViewById(R.id.a1));
                return image;
                
            case 100: //((ImageView)findViewById(R.id.a8)).setImageResource(R.drawable.redc);
                image = (findViewById(R.id.a8));
                return image;
                
            case 101: //((ImageView)findViewById(R.id.a9)).setImageResource(R.drawable.redc);
                image = (findViewById(R.id.a9));
                return image;
                
            case 102: //((ImageView)findViewById(R.id.a10)).setImageResource(R.drawable.redc);
                image = (findViewById(R.id.a10));
                return image;
            case 103: //((ImageView)findViewById(R.id.a11)).setImageResource(R.drawable.redc);
                image = (findViewById(R.id.a11));
                return image;
            case 104: //((ImageView)findViewById(R.id.a12)).setImageResource(R.drawable.redc);
                image = (findViewById(R.id.a12));
                return image;
                
            case 105:   image = findViewById(R.id.pass1);
                /*if(home[0] == 4 ){
                    ((ImageView)image).setImageResource(R.drawable.red4);
                } else if(home[0] == 3){
                    ((ImageView)image).setImageResource(R.drawable.red3);
                } else if(home[0] == 2) {
                    ((ImageView)image).setImageResource(R.drawable.red2);
                } else if(home[0] == 1) {
                    ((ImageView)image).setImageResource(R.drawable.redc);
                }*/
                return image;
            case 200: //((ImageView)findViewById(R.id.b5)).setImageResource(R.drawable.greenc);
                image = (findViewById(R.id.b5));
                return image;
                
            case 201: //((ImageView)findViewById(R.id.b8)).setImageResource(R.drawable.greenc);
                image = (findViewById(R.id.b8));
                return image;
            case 202: //((ImageView)findViewById(R.id.b11)).setImageResource(R.drawable.greenc);
                image = (findViewById(R.id.b11));
                return image;
                
            case 203: //((ImageView)findViewById(R.id.b14)).setImageResource(R.drawable.greenc);
                image = (findViewById(R.id.b14));
                return image;

            case 204: //((ImageView)findViewById(R.id.b17)).setImageResource(R.drawable.greenc);
                image = (findViewById(R.id.b17));
                return image;
            case 205:   image = findViewById(R.id.pass2);
                /*if(home[1] == 4 ){
                    ((ImageView)image).setImageResource(R.drawable.green4);
                } else if(home[1] == 3){
                    ((ImageView)image).setImageResource(R.drawable.green3);
                } else if(home[1] == 2) {
                    ((ImageView)image).setImageResource(R.drawable.green2);
                } else if(home[1] == 1) {
                    ((ImageView)image).setImageResource(R.drawable.greenc);
                }*/
                return image;
            case 300: //((ImageView)findViewById(R.id.c11)).setImageResource(R.drawable.yellowc);
                image = (findViewById(R.id.c11));
                return image;
            case 301: //((ImageView)findViewById(R.id.c10)).setImageResource(R.drawable.yellowc);
                image = (findViewById(R.id.c10));
                return image;
            case 302: //((ImageView)findViewById(R.id.c9)).setImageResource(R.drawable.yellowc);
                image = (findViewById(R.id.c9));
                return image;
            case 303: //((ImageView)findViewById(R.id.c8)).setImageResource(R.drawable.yellowc);
                image = (findViewById(R.id.c8));
                return image;

            case 304: //((ImageView)findViewById(R.id.c7)).setImageResource(R.drawable.yellowc);
                image = (findViewById(R.id.c7));
                return image;

            case 305:   image = findViewById(R.id.pass3);
                /*if(home[2] == 4 ){
                    ((ImageView)image).setImageResource(R.drawable.yellow4);
                } else if(home[2] == 3){
                    ((ImageView)image).setImageResource(R.drawable.yellow3);
                } else if(home[2] == 2) {
                    ((ImageView)image).setImageResource(R.drawable.yellow2);
                } else if(home[2] == 1) {
                    ((ImageView)image).setImageResource(R.drawable.yellowc);
                }*/
                return image;
            case 400: //((ImageView)findViewById(R.id.d14)).setImageResource(R.drawable.bluec);
                image = (findViewById(R.id.d14));
                return image;
            case 401: //((ImageView)findViewById(R.id.d11)).setImageResource(R.drawable.bluec);
                image = (findViewById(R.id.d11));
                return image;

            case 402: //((ImageView)findViewById(R.id.d8)).setImageResource(R.drawable.bluec);
                image = (findViewById(R.id.d8));
                return image;

            case 403: //((ImageView)findViewById(R.id.d5)).setImageResource(R.drawable.bluec);
                image = (findViewById(R.id.d5));
                return image;

            case 404: //((ImageView)findViewById(R.id.d2)).setImageResource(R.drawable.bluec);
                image = (findViewById(R.id.d2));
                return image;

            case 405:
                image = findViewById(R.id.pass4);
                /*if(home[3] == 4 ){
                    ((ImageView)image).setImageResource(R.drawable.blue4);
                } else if(home[3] == 3){
                    ((ImageView)image).setImageResource(R.drawable.blue3);
                } else if(home[3] == 2) {
                    ((ImageView)image).setImageResource(R.drawable.blue2);
                } else if(home[3] == 1){

                    ((ImageView)image).setImageResource(R.drawable.bluec);
                }*/
                return image;
            case 60:
                image = findViewById(R.id.fim4);
                return image;
            case 61:
                image = findViewById(R.id.fim1);
                return image;
            case 62:
                image = findViewById(R.id.fim2);
                return image;
            case 63:
                image = findViewById(R.id.fim3);
                return image;
            case 70:
                image = findViewById(R.id.fim8);
                return image;
            case 71:
                image = findViewById(R.id.fim5);
                return image;
            case 72:
                image = findViewById(R.id.fim6);
                return image;
            case 73:
                image = findViewById(R.id.fim7);
                return image;
            case 80:
                image = findViewById(R.id.fim16);
                return image;
            case 81:
                image = findViewById(R.id.fim13);
                return image;
            case 82:
                image = findViewById(R.id.fim14);
                return image;
            case 83:
                image = findViewById(R.id.fim15);
                return image;
            case 90:
                image = findViewById(R.id.fim12);
                return image;
            case 91:
                image = findViewById(R.id.fim9);
                return image;
            case 92:
                image = findViewById(R.id.fim10);
                return image;
            case 93:
                image = findViewById(R.id.fim11);
                return image;
            case 111:
                image = findViewById(R.id.dim0);
                return image;
            case 222:
                image = findViewById(R.id.dim1);
                return image;
            case 333:
                image = findViewById(R.id.dim2);
                return image;
            case 444:
                image = findViewById(R.id.dim3);
                return image;

            default:
                return image = findViewById(R.id.a2);
        }
    }

    public void reposition(int position){
        byte[] count = new byte[]{0, 0, 0, 0};
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(coinPosition[i][j] == position){
                    count[i]+=1;
                }
            }
        }

        View view = getNewPositionId(position);
        displayCoins(((ImageView)view),count);
    }
    public void winnerAnnounce(){
        String setter;
        if(playerId == 0){
            winColor[0] = true;
            TextView t1 = findViewById(R.id.t1);
            setter = String.format("%d Wins"+playerName[0],winPosition);
            t1.setText(setter);
        }
        else if(playerId == 1){
            winColor[1] = true;
            TextView t1 = findViewById(R.id.t2);
            setter = String.format("%d Wins"+playerName[1],winPosition);
            t1.setText(setter);
        }
        else if(playerId == 2){
            winColor[2] = true;
            TextView t1 = findViewById(R.id.t3);
            setter = String.format("%d Wins"+playerName[2],winPosition);
            t1.setText(setter);
        }
        else{
            winColor[3] = true;
            TextView t1 = findViewById(R.id.t4);
            setter = String.format("%d Wins"+playerName[3],winPosition);
            t1.setText(setter);
        }
        if(winPosition == losePosition){
            activeGame = false;
            ((ImageView)findViewById(R.id.dim2)).setImageResource(0);
            ((ImageView)findViewById(R.id.dim1)).setImageResource(0);
            ((ImageView)findViewById(R.id.dim0)).setImageResource(0);
            ((ImageView)findViewById(R.id.dim3)).setImageResource(0);
            if(winColor[0] == false){
                TextView t1 = findViewById(R.id.t1);
                setter = playerName[0]+"'s Turn";
                t1.setText(setter);
            }
            else if(winColor[1] == false){
                TextView t1 = findViewById(R.id.t2);
                setter = playerName[1]+"'s Loser";
                t1.setText(setter);
            }
            else if(winColor[2] == false){
                TextView t1 = findViewById(R.id.t3);
                setter = playerName[2]+"'s Loser";
                t1.setText(setter);
            }
            else{
                TextView t1 = findViewById(R.id.t4);
                setter = playerName[2]+"'s Loser";
                t1.setText(setter);
            }
        }
    }
    public int getNim(int im){
        int nim;
        if(im>=100){
            if(rd + (im%100) <= 5){
                nim = im + rd;
                if(nim%100 == 5){
                    home[playerId]+=1;
                    activeRoll=true;
                    if(home[playerId] == 4){
                        activeRoll = false;
                        winPosition+=1;
                        winnerAnnounce();
                    }
                    setColorStatus(playerId);
                }
            }
                            /*else if(nim%100 == 5) {
                                activeMove = true;
                                activeOpen = true;
                            }*/
            else {
                activeMove = true;
                nim = im;
                if(rd == 6){
                    activeOpen = true;
                }
            }
        }
        else{
            if (playerId == 0) {
                nim = (im + rd);
                if (im < 51 && nim - 50 > 0) {
                    nim = (im + rd) - 51 + 100;
                }
            } else if (playerId == 1) {
                nim = (im + rd) % 52;
                if (im < 12 && nim - 11 > 0) {
                    nim = (nim - 12) + 200;
                }
            } else if (playerId == 2) {
                nim = (im + rd) % 52;
                if (im < 25 && nim - 24 > 0) {
                    nim = (nim - 25) + 300;
                }
            } else {
                nim = (im + rd) % 52;
                if (im < 38 && nim - 37 > 0) {
                    nim = (nim - 38) + 400;
                }
            }
            if(nim%100 == 5 && nim>100){
                home[playerId]+=1;
                activeRoll = true;
                if(home[playerId] == 4){
                    activeRoll = false;
                    winPosition+=1;
                    winnerAnnounce();
                }
                setColorStatus(playerId);
            }
        }
        return nim;
    }
    public void moveStep(View view){
        if(!activeGame) {
            initialState();
        }
        else if(activeMove) {
            ImageView img = (ImageView)view;
            int im = Integer.parseInt(img.getTag().toString());

            //  String str1 = valueOf(coinPosition[playerId][pos])+" "+valueOf(coinPosition[playerId][pos+1])+" "+valueOf(coinPosition[playerId][pos+2])+" "+ coinPosition[playerId][pos + 3];
            // Toast.makeText(this,str1,Toast.LENGTH_LONG).show();
            if((coinPosition[playerId][0] == im) || (coinPosition[playerId][1] == im) ||
                    (coinPosition[playerId][2] == im) || (coinPosition[playerId][3] == im)){
                /*if(coinPosition[playerId][0] == im) {
                    coinPosition[playerId][0] = (coinPosition[playerId][0] + rd) % 52;
                }
                else if(coinPosition[playerId][1] == im){
                    coinPosition[playerId][1] = (coinPosition[playerId][1] + rd)%52;
                }
                else if(coinPosition[playerId][2] == im){
                    coinPosition[playerId][2] = (coinPosition[playerId][2] + rd)%52;
                }
                else if(coinPosition[playerId][3] == im){
                    coinPosition[playerId][3] = (coinPosition[playerId][3] + rd)%52;
                }*/
                activeMove = false;
                activeOpen = false;
                nim = getNim(im);

                /*if(defstop.contains(im)){
                    reposition(im);
                }else {
                    img.setImageResource(0);
                }*/


                if(coinPosition[playerId][0] == im) {
                    coinPosition[playerId][0] = nim;
                    coinDistance[playerId][0] += rd;
                    //coinDanger[playerId][0] = 0;

                }
                else if(coinPosition[playerId][1] == im){
                    coinPosition[playerId][1] = nim;
                    coinDistance[playerId][1] += rd;
                    //coinDanger[playerId][1] = 0;
                }
                else if(coinPosition[playerId][2] == im){
                    coinPosition[playerId][2] = nim;
                    coinDistance[playerId][2] += rd;
                    //coinDanger[playerId][2] = 0;
                }
                else if(coinPosition[playerId][3] == im){
                    coinPosition[playerId][3] = nim;
                    coinDistance[playerId][3] = 0;
                    //coinDanger[playerId][3] = 0;
                }
                else {

                }
                img.setImageResource(0);
                reposition(im);


                //String str1 = valueOf(nim);
                //Toast.makeText(this,str1,Toast.LENGTH_LONG).show();
                //((ImageView)findViewById(R.id.a10)).setImageResource(R.drawable.redc);

                if(!defstop.contains(nim)) {
                    cutting(nim);
                }

                reposition(nim);

                if(cutCoin || rd==6) {
                    cutCoin = false;
                    activeRoll = true;
                    if(playerId != realPlayer){
                        ((ImageView)rollView).setImageResource(R.drawable.dice6);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rollDice(rollView);
                            }
                        },1000);
                    }
                }else {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            nextMove();
                        }
                    },1000);
                }
            }

        }
    }
}
