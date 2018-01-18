package view.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.zhongyu.gobang_ai.R;

import io.reactivex.functions.Consumer;
import rxjava.bluetooth.BluetoothClient;
import view.GoBangBoard;
import view.dialog.CompositionDialog;
import view.dialog.DialogCenter;

/**
 * Created by zhongyu on 1/12/2018.
 */

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";
    
    public static final String GAME_MODE = "gamemode";

    public static final String GAME_WIFI = "gamewifi";
    public static final String GAME_BLUETOOTH = "gamebluetooth";
    public static final String GAME_AI = "gameai";
    public static final String GAME_DOUBLE_AGAIN = "gamedoubleagain";

    private String gameMode = null;

    private GoBangBoard goBangBoard;
    private DialogCenter mDialogCenter;

    private boolean mIsHost;//主机
    private boolean mIsMePlay = false;


    public static void startActivity(Context context, String mode) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(GAME_MODE, mode);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initData();
        initView();
    }

    private void initView() {
        goBangBoard = (GoBangBoard) findViewById(R.id.go_bang_board);
        mDialogCenter = new DialogCenter(this);
        goBangBoard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                goBangBoard.putChess(true, 10 , 10);
                return false;
            }
        });
        //使用Rxjava取代回调
        goBangBoard.putChessSubjuct.subscribe(new Consumer<GoBangBoard.PutEvent>() {
            @Override
            public void accept(GoBangBoard.PutEvent putEvent) throws Exception {
                Log.d(TAG, "accept() returned: " );
            }
        });

        mDialogCenter.showCompositionDialog().publishClickSubject.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if(s.endsWith(CompositionDialog.CREAT_GAME)) {
                    onCreateGame();
                }else if(s.endsWith(CompositionDialog.JOIN_GAME)) {
                    joinGame();
                }else if(s.endsWith(CompositionDialog.BTN_CANCEL)) {
                    quitGame();
                }
            }
        });
    }

    private void onCreateGame() {
        mIsHost = true;
        mDialogCenter.showWaitintPlayerDialog();
    }

    private void joinGame() {

    }

    private void quitGame() {

    }


    //扫描蓝牙
    private void bluetoothScan() {
        BluetoothClient.get(this).publishSubject.subscribe(new Consumer<BluetoothDevice>() {
            @Override
            public void accept(BluetoothDevice bluetoothDevice) throws Exception {

            }
        });
    }


    private void initData() {
        gameMode = getIntent().getStringExtra(GAME_MODE);
        if(gameMode.equals(GAME_MODE)) {
            bluetoothScan();
        }
        setTitle(gameMode);
    }

    public void whiteFirst(View view) {

    }

    public void startGame(View view) {

    }

    public void endGame(View view) {

    }
}
