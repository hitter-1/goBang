package com.zhongyu.ai.rxjava.bluetooth.Thread;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;

import com.zhongyu.ai.utils.Constants;

/**
 * Created by zhongyu on 1/20/2018.
 */

public class AcceptThread extends Thread{
    private BluetoothServerSocket mServerSocket = null;

    public AcceptThread(BluetoothAdapter mAdapter) {
        BluetoothServerSocket tmp = null;
        try {
            tmp = mAdapter.listenUsingRfcommWithServiceRecord(Constants.APP_NAME, Constants.MY_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mServerSocket = tmp;
    }

    @Override
    public void run() {
        if (mServerSocket == null) {
            return;
        }
        BluetoothSocket socket = null;
        try {
            socket = mServerSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (socket != null) {
            // TODO: 1/20/2018
//            manageConnectedSocket(socket);
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancel() {
        if (mServerSocket != null) {
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
