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

import johmphot.card.Card;
import johmphot.card.R;

public class p2 extends ActionBarActivity {

    public static ArrayList<Card> blackCard = new ArrayList<Card>();

    private void checkHP2(){

        ImageView img0 = (ImageView) findViewById(R.id.imageView28);
        ImageView img1 = (ImageView) findViewById(R.id.imageView27);
        ImageView img2 = (ImageView) findViewById(R.id.imageView26);
        ImageView img3 = (ImageView) findViewById(R.id.imageView25);

        img0.setImageResource(R.drawable.ghp);
        img1.setImageResource(R.drawable.yhp);
        img2.setImageResource(R.drawable.ohp);
        img3.setImageResource(R.drawable.rhp);

        if (p1.HP1 == 4) {
            img0.setVisibility(View.VISIBLE);
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.VISIBLE);
        }
        if (p1.HP1 == 3) {
            img0.setVisibility(View.INVISIBLE);
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.VISIBLE);
        }
        if (p1.HP1 == 2) {
            img0.setVisibility(View.INVISIBLE);
            img1.setVisibility(View.INVISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.VISIBLE);
        }
        if (p1.HP1 == 1) {
            img0.setVisibility(View.INVISIBLE);
            img1.setVisibility(View.INVISIBLE);
            img2.setVisibility(View.INVISIBLE);
            img3.setVisibility(View.VISIBLE);
        }
        if (p1.HP1 == 0) {
            Intent intent = new Intent(p2.this, end.class);
            startActivity(intent);
            finish();
        }

        ImageView img4 = (ImageView) findViewById(R.id.imageView32);
        ImageView img5 = (ImageView) findViewById(R.id.imageView31);
        ImageView img6 = (ImageView) findViewById(R.id.imageView30);
        ImageView img7 = (ImageView) findViewById(R.id.imageView29);

        img4.setImageResource(R.drawable.ghp);
        img5.setImageResource(R.drawable.yhp);
        img6.setImageResource(R.drawable.ohp);
        img7.setImageResource(R.drawable.rhp);

        if (p1.HP2 == 4) {
            img4.setVisibility(View.VISIBLE);
            img5.setVisibility(View.VISIBLE);
            img6.setVisibility(View.VISIBLE);
            img7.setVisibility(View.VISIBLE);
        }
        if (p1.HP2 == 3) {
            img4.setVisibility(View.INVISIBLE);
            img5.setVisibility(View.VISIBLE);
            img6.setVisibility(View.VISIBLE);
            img7.setVisibility(View.VISIBLE);
        }
        if (p1.HP2 == 2) {
            img4.setVisibility(View.INVISIBLE);
            img5.setVisibility(View.INVISIBLE);
            img6.setVisibility(View.VISIBLE);
            img7.setVisibility(View.VISIBLE);

        }
        if (p1.HP2 == 1) {
            img4.setVisibility(View.INVISIBLE);
            img5.setVisibility(View.INVISIBLE);
            img6.setVisibility(View.INVISIBLE);
            img7.setVisibility(View.VISIBLE);

        }
        if (p1.HP2 == 0) {
            Intent intent = new Intent(p2.this, end.class);
            startActivity(intent);
            finish();
        }
    }
    private void switchTurns(){
        // t2's turn
        if(p1.t1 == 1){
            p1.t1 = 0;
            p1.t2 = 1;
        }else{
            p1.t1 = 1;
            p1.t2 = 0;
        }
    }
    private void attack(int playerNo, int attackPoints){
        // playerNo = attack on that player
        if(playerNo == 2){
            p1.HP2 = p1.HP2 - attackPoints;
        }else{
            p1.HP1 = p1.HP1 - attackPoints;
        }
    }
    private void heal(int playerNo, int healPoints){
        // playerNo = heal that player
        if(playerNo == 2){
            if(p1.HP2<4) {
                Log.i("HP2",p1.HP2+"1");
                p1.HP2 = p1.HP2 + healPoints;
            }else{
                Log.i("HP2","Exceeding");
            }
        }else{
            if(p1.HP1<4){
                Log.i("HP1",p1.HP1+"1");
                p1.HP1 = p1.HP1 + healPoints;
            }else{
                Log.i("HP1","Exceeding");
            }
        }
    }
    private void meesuk(int playerNo){
        if(playerNo == 1){
            p1.t1 = 1;
            p1.t2 = 0;
        }else{
            p1.t1 = 0;
            p1.t2 = 1;
        }
    }
    private void swapHP(){
        int i;
        i=p1.HP1;
        p1.HP1=p1.HP2;
        p1.HP2=i;
    }

    public void onClickRestart2(View v){
        p1.count1=0;
        p1.count2=0;
        Intent intent = new Intent(p2.this, MainLocal.class);
        startActivity(intent);
        finish();
    }
    //    public void showP1(){
//        if(p1.t2==1){
//            Intent intent = new Intent(p2.this, p1.class);
//            startActivity(intent);
//            finish();
//        }
//    }
    public void onClickChange2to1(View v){
        p1.t1 = 0;
        p1.t2 = 1;
        Intent intent = new Intent(p2.this, p1.class);
        startActivity(intent);
        finish();
    }

    public void onClickp21(View v) {
        ImageView imgnew1 = (ImageView) findViewById(R.id.imageView13);
        if (p1.t2 == 0) {
            if(imgnew1.getTag() != null && imgnew1.getTag().toString().equals("blackcard")){
                Log.i("21", "Already Black");
                return;
            } else if(p1.player2Hand[0].getValue() == 0) {
                Log.i("21", "Heal");
                heal(2, 1);
            } else if (p1.player2Hand[0].getValue() == 1) {
                Log.i("21", "Attack");
                attack(1,1);
                switchTurns();
            } else if(p1.player2Hand[0].getValue() == 4) {
                Log.i("21","swaphp");
                swapHP();
            }

            p1.p2hused[0] = p1.player2Hand[0];
            checkHP2();


            imgnew1.setImageResource(p1.blackCard.get(0).getImage());
            imgnew1.setTag("blackcard");

            p1.player2Hand[0] = p1.cardD.get(0);
            p1.cardD.remove(0);

            //showP1();
        } else {
            if(imgnew1.getTag() != null && imgnew1.getTag().toString().equals("blackcard")){
                Log.i("21", "Already Black");
                return;
            } else if (p1.player2Hand[0].getValue() == 3) {
                Log.i("21", "meesuk");
                meesuk(1);
            } else if (p1.player2Hand[0].getValue() == 0) {
                Log.i("21", "Heal");
                heal(2, 1);
            } else if(p1.player2Hand[0].getValue() == 1){
                Log.i("21","no quota");
                return;
            } else if(p1.player2Hand[0].getValue() == 4) {
                Log.i("21","swaphp");
                swapHP();
            }
            p1.p2hused[0]=p1.player2Hand[0];
            checkHP2();


            imgnew1.setImageResource(p1.blackCard.get(0).getImage());
            imgnew1.setTag("blackcard");

            p1.player2Hand[0] = p1.cardD.get(0);
            p1.cardD.remove(0);
        }

    }
    public void onClickp22(View v) {
        ImageView imgnew2 = (ImageView) findViewById(R.id.imageView14);
        if (p1.t2 == 0) {
            if (imgnew2.getTag() != null && imgnew2.getTag().toString().equals("blackcard")) {
                Log.i("22", "Already Black");
                return;
            } else if(p1.player2Hand[1].getValue() == 0) {
                Log.i("22", "Heal");
                heal(2, 1);
            } else if (p1.player2Hand[1].getValue() == 1) {
                Log.i("22", "Attack");
                attack(1, 1);
                switchTurns();
            } else if(p1.player2Hand[1].getValue() == 4) {
                Log.i("22","swaphp");
                swapHP();
            }

            p1.p2hused[1] = p1.player2Hand[1];
            checkHP2();


            imgnew2.setImageResource(p1.blackCard.get(0).getImage());
            imgnew2.setTag("blackcard");

            p1.player2Hand[1] = p1.cardD.get(0);
            p1.cardD.remove(0);

            //showP1();
        } else if(p1.t2 == 1){
            if(imgnew2.getTag() != null && imgnew2.getTag().toString().equals("blackcard")){
                Log.i("22", "Already Black");
                return;
            } else if (p1.player2Hand[1].getValue() == 3) {
                Log.i("22", "meesuk");
                meesuk(1);
            } else if (p1.player2Hand[1].getValue() == 0) {
                Log.i("22", "Heal");
                heal(2, 1);
            } else if(p1.player2Hand[1].getValue() == 1){
                Log.i("22","no quota");
                return;
            } else if(p1.player2Hand[1].getValue() == 4) {
                Log.i("22","swaphp");
                swapHP();
            }
            p1.p2hused[1] = p1.player2Hand[1];
            checkHP2();


            imgnew2.setImageResource(p1.blackCard.get(0).getImage());
            imgnew2.setTag("blackcard");

            p1.player2Hand[1] = p1.cardD.get(0);
            p1.cardD.remove(0);
        }
    }
    public void onClickp23(View v) {
        ImageView imgnew3 = (ImageView) findViewById(R.id.imageView15);
        if (p1.t2 == 0) {
            if(imgnew3.getTag() != null && imgnew3.getTag().toString().equals("blackcard")){
                Log.i("23", "Already Black");
                return;
            } else if(p1.player2Hand[2].getValue() == 0) {
                Log.i("23", "Heal");
                heal(2, 1);
            } else if(p1.player2Hand[2].getValue() == 1) {
                Log.i("23", "Attack");
                attack(1,1);
                switchTurns();
            } else if(p1.player2Hand[2].getValue() == 4) {
                Log.i("23","swaphp");
                swapHP();
            }

            p1.p2hused[2]=p1.player2Hand[2];
            checkHP2();


            imgnew3.setImageResource(p1.blackCard.get(0).getImage());
            imgnew3.setTag("blackcard");

            p1.player2Hand[2] = p1.cardD.get(0);
            p1.cardD.remove(0);

            //showP1();
        } else if(p1.t2 == 1){
            if(imgnew3.getTag() != null && imgnew3.getTag().toString().equals("blackcard")){
                Log.i("23", "Already Black");
                return;
            } else if(p1.player2Hand[2].getValue() == 3) {
                Log.i("23", "meesuk");
                meesuk(1);
            } else if(p1.player2Hand[2].getValue() == 0) {
                Log.i("23", "Heal");
                heal(2, 1);
            } else if(p1.player2Hand[2].getValue() == 1){
                Log.i("23","no quota");
                return;
            } else if(p1.player2Hand[2].getValue() == 4) {
                Log.i("23","swaphp");
                swapHP();
            }
            p1.p2hused[2]=p1.player2Hand[2];
            checkHP2();


            imgnew3.setImageResource(p1.blackCard.get(0).getImage());
            imgnew3.setTag("blackcard");

            p1.player2Hand[2] = p1.cardD.get(0);
            p1.cardD.remove(0);
        }
    }
    public void onClickp24(View v) {
        ImageView imgnew4 = (ImageView) findViewById(R.id.imageView16);
        if (p1.t2 == 0) {
            if(imgnew4.getTag() != null && imgnew4.getTag().toString().equals("blackcard")){
                Log.i("24", "Already Black");
                return;
            } else if(p1.player2Hand[3].getValue() == 0) {
                Log.i("24", "Heal");
                heal(2, 1);
            } else if(p1.player2Hand[3].getValue() == 1) {
                Log.i("24", "Attack");
                attack(1,1);
                switchTurns();
            } else if(p1.player2Hand[3].getValue() == 4) {
                Log.i("24","swaphp");
                swapHP();
            }

            p1.p2hused[3]=p1.player2Hand[3];

            checkHP2();


            imgnew4.setImageResource(p1.blackCard.get(0).getImage());
            imgnew4.setTag("blackcard");

            p1.player2Hand[3] = p1.cardD.get(0);
            p1.cardD.remove(0);

            //showP1();
        } else if(p1.t2 == 1){
            if(imgnew4.getTag() != null && imgnew4.getTag().toString().equals("blackcard")){
                Log.i("24", "Already Black");
                return;
            } else if (p1.player2Hand[3].getValue() == 3) {
                Log.i("24", "meesuk");
                meesuk(1);
            } else if (p1.player2Hand[3].getValue() == 0) {
                Log.i("24", "Heal");
                heal(2, 1);
            } else if(p1.player2Hand[3].getValue() == 1){
                Log.i("24","no quota");
                return;
            } else if(p1.player2Hand[3].getValue() == 4) {
                Log.i("24","swaphp");
                swapHP();
            }
            p1.p2hused[3]=p1.player2Hand[3];

            checkHP2();


            imgnew4.setImageResource(p1.blackCard.get(0).getImage());
            imgnew4.setTag("blackcard");

            p1.player2Hand[3] = p1.cardD.get(0);
            p1.cardD.remove(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2);

        checkHP2();

        ImageView img4 = (ImageView) findViewById(R.id.imageView13);
        img4.setImageResource(p1.player2Hand[0].getImage());

        ImageView img5 = (ImageView) findViewById(R.id.imageView14);
        img5.setImageResource(p1.player2Hand[1].getImage());

        ImageView img6 = (ImageView) findViewById(R.id.imageView15);
        img6.setImageResource(p1.player2Hand[2].getImage());

        ImageView img7 = (ImageView) findViewById(R.id.imageView16);
        img7.setImageResource(p1.player2Hand[3].getImage());



        ImageView img8 = (ImageView) findViewById(R.id.imageView5);
        img8.setImageResource(p1.p1hused[0].getImage());

        ImageView img9 = (ImageView) findViewById(R.id.imageView6);
        img9.setImageResource(p1.p1hused[1].getImage());

        ImageView img10 = (ImageView) findViewById(R.id.imageView7);
        img10.setImageResource(p1.p1hused[2].getImage());

        ImageView img11 = (ImageView) findViewById(R.id.imageView8);
        img11.setImageResource(p1.p1hused[3].getImage());

        p1.count2++;

        Card black = new Card(2,R.drawable.blackcard,"");
        blackCard.add(black);
        for(int i = 0; i<4; i++){
            p1.p2hused[i]=blackCard.get(0);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_p2, menu);
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
