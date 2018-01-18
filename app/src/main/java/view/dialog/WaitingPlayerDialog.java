package view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.zhongyu.gobang_ai.R;
import com.gc.materialdesign.views.ButtonRectangle;

/**
 * Created by zhongyu on 1/18/2018.
 */

public class WaitingPlayerDialog extends BaseDialog {
    public static final String TAG = "WaitingPlayerDialog";
    private ButtonRectangle mBeginButton;

    public static final String WAIT_CANCEL = "waitcancel";
    public static final String WAIT_BEGAN = "waitbegan";

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
                publishClickSubject.onNext(WAIT_CANCEL);
            }
        });

        return view;
    }

    public void setBeginEnable(){
        mBeginButton.setEnabled(true);
        mBeginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishClickSubject.onNext(WAIT_BEGAN);
            }
        });
    }
}
