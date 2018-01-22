package rxjava.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.reactivex.subjects.PublishSubject;
import utils.Constants;

/**
 * Created by zhongyu on 1/16/2018.
 */

public class BluetoothClient {

    BluetoothAdapter mBluetoothAdapter;
    private static volatile BluetoothClient proxyClient;
    public PublishSubject<List<BluetoothDevice>> publishSubject;
    public PublishSubject<BluetoothSocket> publishClickSubject = PublishSubject.create();
    private Context mContext;
    private List<BluetoothDevice> bluetoothDeviceList;
    /**
     * @param context
     * @param discoverable 是查找其他设备还是被别的设备找到
     */
    private BluetoothClient(Context context, boolean discoverable) {
        publishSubject = PublishSubject.create();
        mContext = context;
        bluetoothDeviceList = new ArrayList<>();
        initBlueTooth();
        if (discoverable) {
            setDiscoverable(context);
        } else {
            addPairedDevices();//添加已经配对的设备
            startDiscovery();//扫描新的设备
        }
    }

    private boolean initBlueTooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
        }
        return mBluetoothAdapter != null;
    }

    private void startDiscovery() {
        mBluetoothAdapter.startDiscovery();
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mContext.registerReceiver(mBroadcastReceiver, filter);
    }

    private void addPairedDevices() {
        publishSubject.onNext(getPairedDevices());
    }

    private void setDiscoverable(Context mContext) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        mContext.startActivity(discoverableIntent);
        startBlueToothService();
    }

    private void startBlueToothService() {
        //如何本地蓝牙在搜索其他设备就取消搜索.专心做服务蓝牙设备
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        AcceptThread acceptThread = new AcceptThread(mBluetoothAdapter);
        acceptThread.start();
    }

    public static BluetoothClient get(Context context, boolean discoverable) {
        if (proxyClient == null) {
            synchronized (BluetoothClient.class) {
                if (proxyClient == null) {
                    proxyClient = new BluetoothClient(context, discoverable);
                }
            }
        }
        return proxyClient;
    }

    private List<BluetoothDevice> getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        return new ArrayList<>(pairedDevices);
    }

    public void connectBlueDevice(BluetoothDevice device) {
        if(mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }


    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //发现蓝牙设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //从Intent中获取设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(!bluetoothDeviceList.contains(device)) {
                    bluetoothDeviceList.add(device);
                    publishSubject.onNext(bluetoothDeviceList);
                }
            } else if (mBluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                publishSubject.onComplete();
            }
        }
    };


    public static class ConnectThread extends Thread {
        private static final String TAG = "ConnectThread";

        private final BluetoothSocket mmSocket;

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
            if(mmSocket == null) {
                return;
            }
            // Cancel discovery because it otherwise slows down the connection.

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
//            manageMyConnectedSocket(mmSocket);
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
                publishClickSubject.onNext(socket);
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

}
