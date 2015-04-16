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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.IOException;


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
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 3;

    // Layout Views
    private ImageView[] yourBlood = new ImageView[4];
    private ImageView[] opponentBlood = new ImageView[4];
    private ImageView[] handCard = new ImageView[4];
    private ImageView fieldCardImage;
    private Button endTurnButton;

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
            catch (IOException e)
            {
                e.printStackTrace();
            }
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

        yourBlood[0] = (ImageView) view.findViewById(R.id.blood11);
        yourBlood[1] = (ImageView) view.findViewById(R.id.blood12);
        yourBlood[2] = (ImageView) view.findViewById(R.id.blood13);
        yourBlood[3] = (ImageView) view.findViewById(R.id.blood14);

        opponentBlood[0] = (ImageView) view.findViewById(R.id.blood21);
        opponentBlood[1] = (ImageView) view.findViewById(R.id.blood22);
        opponentBlood[2] = (ImageView) view.findViewById(R.id.blood23);
        opponentBlood[3] = (ImageView) view.findViewById(R.id.blood24);

        handCard[0] = (ImageView) view.findViewById(R.id.card1);
        handCard[1] = (ImageView) view.findViewById(R.id.card2);
        handCard[2] = (ImageView) view.findViewById(R.id.card3);
        handCard[3] = (ImageView) view.findViewById(R.id.card4);

        fieldCardImage = (ImageView) view.findViewById(R.id.field_card);

        endTurnButton = (Button) view.findViewById(R.id.end_button);
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupGame() throws IOException
    {

        // Initialize the MultiplayerGameService to perform bluetooth connections
        btGameService = new MultiplayerGameService(getActivity(), mHandler);

        game = new MultiplayerGame();
        game.start();
        updateBloodUI();
        updateHandUI();

        // Initialize the send button with a listener that for click events
        for(int i=0;i<4;i++)
        {
            final int n = i;
            handCard[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Card c = game.playerCard[n];
                    if(game.canAttack)
                    {
                        game.playerCard[n] = null;
                        checkUsedCard(c);
                        try
                        {
                            sendData(c);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        if(c.getValue()!=1)
                        {
                            game.playerCard[n] = null;
                            checkUsedCard(c);
                            try
                            {
                                sendData(c);
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    updateBloodUI();
                    updateHandUI();
                    updateFieldUI();
                }
            });
            handCard[i].setEnabled(false);
        }


        endTurnButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                for(int i=0;i<4;i++)
                {
                    handCard[i].setEnabled(false);
                }
                endTurnButton.setEnabled(false);
                game.endTurn();
                try
                {
                    sendData(new Card (0, 0, "End_turn"));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        endTurnButton.setEnabled(false);

        if(btAdapter.getScanMode()==BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) //player1 start first
        {
            btAdapter.cancelDiscovery();
            for(int i=0;i<4;i++)
            {
                handCard[i].setEnabled(true);
            }
            endTurnButton.setEnabled(true);
        }
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
        }
    }

    /**
     * Sends a message.
     */

    private void sendData(Card c) throws IOException
    {
        // Check that we're actually connected before trying anything
        if (btGameService.getState() != MultiplayerGameService.STATE_CONNECTED)
        {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        if (game.buffer!=null)
        {
            // Get the message bytes and tell the MultiplayerGameService to write

            byte[] send = Serializer.serialize(c);
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
                    try
                    {
                        game.buffer = (Card) Serializer.deserialize(readBuf);
                        checkBufferCard();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    /*
                        Name of the connected device
                    */
                    String btConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
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
        switch (requestCode)
        {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK)
                {
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
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
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

    /**
     *  Update UI
     */
    public void updateHandUI()
    {
        for(int i=0;i<4;i++)
        {
            if(handCard[i]==null)
            {
                handCard[i].setImageResource(R.drawable.blackcard);
            }
            else
            {
                handCard[i].setImageResource(game.playerCard[i].getImage());
            }
        }
    }

    public void updateFieldUI()
    {
        if(game.buffer!=null) fieldCardImage.setImageResource(game.buffer.getImage());
    }

    public void updateBloodUI()
    {
        for(int i=3;i>=4-game.playerHP;i--)
        {
            yourBlood[i].setImageResource(R.drawable.whp);
        }
        for(int i=0;i<4-game.playerHP;i++)
        {
            yourBlood[i].setImageResource(R.drawable.bhp);
        }

        for(int i=3;i>=4-game.opponentHP;i--)
        {
            opponentBlood[i].setImageResource(R.drawable.whp);
        }
        for(int i=0;i<4-game.opponentHP;i++)
        {
            opponentBlood[i].setImageResource(R.drawable.bhp);
        }
    }


    /**
     *  Check card's value
     */
    public void checkUsedCard(Card c)
    {
        switch (c.getValue())
        {
            case 1:
                game.attack();
                break;
            case 2:
                game.heal();
                break;
            default:
                break;
        }
    }

    public void checkBufferCard()
    {
        //check a card received from bt stream
        switch (game.buffer.getValue())
        {
            case 0:
                game.startTurn(); //opponent presses "End Turn" button
                updateHandUI();
                for(int i=0;i<4;i++)
                {
                    handCard[i].setEnabled(true);
                }
                endTurnButton.setEnabled(true);

            case 1:
                game.opponentAttack();
                break;
            case 2:
                game.opponentHeal();
                break;
            default:
                break;
        }
    }

}

