/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package johmphot.card;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple launcher activity containing a summary sample description, sample log and a custom
 * {@link android.support.v4.app.Fragment} which can display a view.
 * <p>
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */
public class MultiplayerGameActivity extends Fragment
{

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_ENABLE_BT = 3;
    private boolean isServer = false;

    // Layout Views
    private Button drawButton, readyButton;
    private ImageView playerCardImage, opponentCardImage;

    /**
     * Name of the connected device
     */
    private String btConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> btConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer btOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    /**
     * Member object for the chat services
     */
    private MultiplayerGameService btGameService = null;

    /**
     * Game object
     */
    MultiplayerGame game;
    private BluetoothDevice opponent;
    ArrayList syncData = null;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // If the adapter is null, then Bluetooth is not supported
        if (btAdapter == null)
        {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }


    @Override
    public void onStart()
    {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupGame() will then be called during onActivityResult
        if (!btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        }
        else if (btGameService == null)
        {
            try
            {
                setupGame();
            }
            catch (IOException e) {}
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (btGameService != null) {
            btGameService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (btGameService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (btGameService.getState() == MultiplayerGameService.STATE_NONE) {
                // Start the Bluetooth chat services
                btGameService.start();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_multiplayer_game, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        drawButton = (Button) view.findViewById(R.id.draw_button);
        readyButton = (Button) view.findViewById(R.id.ready_button);
        playerCardImage = (ImageView) view.findViewById(R.id.playerCard);
        opponentCardImage = (ImageView) view.findViewById(R.id.opponentCard);
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupGame() throws IOException
    {
        if(btAdapter.getBondedDevices().size()<=1 && !btAdapter.getBondedDevices().isEmpty())
        {
            opponent = btAdapter.getBondedDevices().iterator().next();
        }

        // Initialize the MultiplayerGameService to perform bluetooth connections
        btGameService = new MultiplayerGameService(getActivity(), mHandler);

        // Initialize the buffer for outgoing messages
        btOutStringBuffer = new StringBuffer("");

        game = new MultiplayerGame();
        game.start();
        if(isServer)
        {
            sendData();
        }
        else
        {
            if(syncData!=null)
            {
                game.deck = (ArrayList) syncData.get(0);
            }
        }
        game.playerCard = game.draw();
        updateUI();
        sendData();


        // Initialize the send button with a listener that for click events
        drawButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                game.playerCard = game.draw();
                updateUI();
                try
                {
                    sendData();
                }
                catch (IOException e) {}
            }
        });

        readyButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // set to ready state
            }
        });
    }

    /**
     * Makes this device discoverable.
     */
    private void ensureDiscoverable()
    {
        if (btAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
        {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
            isServer = true;
        }
    }

    /**
     * Sends a message.
     */

    private void sendData() throws IOException
    {
        // Check that we're actually connected before trying anything
        if (btGameService.getState() != MultiplayerGameService.STATE_CONNECTED)
        {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        //syncData = new ArrayList();
        //syncData.add(game.deck);
        //syncData.add(game.playerCard);
        if (game.deck!=null)
        {
            // Get the message bytes and tell the MultiplayerGameService to write
            //byte[] send = serialize(syncData);
            byte[] send = serialize(game.playerCard);
            btGameService.write(send);
        }
    }



    /**
     * The Handler that gets information back from the MultiplayerGameService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg)
        {
            FragmentActivity activity = getActivity();
            switch (msg.what)
            {
                case Constants.MESSAGE_WRITE:
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    try
                    {
                        //syncData = deserialize(readBuf);
                        //game.deck = (ArrayList<Card>) syncData.get(0);
                        //game.opponentCard = (Card) syncData.get(1);
                        game.opponentCard = deserialize(readBuf);
                        updateUI();
                    }
                    catch (IOException e) {}
                    catch (ClassNotFoundException e) {}
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    btConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity)
                    {
                        Toast.makeText(activity, "Connected to "+ btConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity)
                    {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data);
                }
                break;

            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK)
                {
                    // Bluetooth is now enabled, so set up a chat session
                    try
                    {
                        setupGame();
                    }
                    catch (IOException e) {}
                }
                else
                {
                    // User did not enable Bluetooth or an error occurred
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

    /**
     * Establish connection with other device
     *
     * @param data   An {@link android.content.Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     */
    private void connectDevice(Intent data) {
        // Get the device MAC address
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        btGameService.connect(device);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        //inflater.inflate(R.menu.multiplayer_game, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }

    public void updateUI()
    {
        if(game.playerCard!=null) playerCardImage.setImageResource(game.playerCard.getCardReference());
        if(game.opponentCard!=null) opponentCardImage.setImageResource(game.opponentCard.getCardReference());
    }

    public byte[] serialize(Card card) throws IOException
    {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(card);
        return b.toByteArray();
    }
    /*public ArrayList deserialize(byte[] bytes) throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return (ArrayList) o.readObject();
    }*/

    public Card deserialize(byte[] bytes) throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return (Card) o.readObject();
    }
}

