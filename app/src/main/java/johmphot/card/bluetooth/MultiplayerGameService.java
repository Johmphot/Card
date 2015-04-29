package johmphot.card.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */

public class MultiplayerGameService
{

    // Name for the SDP record when creating server socket
    private static final String NAME_SECURE = "BluetoothChatSecure";

    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    private final BluetoothAdapter btAdapter;
    private final Handler btHandler;
    private AcceptThread btSecureAcceptThread;
    private ConnectThread btConnectThread;
    private ConnectedThread btConnectedThread;
    private int btState;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    /**
     * Constructor. Prepares a new BluetoothChat session.
     *
     * @param context The UI Activity Context
     * @param handler A Handler to send messages back to the UI Activity
     */
    public MultiplayerGameService(Context context, Handler handler)
    {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btState = STATE_NONE;
        btHandler = handler;
    }

    /**
     * Set the current state of the chat connection
     *
     * @param state An integer defining the current connection state
     */
    private synchronized void setState(int state)
    {
        btState = state;

        // Give the new state to the Handler so the UI Activity can update
        btHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    /**
     * Return the current connection state.
     */
    public synchronized int getState()
    {
        return btState;
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start()
    {

        // Cancel any thread attempting to make a connection
        if (btConnectThread != null)
        {
            btConnectThread.cancel();
            btConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (btConnectedThread != null)
        {
            btConnectedThread.cancel();
            btConnectedThread = null;
        }

        setState(STATE_LISTEN);

        // Start the thread to listen on a BluetoothServerSocket
        if (btSecureAcceptThread == null)
        {
            btSecureAcceptThread = new AcceptThread();
            btSecureAcceptThread.start();
        }
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device)
    {

        // Cancel any thread attempting to make a connection
        if (btState == STATE_CONNECTING)
        {
            if (btConnectThread != null)
            {
                btConnectThread.cancel();
                btConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (btConnectedThread != null)
        {
            btConnectedThread.cancel();
            btConnectedThread = null;
        }

        // Start the thread to connect with the given device
        btConnectThread = new ConnectThread(device);
        btConnectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device)
    {
        // Cancel the thread that completed the connection
        if (btConnectThread != null)
        {
            btConnectThread.cancel();
            btConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (btConnectedThread != null)
        {
            btConnectedThread.cancel();
            btConnectedThread = null;
        }

        // Cancel the accept thread because we only want to connect to one device
        if (btSecureAcceptThread != null)
        {
            btSecureAcceptThread.cancel();
            btSecureAcceptThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        btConnectedThread = new ConnectedThread(socket);
        btConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        Message msg = btHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        btHandler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }

    /**
     * Stop all threads
     */
    public synchronized void stop()
    {

        if (btConnectThread != null)
        {
            btConnectThread.cancel();
            btConnectThread = null;
        }

        if (btConnectedThread != null)
        {
            btConnectedThread.cancel();
            btConnectedThread = null;
        }

        if (btSecureAcceptThread != null)
        {
            btSecureAcceptThread.cancel();
            btSecureAcceptThread = null;
        }
        setState(STATE_NONE);
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out)
    {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (btState != STATE_CONNECTED) return;
            r = btConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed()
    {
        // Send a failure message back to the Activity
        Message msg = btHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Unable to connect device");
        msg.setData(bundle);
        btHandler.sendMessage(msg);

        // Start the service over to restart listening mode
        MultiplayerGameService.this.start();
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost()
    {
        // Send a failure message back to the Activity
        Message msg = btHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Device connection was lost");
        msg.setData(bundle);
        btHandler.sendMessage(msg);

        // Start the service over to restart listening mode
        MultiplayerGameService.this.start();
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread
    {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread()
        {
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try
            {
                tmp = btAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID_SECURE);
            }
            catch (IOException e) {}
            mmServerSocket = tmp;
        }

        public void run()
        {
            setName("AcceptThread");

            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (btState != STATE_CONNECTED)
            {
                try
                {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                }
                catch (IOException e)
                {
                    break;
                }

                // If a connection was accepted
                if (socket != null)
                {
                    synchronized (MultiplayerGameService.this)
                    {
                        switch (btState)
                        {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                connected(socket, socket.getRemoteDevice());
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate new socket.
                                try
                                {
                                    socket.close();
                                }
                                catch (IOException e) {}
                                break;
                        }
                    }
                }
            }
        }

        public void cancel()
        {
            try
            {
                mmServerSocket.close();
            }
            catch (IOException e){}
        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread
    {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device)
        {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try
            {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
            }
            catch (IOException e){}
            mmSocket = tmp;
        }

        public void run()
        {
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            btAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try
            {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            }
            catch (IOException e)
            {
                // Close the socket
                try
                {
                    mmSocket.close();
                }
                catch (IOException e2) {}
                connectionFailed();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (MultiplayerGameService.this)
            {
                btConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel()
        {
            try
            {
                mmSocket.close();
            }
            catch (IOException e) {}
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread
    {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket)
        {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try
            {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            }
            catch (IOException e) {}

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run()
        {
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true)
            {
                try
                {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    // Send the obtained bytes to the UI Activity
                    btHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                }
                catch (IOException e)
                {
                    connectionLost();
                    // Start the service over to restart listening mode
                    MultiplayerGameService.this.start();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer)
        {
            try
            {
                mmOutStream.write(buffer);
            }
            catch (IOException e) {}
        }

        public void cancel()
        {
            try
            {
                mmSocket.close();
            }
            catch (IOException e) {}
        }
    }
}
