package johmphot.card;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class GameActivity extends ActionBarActivity {

    private ImageView player1card;
    private Button drawButton;
    final Game round = new Game();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        player1card = (ImageView)findViewById(R.id.player1card);
        round.start();
        updateUI(round);

        drawButton = (Button)findViewById(R.id.draw_button);
        drawButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                round.draw(0);
                updateUI(round);
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
        player1card.setImageResource(round.getPlayerCard(0).getCardReference());
    }
}
