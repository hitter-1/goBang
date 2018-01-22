package rxjava.bluetooth.Thread;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

import event.Event;
import io.reactivex.subjects.PublishSubject;
import utils.Constants;

/**
 * Created by zhongyu on 1/19/2018.
 */

public class ConnectThread extends Thread {
    private static final String TAG = "ConnectThread";

    public PublishSubject<BluetoothSocket> publishClickSubject = PublishSubject.create();

    private BluetoothSocket mmSocket;

    public BluetoothSocket getMmSocket() {
        return mmSocket;
    }

    public ConnectThread(BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;

        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            tmp = device.createRfcommSocketToServiceRecord(Constants.MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
//        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            return;
        }
        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
//        manageMyConnectedSocket(mmSocket);
        publishClickSubject.onNext(mmSocket);
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}
