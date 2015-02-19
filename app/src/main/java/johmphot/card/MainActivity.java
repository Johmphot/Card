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


public class MainActivity extends ActionBarActivity {

    private Button start,multiplayer;
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

        start = (Button)findViewById(R.id.start_button);
        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this,GameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        multiplayer = (Button)findViewById(R.id.multiplayer_button);
        multiplayer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, MultiplayerActivity.class);
                startActivity(intent);
                finish();
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
        if(resultCode==RESULT_CANCELED)
        {
            Toast.makeText(getApplicationContext(), "Bluetooth must be enable to continue", Toast.LENGTH_LONG).show();
            finish();
        }
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
