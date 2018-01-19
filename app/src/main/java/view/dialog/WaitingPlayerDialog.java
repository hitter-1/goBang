package view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.zhongyu.gobang_ai.R;
import com.gc.materialdesign.views.ButtonRectangle;

import event.StringEvent;
import utils.Constants;

/**
 * Created by zhongyu on 1/18/2018.
 */

public class WaitingPlayerDialog extends BaseDialog {
    public static final String TAG = "WaitingPlayerDialog";
    private ButtonRectangle mBeginButton;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.dialog_waiting_player, container, false);
        mBeginButton = (ButtonRectangle)view.findViewById(R.id.btn_begin);
        mBeginButton.setEnabled(false);
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishClickSubject.onNext(new StringEvent(Constants.WAIT_CANCEL));
            }
        });

        return view;
    }

    public void setBeginEnable(){
        mBeginButton.setEnabled(true);
        mBeginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishClickSubject.onNext(new StringEvent(Constants.WAIT_BEGAN));
            }
        });
    }
}
