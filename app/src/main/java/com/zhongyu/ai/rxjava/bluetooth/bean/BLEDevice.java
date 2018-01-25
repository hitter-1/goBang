package com.zhongyu.ai.rxjava.bluetooth.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 *存储蓝牙设备信息
 * Created by zhongyu on 1/16/2018.
 */

public class BLEDevice implements Parcelable {

    private String deviceName;

    private String mac;

    private int rssi;


    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public BLEDevice() {
    }

    protected BLEDevice(Parcel in) {
        int[] intArr = new int[1];
        in.readIntArray(intArr);
        setRssi(intArr[0]);

        String[] strArr = new String[3];
        in.readStringArray(strArr);
        setDeviceName(strArr[0]);
        setMac(strArr[1]);
    }



    public static final Creator<BLEDevice> CREATOR = new Creator<BLEDevice>() {
        @Override
        public BLEDevice createFromParcel(Parcel in) {
            return new BLEDevice(in);
        }

        @Override
        public BLEDevice[] newArray(int size) {
            return new BLEDevice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        int[] intArr = new int[1];
        parcel.writeIntArray(intArr);
        setRssi(intArr[0]);

        String[] strArr = new String[3];
        parcel.writeStringArray(strArr);
        setDeviceName(strArr[0]);
        setMac(strArr[1]);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        BLEDevice device = (BLEDevice) obj;
        return mac.equals(device.mac);
    }

    @Override
    public int hashCode() {
        return mac.hashCode();
    }

    @Override
    public String toString() {
        return "BLEDevice{" +
                "deviceName='" + deviceName + '\'' +
                ", mac='" + mac + '\'' +
                ", rssi=" + rssi +
                '}';
    }
}
