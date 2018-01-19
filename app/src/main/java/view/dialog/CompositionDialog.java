package view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhongyu.gobang_ai.R;

import event.StringEvent;

/**
 * Created by zhongyu on 1/17/2018.
 */

public class CompositionDialog extends BaseDialog {
    public static final String TAG = "CompositionDialog";

    public static final String CREAT_GAME = "creatgame";
    public static final String JOIN_GAME = "joingame";
    public static final String BTN_CANCEL = "btncancel";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_composition, container, false);

        view.findViewById(R.id.btn_create_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishClickSubject.onNext(new StringEvent(CREAT_GAME));
            }
        });
        view.findViewById(R.id.btn_join_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishClickSubject.onNext(new StringEvent(JOIN_GAME));
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishClickSubject.onNext(new StringEvent(BTN_CANCEL));
            }
        });

        return view;
    }
}
