package com.zhongyu.ai.view.dialog;

import android.bluetooth.BluetoothDevice;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by zhongyu on 1/17/2018.
 */

public class DialogCenter {
    private FragmentManager mFragmentManager;

    private CompositionDialog mCompositionDialog;
    private PeersDialog mPeersDialog;
    private WaitingPlayerDialog mWaitingDialog;
    private RestartWaitingDialog mRestartWaitingDialog;
    private RestartAckDialog mRestartAckDialog;
    private ExitAckDialog mExitAckDialog;
    private MoveBackAckDialog mMoveBackAckDialog;
    private MoveBackWaitingDialog mMoveBackWaitingDialog;

    public DialogCenter(AppCompatActivity appCompatActivity) {
        mFragmentManager = appCompatActivity.getSupportFragmentManager();
        mCompositionDialog = new CompositionDialog();
        mCompositionDialog.setCancelable(false);
        mPeersDialog = new PeersDialog();
        mPeersDialog.setCancelable(false);
        mWaitingDialog = new WaitingPlayerDialog();
        mWaitingDialog.setCancelable(false);
        mRestartWaitingDialog = new RestartWaitingDialog();
        mRestartAckDialog = new RestartAckDialog();
        mRestartAckDialog.setCancelable(false);
        mExitAckDialog = new ExitAckDialog();
        mExitAckDialog.setCancelable(false);
        mMoveBackAckDialog = new MoveBackAckDialog();
        mMoveBackAckDialog.setCancelable(false);
        mMoveBackWaitingDialog = new MoveBackWaitingDialog();
    }
    public CompositionDialog showCompositionDialog() {
        mCompositionDialog.show(preShowDialog(CompositionDialog.TAG), CompositionDialog.TAG);
        return mCompositionDialog;
    }

    public void dismissWaitingAndComposition() {
        mWaitingDialog.dismiss();
        mCompositionDialog.dismiss();
    }

    public void dismissPeersAndComposition() {
        mPeersDialog.dismiss();
        mCompositionDialog.dismiss();
    }

    public WaitingPlayerDialog showWaitintPlayerDialog() {
        mWaitingDialog.show(preShowDialog(WaitingPlayerDialog.TAG), WaitingPlayerDialog.TAG);
        return mWaitingDialog;
    }

    public PeersDialog showPeersDialog() {
        mPeersDialog.show(preShowDialog(PeersDialog.TAG), PeersDialog.TAG);
        return mPeersDialog;
    }

    public void updateBlueToothPeers(List<BluetoothDevice> data, boolean append) {
        mPeersDialog.updateBlueToothPeers(data, append);
    }

    public void enableWaitingPlayerDialogsBegin() {
        mWaitingDialog.setBeginEnable();
    }

    public void dismissWaitingPlayerDialog() {
        mWaitingDialog.dismiss();
    }

    public void dismissPeersDialog() {
        mPeersDialog.dismiss();
    }


    private FragmentTransaction preShowDialog(String tag) {
        FragmentTransaction fr = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fr.remove(fragment);
        }
        fr.addToBackStack(null);

        return fr;
    }
}
