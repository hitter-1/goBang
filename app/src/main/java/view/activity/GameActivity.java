package view.activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.example.zhongyu.gobang_ai.R;
import com.google.gson.Gson;

import java.util.List;

import bean.Message;
import bean.Point;
import event.ConnectEvent;
import event.Event;
import event.StringEvent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rxjava.bluetooth.BluetoothClient;
import rxjava.bluetooth.Thread.ConnectThread;
import rxjava.bluetooth.Thread.DataTransferThread;
import utils.Constants;
import utils.GameJudger;
import utils.MessageWrapper;
import utils.OperationQueue;
import utils.ToastUtil;
import view.GoBangBoard;
import view.dialog.BaseDialog;
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

    private boolean mCanClickConnect = true;//看是否可以点击连接别的蓝牙


    private boolean mIsHost;//主机
    private boolean mIsMePlay = false;
    private boolean mIsGameEnd = false;
    DataTransferThread dataTransferThread;

    private OperationQueue mOperationQueue;


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
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!mIsGameEnd && mIsMePlay) {
                            float x = motionEvent.getX();
                            float y = motionEvent.getY();
                            Point point = goBangBoard.convertPoint(x, y);
                            if (goBangBoard.putChess(mIsHost, point.x, point.y)) {
                                Message data = MessageWrapper.getSendDataMessage(point, mIsHost);
                                sendMessage(data);
                                mIsMePlay = false;
                            }
                        }
                        break;
                }
                return false;
            }
        });
        //使用Rxjava取代回调
        goBangBoard.putChessSubjuct.subscribe(new Consumer<GoBangBoard.PutEvent>() {
            @Override
            public void accept(GoBangBoard.PutEvent putEvent) throws Exception {
                onPutChess(putEvent.getmBoard(), putEvent.getX(), putEvent.getY());
            }
        });



        mDialogCenter.showCompositionDialog().publishClickSubject.subscribe(new Consumer<Event>() {
            @Override
            public void accept(Event event) throws Exception {
                if(event instanceof StringEvent) {
                    strEventDeal(((StringEvent) event).getStrName());
                }else if(event instanceof ConnectEvent) {
                    connectEventDeal((ConnectEvent) event);
                }
            }
        });
    }

    private void strEventDeal(String s) {
        if(s.endsWith(CompositionDialog.CREAT_GAME)) {
            onCreateGame();
        }else if(s.endsWith(CompositionDialog.JOIN_GAME)) {
            joinGame();
        }else if(s.endsWith(CompositionDialog.BTN_CANCEL)) {
            quitGame();
        }else if(s.endsWith(Constants.WAIT_BEGAN)) {
            if(mIsHost) {
                mDialogCenter.dismissWaitingAndComposition();
                // TODO: 1/22/2018 发送消息到对方
                Message begin = MessageWrapper.getHostBeginMessage();
                sendMessage(begin);
            }
        }
    }


    private void sendMessage(Message message) {
        dataTransferThread.sendData(message);
    }


    public void onPutChess(int[][] board, int x, int y) {
        if (mIsMePlay && GameJudger.isGameEnd(board, x, y)) {
            ToastUtil.showShort(this, Constants.YOU_WIN);
            Message end = MessageWrapper.getGameEndMessage(Constants.YOU_LOSE);
            sendMessage(end);
            mIsMePlay = false;
            mIsGameEnd = true;
        }
        Point point = new Point();
        point.setXY(x, y);
        mOperationQueue.addOperation(point);
    }

    private void connectEventDeal(ConnectEvent connectEvent) {
        if(mCanClickConnect) {
            mCanClickConnect = false;
            if(connectEvent.getmBlueToothDevice() != null) {
                blueConnect(connectEvent.getmBlueToothDevice());
            }else {
                salutConnect();
            }
        }
    }

    private void blueConnect(BluetoothDevice device) {
        ConnectThread connectThread = new ConnectThread(device);
        connectThread.start();
        connectThread.publishClickSubject.subscribe(new Consumer<BluetoothSocket>() {
            @Override
            public void accept(BluetoothSocket bluetoothSocket) throws Exception {
                Toast.makeText(GameActivity.this, "success", Toast.LENGTH_SHORT).show();
                dataTransferThread(bluetoothSocket);
            }
        });
    }

    private void dataTransferThread(BluetoothSocket bluetoothSocket) {
        dataTransferThread = new DataTransferThread(bluetoothSocket);
        dataTransferThread.start();
        dataTransferThread.messaageSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Message message = new Gson().fromJson(s, Message.class);
                        messageDeal(message);
                    }
                });
    }

    private void messageDeal(Message message) {
        int type = message.mMessageType;
        switch (type) {
            case Message.MSG_TYPE_HOST_BEGIN:
                mDialogCenter.dismissPeersAndComposition();
                Message ack = MessageWrapper.getHostBeginAckMessage();
                sendMessage(ack);
                ToastUtil.showShort(this, "游戏开始");
                mCanClickConnect = true;
                break;
            case Message.MSG_TYPE_BEGIN_ACK:
                mDialogCenter.dismissWaitingAndComposition();
                mIsMePlay = true;
                break;
            case Message.MSG_TYPE_GAME_DATA:
                goBangBoard.putChess(message.mIsWhite, message.mGameData.x, message.mGameData.y);
                mIsMePlay = true;
                break;
        }
    }

    private void salutConnect() {

    }


    private void onCreateGame() {
        mIsHost = true;
        mDialogCenter.showWaitintPlayerDialog();
        bluetoothScan(true);
    }

    private void joinGame() {
        mIsHost = false;
        mDialogCenter.showPeersDialog();
        bluetoothScan(false);
    }

    private void quitGame() {

    }

    /**
     * 蓝牙
     * @param discoverable ture 扫描或被发现
     */
    private void bluetoothScan(boolean discoverable) {
        BluetoothClient bluetoothClient = BluetoothClient.get(this, discoverable);
        bluetoothClient.publishSubject.subscribe(new Consumer<List<BluetoothDevice>>() {
            @Override
            public void accept(List<BluetoothDevice> bluetoothDeviceList) throws Exception {
                mDialogCenter.updateBlueToothPeers(bluetoothDeviceList, false);
            }
        });
        bluetoothClient.publishClickSubject.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BluetoothSocket>() {
            @Override
            public void accept(BluetoothSocket bluetoothSocket) throws Exception {
                onBlueToothDeviceConnected();
                dataTransferThread(bluetoothSocket);
            }
        });
    }

    /**
     * 更新蓝牙扫描
     * @param bluetoothDevice
     */
    private void peerDialogBluUpdate(List<BluetoothDevice> bluetoothDeviceList ,BluetoothDevice bluetoothDevice) {
        bluetoothDeviceList.add(bluetoothDevice);
        mDialogCenter.updateBlueToothPeers(bluetoothDeviceList, true);
    }


    private void initData() {
        gameMode = getIntent().getStringExtra(GAME_MODE);
        if(gameMode.endsWith(Constants.BLUE_TOOTH_MODE)) {
        }else if(gameMode.endsWith(Constants.WIFI_MODE)) {
        }
        setTitle(gameMode);
        mOperationQueue = new OperationQueue();
    }

    private void onBlueToothDeviceConnected() {
        ToastUtil.showShort(this, "蓝牙连接成功");
        if(mIsHost) {
            mDialogCenter.enableWaitingPlayerDialogsBegin();
        }
    }



    public void whiteFirst(View view) {

    }

    public void startGame(View view) {

    }

    public void endGame(View view) {

    }
}
