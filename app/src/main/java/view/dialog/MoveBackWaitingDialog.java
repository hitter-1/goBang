package view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhongyu.gobang_ai.R;


/**
 * Created by Administrator on 2016/1/27.
 */
public class MoveBackWaitingDialog extends BaseDialog {

    public static final String TAG = "MoveBackWaitingDialog";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.dialog_waiting_move_back, container, false);
    }
}
