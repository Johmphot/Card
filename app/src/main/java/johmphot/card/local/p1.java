package johmphot.card.local;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Collections;

import johmphot.card.Card;
import johmphot.card.CardSet;
import johmphot.card.R;

public class p1 extends ActionBarActivity {

    public static int t1;
    public static int t2;
    public static int HP1;
    public static int HP2;
    public static int count1=0;
    public static int count2=0;
    public static ArrayList<Card> cardD = new ArrayList<Card>();
    public static ArrayList<Card> blackCard = new ArrayList<Card>();
    public static Card[] player1Hand = new Card[4];
    public static Card[] player2Hand = new Card[4];
    public static Card[] p1hused = new Card[4];
    public static Card[] p2hused = new Card[4];
    private CardSet set = new CardSet();

    public void createDeck() {

//        for(int i = 0; i < 30; i++){
//            Card atk = new Card(1, R.drawable.redcard,"Attack");
//            cardD.add(atk);
//
//            Card def = new Card(0, R.drawable.bluecard, "Heal");
//            cardD.add(def);
//
//        }
//        for(int i = 0; i < 5; i++){
//            Card meesuk = new Card(3, R.drawable.meesukcard, "Heal");
//            cardD.add(meesuk);
//
//            Card swaphp = new Card(4, R.drawable.greencard,"Meesuk");
//            cardD.add(swaphp);
//        }
        cardD = set.getDeck();
        Collections.shuffle(cardD);
    }

    private void checkHP1(){

        ImageView img0 = (ImageView) findViewById(R.id.imageView21);
        ImageView img1 = (ImageView) findViewById(R.id.imageView22);
        ImageView img2 = (ImageView) findViewById(R.id.imageView23);
        ImageView img3 = (ImageView) findViewById(R.id.imageView24);

        img0.setImageResource(R.drawable.rhp);
        img1.setImageResource(R.drawable.ohp);
        img2.setImageResource(R.drawable.yhp);
        img3.setImageResource(R.drawable.ghp);

        if (HP1 == 4) {
            img0.setVisibility(View.VISIBLE);
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.VISIBLE);
        }
        if (HP1 == 3) {
            img0.setVisibility(View.VISIBLE);
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.INVISIBLE);

        }
        if (HP1 == 2) {
            img0.setVisibility(View.VISIBLE);
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.INVISIBLE);
            img3.setVisibility(View.INVISIBLE);


        }
        if (HP1 == 1) {
            img0.setVisibility(View.VISIBLE);
            img1.setVisibility(View.INVISIBLE);
            img2.setVisibility(View.INVISIBLE);
            img3.setVisibility(View.INVISIBLE);

        }
        if (HP1 == 0) {
            Intent intent = new Intent(p1.this, end.class);
            startActivity(intent);
            finish();
        }

        ImageView img4 = (ImageView) findViewById(R.id.imageView17);
        ImageView img5 = (ImageView) findViewById(R.id.imageView18);
        ImageView img6 = (ImageView) findViewById(R.id.imageView19);
        ImageView img7 = (ImageView) findViewById(R.id.imageView20);

        img4.setImageResource(R.drawable.rhp);
        img5.setImageResource(R.drawable.ohp);
        img6.setImageResource(R.drawable.yhp);
        img7.setImageResource(R.drawable.ghp);

        if (HP2 == 4) {
            img4.setVisibility(View.VISIBLE);
            img5.setVisibility(View.VISIBLE);
            img6.setVisibility(View.VISIBLE);
            img7.setVisibility(View.VISIBLE);

        }
        if (HP2 == 3) {
            img4.setVisibility(View.VISIBLE);
            img5.setVisibility(View.VISIBLE);
            img6.setVisibility(View.VISIBLE);
            img7.setVisibility(View.INVISIBLE);

        }
        if (HP2 == 2) {
            img4.setVisibility(View.VISIBLE);
            img5.setVisibility(View.VISIBLE);
            img6.setVisibility(View.INVISIBLE);
            img7.setVisibility(View.INVISIBLE);

        }
        if (HP2 == 1) {
            img4.setVisibility(View.VISIBLE);
            img5.setVisibility(View.INVISIBLE);
            img6.setVisibility(View.INVISIBLE);
            img7.setVisibility(View.INVISIBLE);

        }
        if (HP2 == 0) {
            Intent intent = new Intent(p1.this, end.class);
            startActivity(intent);
            finish();
        }
    }
    private void switchTurns(){
        // t2's turn
        if(t1 == 1){
            t1 = 0;
            t2 = 1;
        }else{
            t1 = 1;
            t2 = 0;
        }
    }
    private void attack(int playerNo, int attackPoints){
        // playerNo = attack on that player
        if(playerNo == 2){
            HP2 = HP2 - attackPoints;
        }else{
            HP1 = HP1 - attackPoints;
        }
    }
    private void heal(int playerNo, int healPoints){
        // playerNo = heal that player
        if(playerNo == 2){
            if(HP2<4) {
                Log.i("HP2",HP2+"1");
                HP2 = HP2 + healPoints;
            }else{
                Log.i("HP2","Exceeding");
            }
        }else{
            if(HP1<4){
                Log.i("HP1",HP1+"1");
                HP1 = HP1 + healPoints;
            }else{
                Log.i("HP1","Exceeding");
            }
        }
    }
    private void meesuk(int playerNo){
        if(playerNo == 1){
            t1 = 1;
            t2 = 0;
        }else{
            t1 = 0;
            t2 = 1;
        }
    }
    private void swapHP(){
        int i;
        i=HP1;
        HP1=HP2;
        HP2=i;
    }

    //    public void showP2(){
//        if(t1==1){
//            Intent intent = new Intent(p1.this, p2.class);
//            startActivity(intent);
//            finish();
//        }
//    }
    public void onClickChange1to2(View v){
        t1 = 1;
        t2 = 0;
        Intent intent = new Intent(p1.this, p2.class);
        startActivity(intent);
        finish();
    }

    public void onClickp11(View v) {
        ImageView imgnew1 = (ImageView) findViewById(R.id.imageView9);
        if(t1==0) {
            if(imgnew1.getTag() != null && imgnew1.getTag().toString().equals("blackcard")){
                Log.i("11", "Already Black");
                return;
            } else if(player1Hand[0].getValue() == 0) {
                Log.i("11", "Heal");
                heal(1,1);
            } else if(player1Hand[0].getValue() == 1) {
                Log.i("11", "Attack");
                attack(2,1);
                switchTurns();
            } else if(player1Hand[0].getValue() == 4) {
                Log.i("11","swaphp");
                swapHP();
            }

            p1hused[0]=player1Hand[0];

            checkHP1();

            //change image of new card
            imgnew1.setImageResource(blackCard.get(0).getImage());
            imgnew1.setTag("blackcard");


            player1Hand[0] = cardD.get(0);
            cardD.remove(0);

            //showP2();

        } else if(t1==1) {
            if(imgnew1.getTag() != null && imgnew1.getTag().toString().equals("blackcard")){
                Log.i("11", "Already Black");
                return;
            } else if(player1Hand[0].getValue() == 3) {
                Log.i("11", "meesuk");
                meesuk(2);
            } else if(player1Hand[0].getValue() == 0) {
                Log.i("11", "Heal");
                heal(1, 1);
            } else if(player1Hand[0].getValue() == 1){
                Log.i("11","no quota");
                return;
            } else if(player1Hand[0].getValue() == 4) {
                Log.i("11","swaphp");
                swapHP();
            }
            p1hused[0]=player1Hand[0];

            checkHP1();

            //change image of new card
            imgnew1.setImageResource(blackCard.get(0).getImage());
            imgnew1.setTag("blackcard");


            player1Hand[0] = cardD.get(0);
            cardD.remove(0);
        }
    }
    public void onClickp12(View v) {
        ImageView imgnew2 = (ImageView) findViewById(R.id.imageView10);
        if (t1 == 0) {
            if(imgnew2.getTag() != null && imgnew2.getTag().toString().equals("blackcard")) {
                Log.i("12", "Already Black");
                return;
            } else if(player1Hand[1].getValue() == 0) {
                Log.i("12", "Heal");
                heal(1,1);
            } else if(player1Hand[1].getValue() == 1) {
                Log.i("12", "Attack");
                attack(2,1);
                switchTurns();
            } else if(player1Hand[1].getValue() == 4) {
                Log.i("12","swaphp");
                swapHP();
            }

            p1hused[1]=player1Hand[1];

            checkHP1();

            imgnew2.setImageResource(blackCard.get(0).getImage());
            imgnew2.setTag("blackcard");

            player1Hand[1] = cardD.get(0);
            cardD.remove(0);

            //showP2();
        } else if(t1==1){
            if(imgnew2.getTag() != null && imgnew2.getTag().toString().equals("blackcard")){
                Log.i("12", "Already Black");
                return;
            } else if (player1Hand[1].getValue() == 3) {
                Log.i("12", "meesuk");
                meesuk(2);
            } else if (player1Hand[1].getValue() == 0) {
                Log.i("12", "Heal");
                HP1 = HP1 + 1;
            } else if(player1Hand[1].getValue() == 1){
                Log.i("12","no quota");
                return;
            } else if(player1Hand[1].getValue() == 4) {
                Log.i("12","swaphp");
                swapHP();
            }
            p1hused[1]=player1Hand[1];

            checkHP1();

            imgnew2.setImageResource(blackCard.get(0).getImage());
            imgnew2.setTag("blackcard");

            player1Hand[1] = cardD.get(0);
            cardD.remove(0);
        }
    }
    public void onClickp13(View v) {
        ImageView imgnew3 = (ImageView) findViewById(R.id.imageView11);
        if (t1 == 0) {
            if(imgnew3.getTag() != null && imgnew3.getTag().toString().equals("blackcard")){
                Log.i("13", "Already Black");
                return;
            } else if(player1Hand[2].getValue() == 0) {
                Log.i("13", "Heal");
                heal(1,1);
            } else if (player1Hand[2].getValue() == 1) {
                Log.i("13", "Attack");
                attack(2,1);
                switchTurns();
            } else if(player1Hand[2].getValue() == 4) {
                Log.i("13","swaphp");
                swapHP();
            }

            p1hused[2]=player1Hand[2];

            checkHP1();

            imgnew3.setImageResource(blackCard.get(0).getImage());
            imgnew3.setTag("blackcard");

            player1Hand[2] = cardD.get(0);
            cardD.remove(0);

            //showP2();
        } else if(t1==1){
            if(imgnew3.getTag() != null && imgnew3.getTag().toString().equals("blackcard")){
                Log.i("13", "Already Black");
                return;
            } else if (player1Hand[2].getValue() == 3) {
                Log.i("13", "meesuk");
                meesuk(2);
            } else if (player1Hand[2].getValue() == 0) {
                Log.i("13", "Heal");
                HP1 = HP1 + 1;
            } else if(player1Hand[2].getValue() == 1){
                Log.i("13","no quota");
                return;
            }
            p1hused[2]=player1Hand[2];

            checkHP1();

            imgnew3.setImageResource(blackCard.get(0).getImage());
            imgnew3.setTag("blackcard");

            player1Hand[2] = cardD.get(0);
            cardD.remove(0);
        }
    }
    public void onClickp14(View v) {
        ImageView imgnew4 = (ImageView) findViewById(R.id.imageView12);
        if (t1 == 0) {
            if(imgnew4.getTag() != null && imgnew4.getTag().toString().equals("blackcard")){
                Log.i("14", "Already Black");
                return;
            } else if(player1Hand[3].getValue() == 0) {
                Log.i("14", "Heal");
                heal(1,1);
            } else if(player1Hand[3].getValue() == 1) {
                Log.i("14", "Attack");
                attack(2,1);
                switchTurns();
            } else if(player1Hand[3].getValue() == 4) {
                Log.i("14","swaphp");
                swapHP();
            }

            p1hused[3]=player1Hand[3];

            checkHP1();

            imgnew4.setImageResource(blackCard.get(0).getImage());
            imgnew4.setTag("blackcard");

            player1Hand[3] = cardD.get(0);
            cardD.remove(0);

            //showP2();
        } else if(t1==1){
            if(imgnew4.getTag() != null && imgnew4.getTag().toString().equals("blackcard")){
                Log.i("14", "Already Black");
                return;
            } else if (player1Hand[3].getValue() == 3) {
                Log.i("14", "meesuk");
                meesuk(2);
            } else if (player1Hand[3].getValue() == 0) {
                Log.i("14", "Heal");
                HP1 = HP1 + 1;
            } else if (player1Hand[3].getValue() == 1) {
                Log.i("14","no quota");
                return;
            } else if(player1Hand[3].getValue() == 4) {
                Log.i("14","swaphp");
                swapHP();
            }
            p1hused[3]=player1Hand[3];

            checkHP1();

            imgnew4.setImageResource(blackCard.get(0).getImage());
            imgnew4.setTag("blackcard");

            player1Hand[3] = cardD.get(0);
            cardD.remove(0);
        }
    }

    public void onClickRestart1(View v){
        count1=0;
        count2=0;
        Intent intent = new Intent(p1.this, MainLocal.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p1);


        createDeck();

        Card black = new Card(2,R.drawable.blackcard,"");
        blackCard.add(black);
        for(int i = 0; i<4; i++){
            p1hused[i]=blackCard.get(0);
        }
        if(count2==0){
            for(int i = 0; i<4; i++){
                p2hused[i]=blackCard.get(0);
            }
        }


        if(count1==0) {
            HP1 = 4;
            HP2 = 4;
            t1 = 0;
            t2 = 1;
            player1Hand[0] = cardD.get(0);
            cardD.remove(0);
            player1Hand[1] = cardD.get(0);
            cardD.remove(0);
            player1Hand[2] = cardD.get(0);
            cardD.remove(0);
            player1Hand[3] = cardD.get(0);
            cardD.remove(0);
            count1++;
        }


        ImageView img0 = (ImageView) findViewById(R.id.imageView9);
        img0.setImageResource(player1Hand[0].getImage());

        ImageView img1 = (ImageView) findViewById(R.id.imageView10);
        img1.setImageResource(player1Hand[1].getImage());

        ImageView img2 = (ImageView) findViewById(R.id.imageView11);
        img2.setImageResource(player1Hand[2].getImage());

        ImageView img3 = (ImageView) findViewById(R.id.imageView12);
        img3.setImageResource(player1Hand[3].getImage());



        ImageView img4 = (ImageView) findViewById(R.id.imageView33);
        img4.setImageResource(p2hused[0].getImage());

        ImageView img5 = (ImageView) findViewById(R.id.imageView34);
        img5.setImageResource(p2hused[1].getImage());

        ImageView img6 = (ImageView) findViewById(R.id.imageView35);
        img6.setImageResource(p2hused[2].getImage());

        ImageView img7 = (ImageView) findViewById(R.id.imageView36);
        img7.setImageResource(p2hused[3].getImage());

        if (count2 == 0) {
            HP1 = 4;
            HP2 = 4;
            player2Hand[0] = cardD.get(0);
            cardD.remove(0);
            player2Hand[1] = cardD.get(0);
            cardD.remove(0);
            player2Hand[2] = cardD.get(0);
            cardD.remove(0);
            player2Hand[3] = cardD.get(0);
            cardD.remove(0);



        }
        checkHP1();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_p1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}