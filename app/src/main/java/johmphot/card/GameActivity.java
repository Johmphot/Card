package johmphot.card;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class GameActivity extends ActionBarActivity {

    final Game round = new Game();
    private ImageView[] playerCardImage = new ImageView[round.playerNum];
    private Button drawButton,drawButton2,drawButton3,drawButton4,readyButton;
    public int loser = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Resources r = getResources();
        //String name = getPackageName();
        //for(int i = 0; i < round.playerNum; i++)
        //{
        //    playerCard[i] = (ImageView) findViewById(r.getIdentifier("playercard" + i, "id", name));
        //}
        playerCardImage[0] = (ImageView)findViewById(R.id.playercard1);
        playerCardImage[1] = (ImageView)findViewById(R.id.playercard2);
        playerCardImage[2] = (ImageView)findViewById(R.id.playercard3);
        playerCardImage[3] = (ImageView)findViewById(R.id.playercard4);

        round.start();
        updateUI(round);

        drawButton = (Button)findViewById(R.id.draw_button);
        drawButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                boolean drawable = round.draw(0);
                if(!drawable)
                {
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), "No more card left", duration);
                    toast.show();
                }
                updateUI(round);
            }
        });

        //temp end
        //temporary until bluetooth multiplayer implemented
        drawButton2 = (Button)findViewById(R.id.draw_button_2);
        drawButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                boolean drawable = round.draw(1);
                if(!drawable)
                {
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), "No more card left", duration);
                    toast.show();
                }
                updateUI(round);
            }
        });
        //temp end

        //temporary until bluetooth multiplayer implemented
        drawButton3 = (Button)findViewById(R.id.draw_button_3);
        drawButton3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                boolean drawable = round.draw(2);
                if(!drawable)
                {
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), "No more card left", duration);
                    toast.show();
                }
                updateUI(round);
            }
        });
        //temp end

        //temporary until bluetooth multiplayer implemented
        drawButton4 = (Button)findViewById(R.id.draw_button_4);
        drawButton4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                boolean drawable = round.draw(3);
                if(!drawable)
                {
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), "No more card left", duration);
                    toast.show();
                }
                updateUI(round);
            }
        });

        readyButton = (Button)findViewById(R.id.ready_button);
        readyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                loser = round.findLoser(round.playerCard);
                Intent intent = new Intent(GameActivity.this,FinishActivity.class);
                intent.putExtra("loser", loser);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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

    public void updateUI(Game round)
    {
        for(int i=0;i<round.playerNum;i++)
        {
            playerCardImage[i].setImageResource(round.getPlayerCard(i).getCardReference());
        }
    }
}
