package view.dialog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by zhongyu on 1/17/2018.
 */

public class DialogCenter {
    private FragmentManager mFragmentManager;

    private CompositionDialog mCompositionDialog;

    public DialogCenter(AppCompatActivity appCompatActivity) {
        mFragmentManager = appCompatActivity.getSupportFragmentManager();
        mCompositionDialog = new CompositionDialog();
        mCompositionDialog.setCancelable(false);
    }
    public CompositionDialog showCompositionDialog() {
        mCompositionDialog.show(preShowDialog(CompositionDialog.TAG), CompositionDialog.TAG);
        return mCompositionDialog;
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
