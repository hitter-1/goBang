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

    private BluetoothClient(Context context) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        publishSubject = PublishSubject.create();
        //判断蓝牙是否可用
        if(!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();//打开蓝牙
        }
        mBluetoothAdapter.startDiscovery();
        IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mBroadcastReceiver,filter);
    }

    public static BluetoothClient get(Context context) {
        if(proxyClient == null) {
            synchronized (BluetoothClient.class) {
                if(proxyClient == null) {
                    proxyClient = new BluetoothClient(context);
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
