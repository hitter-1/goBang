package com.zhongyu.ai.interator;

import android.content.Context;

/**
 * Created by zhongyu on 1/18/2018.
 */

public class BlueToothInteractor extends NetInteractor {

    private static final String TAG = "BlueToothInteractor";

    private Context mContext;

    public BlueToothInteractor(Context mContext) {
        this.mContext = mContext;
    }
}
