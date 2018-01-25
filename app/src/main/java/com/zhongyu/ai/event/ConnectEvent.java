package com.zhongyu.ai.event;

import android.bluetooth.BluetoothDevice;

import com.zhongyu.ai.salut.SalutDevice;

/**
 * Created by zhongyu on 1/19/2018.
 */

public class ConnectEvent implements Event {

    private SalutDevice mSalutDevice;
    private BluetoothDevice mBlueToothDevice;

    public ConnectEvent(SalutDevice mSalutDevice, BluetoothDevice mBlueToothDevice) {
        this.mSalutDevice = mSalutDevice;
        this.mBlueToothDevice = mBlueToothDevice;
    }

    public SalutDevice getmSalutDevice() {
        return mSalutDevice;
    }

    public void setmSalutDevice(SalutDevice mSalutDevice) {
        this.mSalutDevice = mSalutDevice;
    }

    public BluetoothDevice getmBlueToothDevice() {
        return mBlueToothDevice;
    }

    public void setmBlueToothDevice(BluetoothDevice mBlueToothDevice) {
        this.mBlueToothDevice = mBlueToothDevice;
    }
}
