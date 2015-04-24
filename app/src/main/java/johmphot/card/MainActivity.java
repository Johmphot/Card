package johmphot.card;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import johmphot.card.bluetooth.CreateMatchActivity;
import johmphot.card.local.MainLocal;


public class MainActivity extends ActionBarActivity {

    private Button localMultiplayer, btMultiplayer;
    BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter==null)
        {
            Toast.makeText(getApplicationContext(), "No Bluetooth Detected", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            if(!btAdapter.isEnabled())
            {
                turnOnBluetooth();
            }
        }

        localMultiplayer = (Button)findViewById(R.id.local_multiplayer_button);
        localMultiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainLocal.class);
                startActivity(intent);
                finish();
            }
        });

        btMultiplayer = (Button)findViewById(R.id.bt_multiplayer_button);
        btMultiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateMatchActivity.class);
                startActivity(intent);
            }
        });


    }

    private void turnOnBluetooth()
    {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==RESULT_CANCELED)
//        {
//            Toast.makeText(getApplicationContext(), "Bluetooth must be enable to continue", Toast.LENGTH_LONG).show();
//            finish();
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
