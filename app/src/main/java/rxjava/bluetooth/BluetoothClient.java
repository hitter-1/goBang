package rxjava.bluetooth;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import rxjava.bluetooth.bean.BLEDevice;
import rxjava.bluetooth.callback.BaseResultCallback;
import rxjava.bluetooth.exception.BluetoothSearchConflictException;

/**
 * Created by zhongyu on 1/16/2018.
 */

public class BluetoothClient {

    BluetoothAdapter mBluetoothAdapter;
    private static volatile BluetoothClient proxyClient;
    public PublishSubject<BluetoothDevice> publishSubject;
    private Context mContext;
    private boolean Discoverable = false;

    /**
     *
     * @param context
     * @param discoverable 是查找其他设备还是被别的设备找到
     */
    private BluetoothClient(Context context, boolean discoverable) {
        publishSubject = PublishSubject.create();
        mContext = context;
        initBlueTooth();
        if(discoverable) {
            setDiscoverable(context);
        }else {
            startDiscovery();
        }
    }


    private boolean initBlueTooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter != null) {
            if(!mBluetoothAdapter.isEnabled()) {
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


    private void setDiscoverable(Context mContext) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        mContext.startActivity(discoverableIntent);
        startBlueToothService();
    }

    private void startBlueToothService() {
        //如何本地蓝牙在搜索其他设备就取消搜索.专心做服务蓝牙设备
        if(mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }


    public static BluetoothClient get(Context context, boolean discoverable) {
        if(proxyClient == null) {
            synchronized (BluetoothClient.class) {
                if(proxyClient == null) {
                    proxyClient = new BluetoothClient(context, discoverable);
                }
            }
        }
        return proxyClient;
    }


    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //发现蓝牙设备
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                //从Intent中获取设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                publishSubject.onNext(device);
            }else if(mBluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                publishSubject.onComplete();
            }
        }
    };




}
