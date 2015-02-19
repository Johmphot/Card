package johmphot.card;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;


public class MultiplayerActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    BluetoothAdapter btAdapter;
    ListView listView;
    ArrayAdapter<String> listAdapter;
    Set<BluetoothDevice> deviceArray;
    ArrayList<String> pairedDevices;
    IntentFilter filter;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        initialize();

        getPairedDevices();
        startDiscovery();
        Toast.makeText(getApplicationContext(), "Finding Bluetooth Device ... ", Toast.LENGTH_SHORT).show();

    }


    private void initialize()
    {
        listView = (ListView)findViewById(R.id.deviceView);
        listView.setOnItemClickListener(this);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 0);
        listView.setAdapter(listAdapter);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        pairedDevices = new ArrayList<String>();
        filter  = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        receiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                if(BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    Toast.makeText(getApplicationContext(), "Devices Found", Toast.LENGTH_SHORT).show();
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    listAdapter.add(device.getName()+"\n"+device.getAddress());
                }
                else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
                {
                    //run some code
                }
                else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
                {
                    //run some code
                }
                else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
                {
                    if(btAdapter.getState()==BluetoothAdapter.STATE_OFF)
                    {
                        turnOnBluetooth();
                    }
                }
            }
        };
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver,filter);
    }

    private void startDiscovery()
    {
        btAdapter.cancelDiscovery();
        btAdapter.startDiscovery();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        try
        {
            unregisterReceiver(receiver);
        }
        catch(IllegalArgumentException ignored){}
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(receiver, filter);
    }

    private void turnOnBluetooth()
    {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, 1);
    }

    private void getPairedDevices()
    {
        deviceArray = btAdapter.getBondedDevices();
        if(deviceArray.size()>0)
        {
            for(BluetoothDevice device:deviceArray)
            {
                pairedDevices.add(device.getName());
            }
        }
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
        getMenuInflater().inflate(R.menu.menu_multiplayer, menu);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

    }
}
