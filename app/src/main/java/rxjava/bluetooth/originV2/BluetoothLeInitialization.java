package rxjava.bluetooth.originV2;

import android.content.Context;
import android.os.Handler;

/**
 * 蓝牙设备管理类
 *
 * Created by zhongyu on 1/16/2018.
 */

public class BluetoothLeInitialization {

    private static final String TAG = "BluetoothLe";

    private static volatile BluetoothLeInitialization mInstance;

    private final Context mContext;

    private static Handler mBluetoothWorker;

//    private


    public BluetoothLeInitialization(Context mContext) {
        this.mContext = mContext;
    }
}
