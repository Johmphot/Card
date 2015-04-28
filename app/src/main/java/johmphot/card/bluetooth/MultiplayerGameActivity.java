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

package johmphot.card.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.IOException;
import java.util.Random;

import android.util.Log;

import johmphot.card.Card;
import johmphot.card.FinishActivity;
import johmphot.card.R;


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
    private ImageView shieldIcon, opponentShieldIcon;
    private Button endTurnButton;

    /**
     * Animation stuffs
     */
    private TranslateAnimation anim;
    final int amountToMoveRight = 0;
    final int amountToMoveDown = 200;

    /**
     * Phone's vibrator
     */
    private Vibrator vibrator;

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
    MediaPlayer bgMusic;
    public static boolean isServer;


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

        btGameService = new MultiplayerGameService(getActivity(), mHandler);
        isServer = true;

        bgMusic = MediaPlayer.create(getActivity(), R.raw.ingame);
        bgMusic.start();
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
        }
        try
        {
            setupGame();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (btGameService != null) {
            btGameService.stop();
        }
        bgMusic.stop();
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
                btGameService.start();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_multiplayer_game, container, false);
    }

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


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

        yourBlood[0] = (ImageView) view.findViewById(R.id.blood11);
        yourBlood[1] = (ImageView) view.findViewById(R.id.blood12);
        yourBlood[2] = (ImageView) view.findViewById(R.id.blood13);
        yourBlood[3] = (ImageView) view.findViewById(R.id.blood14);
        yourBlood[0].setImageResource(R.drawable.ghp);
        yourBlood[1].setImageResource(R.drawable.yhp);
        yourBlood[2].setImageResource(R.drawable.ohp);
        yourBlood[3].setImageResource(R.drawable.rhp);

        opponentBlood[0] = (ImageView) view.findViewById(R.id.blood21);
        opponentBlood[1] = (ImageView) view.findViewById(R.id.blood22);
        opponentBlood[2] = (ImageView) view.findViewById(R.id.blood23);
        opponentBlood[3] = (ImageView) view.findViewById(R.id.blood24);
        opponentBlood[0].setImageResource(R.drawable.ghp);
        opponentBlood[1].setImageResource(R.drawable.yhp);
        opponentBlood[2].setImageResource(R.drawable.ohp);
        opponentBlood[3].setImageResource(R.drawable.rhp);

        handCard[0] = (ImageView) view.findViewById(R.id.card1);
        handCard[1] = (ImageView) view.findViewById(R.id.card2);
        handCard[2] = (ImageView) view.findViewById(R.id.card3);
        handCard[3] = (ImageView) view.findViewById(R.id.card4);

        fieldCardImage = (ImageView) view.findViewById(R.id.field_card);
        fieldCardImage.setVisibility(View.INVISIBLE);

        shieldIcon = (ImageView) view.findViewById(R.id.shieldIcon);
        shieldIcon.setImageResource(R.drawable.shield);
        shieldIcon.setVisibility(View.INVISIBLE);

        opponentShieldIcon = (ImageView) view.findViewById(R.id.opponentShieldIcon);
        opponentShieldIcon.setImageResource(R.drawable.shield);
        opponentShieldIcon.setVisibility(View.INVISIBLE);

        endTurnButton = (Button) view.findViewById(R.id.end_button);

        final int amountToMoveRight = 0;
        final int amountToMoveDown = 200;
        anim = new TranslateAnimation(0, amountToMoveRight, 0, amountToMoveDown);
        anim.setDuration(1000);
        anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fieldCardImage.getLayoutParams();
                params.topMargin += amountToMoveDown;
                params.leftMargin += amountToMoveRight;
                fieldCardImage.setLayoutParams(params);
            }
        });

        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * Set up the UI and background operations for chat.
     */
    public void setupGame() throws IOException
    {
        game = new MultiplayerGame();
        game.start();
        updateBloodUI();
        updateHandUI();

        // Initialize the send button with a listener that for click events
        setHandButton();

        setEndButton();

        Log.i("Status", " "+isServer);

        if(!isServer)
        {
            Log.i("Not Server","");
            for(int i=0;i<4;i++)
            {
                handCard[i].setEnabled(false);
            }
            endTurnButton.setEnabled(false);
            fieldCardImage.setVisibility(View.INVISIBLE);
            game.endTurn();
            clearField();
            sendData(new Card (0, 0, "End_turn"));
        }

    }


    private void setHandButton()
    {
        for(int i=0;i<4;i++)
        {
            final int n = i;
            handCard[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    game.buffer = game.playerCard[n];
                    if(game.buffer.getValue()==1 && !game.canAttack)
                    {
                        Toast.makeText(getActivity(), "Can attack only once per turn", Toast.LENGTH_SHORT).show();
                    }
                    else if (game.buffer.getValue()==2 && game.playerHP==4)
                    {
                        Toast.makeText(getActivity(), "HP is already full", Toast.LENGTH_SHORT).show();
                    }
                    else if (handCard[n]!=null)
                    {
                        fieldCardImage.setVisibility(View.VISIBLE);
                        game.playerCard[n]=null;
                        checkUsedCard(game.buffer);
                        try
                        {
                            sendData(game.buffer);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        updateBloodUI();
                        updateHandUI();
                        updateFieldUI();
                    }
                }
            });
            handCard[i].setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    game.playerCard[n]=null;
                    updateHandUI();
                    long[] pattern = {100,100};
                    vibrator.vibrate(pattern, -1);
                    return true;
                }
            });
        }
    }

    private void setEndButton()
    {
        endTurnButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                for(int i=0;i<4;i++)
                {
                    handCard[i].setEnabled(false);
                }
                endTurnButton.setEnabled(false);
                fieldCardImage.setVisibility(View.INVISIBLE);
                game.opponentShieldExpire();
                opponentShieldIcon.setVisibility(View.INVISIBLE);
                game.endTurn();
                clearField();
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
        byte[] send = Serializer.serialize(c);
        btGameService.write(send);
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
                        if (game.buffer.getValue()==0)
                        {
                            fieldCardImage.setVisibility(View.INVISIBLE);
                            checkBufferCard();
                        }
                        else
                        {
                            fieldCardImage.setVisibility(View.VISIBLE);
                            checkBufferCard();
                            updateFieldUI();

                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fieldCardImage.getLayoutParams();
                            params.topMargin -= amountToMoveDown;
                            params.leftMargin -= amountToMoveRight;
                            fieldCardImage.setLayoutParams(params);
                            fieldCardImage.startAnimation(anim);
                        }
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


    /**
     *  Update UI
     */
    public void updateHandUI()
    {
        for(int i=0;i<4;i++)
        {
            if(game.playerCard[i]==null)
            {
                handCard[i].setImageResource(R.drawable.blackcard);
                handCard[i].setEnabled(false);
                handCard[i].setVisibility(View.INVISIBLE);
            }
            else
            {
                handCard[i].setImageResource(game.playerCard[i].getImage());
            }
        }
    }

    public void updateFieldUI()
    {
        if(game.buffer!=null)
        {
            fieldCardImage.setImageResource(game.buffer.getImage());
        }
    }

    public void updateBloodUI()
    {

        if(game.playerHP==4)
        {
            for(ImageView blood : yourBlood)
            {
                blood.setVisibility(View.VISIBLE);
            }
        }
        else if (game.playerHP==3)
        {
            yourBlood[0].setVisibility(View.INVISIBLE);
            yourBlood[1].setVisibility(View.VISIBLE);
            yourBlood[2].setVisibility(View.VISIBLE);
            yourBlood[3].setVisibility(View.VISIBLE);
        }
        else if (game.playerHP==2)
        {
            yourBlood[0].setVisibility(View.INVISIBLE);
            yourBlood[1].setVisibility(View.INVISIBLE);
            yourBlood[2].setVisibility(View.VISIBLE);
            yourBlood[3].setVisibility(View.VISIBLE);
        }
        else if (game.playerHP==1)
        {
            yourBlood[0].setVisibility(View.INVISIBLE);
            yourBlood[1].setVisibility(View.INVISIBLE);
            yourBlood[2].setVisibility(View.INVISIBLE);
            yourBlood[3].setVisibility(View.VISIBLE);
        }
        else if (game.playerHP==0)
        {
            yourBlood[0].setVisibility(View.INVISIBLE);
            yourBlood[1].setVisibility(View.INVISIBLE);
            yourBlood[2].setVisibility(View.INVISIBLE);
            yourBlood[3].setVisibility(View.INVISIBLE);

            Toast.makeText(getActivity(), "Opponent win !", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getActivity(), FinishActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        if(game.opponentHP==4)
        {
            for(ImageView blood : opponentBlood)
            {
                blood.setVisibility(View.VISIBLE);
            }
        }
        else if (game.opponentHP==3)
        {
            opponentBlood[0].setVisibility(View.INVISIBLE);
            opponentBlood[1].setVisibility(View.VISIBLE);
            opponentBlood[2].setVisibility(View.VISIBLE);
            opponentBlood[3].setVisibility(View.VISIBLE);
        }
        else if (game.opponentHP==2)
        {
            opponentBlood[0].setVisibility(View.INVISIBLE);
            opponentBlood[1].setVisibility(View.INVISIBLE);
            opponentBlood[2].setVisibility(View.VISIBLE);
            opponentBlood[3].setVisibility(View.VISIBLE);
        }
        else if (game.opponentHP==1)
        {
            opponentBlood[0].setVisibility(View.INVISIBLE);
            opponentBlood[1].setVisibility(View.INVISIBLE);
            opponentBlood[2].setVisibility(View.INVISIBLE);
            opponentBlood[3].setVisibility(View.VISIBLE);
        }
        else if (game.opponentHP==0)
        {
            opponentBlood[0].setVisibility(View.INVISIBLE);
            opponentBlood[1].setVisibility(View.INVISIBLE);
            opponentBlood[2].setVisibility(View.INVISIBLE);
            opponentBlood[3].setVisibility(View.INVISIBLE);

            Toast.makeText(getActivity(), "You win !", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getActivity(), FinishActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        Log.i("your HP"+game.playerHP,"opponent HP"+game.opponentHP);
    }

    public void clearField()
    {
        fieldCardImage.setVisibility(View.INVISIBLE);
        game.buffer = null;
    }

    /**
     *  Check used card's value
     */
    public void checkUsedCard(Card c)
    {
        switch (c.getValue())
        {
            case 1:
                game.attack();
                updateBloodUI();
                Log.i("use attack card", ""+game.opponentHP);
                break;
            case 2:
                game.heal();
                updateBloodUI();
                Log.i("use heal card", ""+game.playerHP);
                break;
            case 3:
                game.meesuk();
                break;
            case 4:
                game.swapHP();
                updateBloodUI();
                break;
            case 5:
                game.suicide();
                updateBloodUI();
                break;
            case 6:
                //discard
                break;
            case 7:
                shieldIcon.setVisibility(View.VISIBLE);
                game.shield();
                break;
            default:
                break;
        }
    }
    /**
     *  Check card's value on field
     */
    public void checkBufferCard()
    {
        //check a card received from bt stream
        switch (game.buffer.getValue())
        {
            /**
             * Start new turn
             */
            case 0:
                clearField();
                game.startTurn();
                shieldIcon.setVisibility(View.INVISIBLE);
                Log.i("EndTURN your HP " + game.playerHP, "opponent HP " + game.opponentHP);
                updateHandUI();
                updateBloodUI();
                for(int i=0;i<4;i++)
                {
                    if(game.playerCard[i]==null)
                    {
                        game.draw();
                    }
                    handCard[i].setEnabled(true);
                    handCard[i].setVisibility(View.VISIBLE);
                }
                endTurnButton.setEnabled(true);
                setHandButton();
                break;

            case 1:
                game.opponentAttack();
                vibrator.vibrate(100);
                updateBloodUI();
                Log.i("get attack", ""+game.playerHP);
                break;
            case 2:
                game.opponentHeal();
                updateBloodUI();
                Log.i("opponent heal", ""+game.opponentHP);
                break;
            case 3:
                //meesuk
                break;
            case 4:
                game.swapHP();
                updateBloodUI();
                break;
            case 5:
                game.opponentSuicide();
                updateBloodUI();
                break;
            case 6:
                Random r = new Random();
                int R = r.nextInt(4);
                while(game.playerCard[R]==null)
                {
                    R = r.nextInt(4);
                }
                game.playerCard[R]=null;
                updateHandUI();
                break;
            case 7:
                opponentShieldIcon.setVisibility(View.VISIBLE);
                game.opponentShield();
                break;
            default:
                break;
        }
    }

}

