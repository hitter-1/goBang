package com.zhongyu.ai.rxjava.bluetooth.Thread;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.google.common.base.Charsets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import io.reactivex.subjects.PublishSubject;
import com.zhongyu.ai.utils.GsonUtils;

/**
 * Created by zhongyu on 1/20/2018.
 */

public class DataTransferThread extends Thread {
    private static final String TAG = "DataTransferThread";
    private BluetoothSocket mSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    public PublishSubject<String> messaageSubject;


    public DataTransferThread(BluetoothSocket socket) {
        mSocket = socket;
        messaageSubject = PublishSubject.create();
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = mSocket.getInputStream();
            tmpOut = mSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mInputStream = tmpIn;
        mOutputStream = tmpOut;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;
        while (true) {
            try {
                bytes = mInputStream.read(buffer);
                byte[] tmp = Arrays.copyOf(buffer, bytes);
                final String data = new String(tmp, Charsets.UTF_8);
                Log.i(TAG, "res:" + data);
                if (!data.isEmpty()) {
                    messaageSubject.onNext(data);
//                    if (mContext instanceof Activity) {
//                        ((Activity) mContext).runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mDataListener.onDataReceived(data);
//                            }
//                        });
//                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void sendData(Object data) {
        try {
            String strData = GsonUtils.getInstance().toJson(data);
//            String strData = LoganSquare.serialize(data);
            byte[] buffer;
            buffer = strData.getBytes(Charsets.UTF_8);
            mOutputStream.write(buffer);
            Log.i(TAG, strData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
