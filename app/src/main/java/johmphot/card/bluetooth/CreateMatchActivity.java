package johmphot.card.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;

import johmphot.card.R;


public class CreateMatchActivity extends ActionBarActivity
{

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    MultiplayerGameActivity fragment = new MultiplayerGameActivity();

    MediaPlayer bgMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);
        if (savedInstanceState == null) {
            transaction.replace(R.id.frame_view, fragment);
            transaction.commit();
        }

        bgMusic = MediaPlayer.create(this, R.raw.ingame);
        bgMusic.start();

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

        IntentFilter filterScreenOff = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        IntentFilter filterScreenOn = new IntentFilter(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenReceiver, filterScreenOff);
        registerReceiver(screenReceiver, filterScreenOn);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        bgMusic.reset();
        unregisterReceiver(mReceiver);
        unregisterReceiver(screenReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.multiplayer_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return fragment.onOptionsItemSelected(item);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_CONNECTED:
                        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };

    private BroadcastReceiver screenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action.equals(Intent.ACTION_SCREEN_OFF))
            {
                bgMusic.reset();
            }
            else if(action.equals(Intent.ACTION_SCREEN_ON))
            {
                bgMusic.start();
            }
        }
    };

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        fragment.onDestroy();
        this.finish();
    }
}
