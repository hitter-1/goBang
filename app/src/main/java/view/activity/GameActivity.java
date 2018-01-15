package view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.zhongyu.gobang_ai.R;

import io.reactivex.functions.Consumer;
import view.GoBangBoard;

/**
 * Created by zhongyu on 1/12/2018.
 */

public class GameActivity extends AppCompatActivity {
    public static final String GAME_MODE = "gamemode";

    public static final String GAME_WIFI = "gamewifi";
    public static final String GAME_BLUETOOTH = "gamebluetooth";
    public static final String GAME_AI = "gameai";
    public static final String GAME_DOUBLE_AGAIN = "gamedoubleagain";

    private String gameMode = null;

    private GoBangBoard goBangBoard;


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
        goBangBoard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                goBangBoard.putChess(true, 10 , 10);
                return false;
            }
        });
        goBangBoard.putChessEvent.subscribe(new Consumer<GoBangBoard.PutEvent>() {
            @Override
            public void accept(GoBangBoard.PutEvent putEvent) throws Exception {
            }
        });
    }

    private void initData() {
        gameMode = getIntent().getStringExtra(GAME_MODE);
        setTitle(gameMode);
    }

    public void whiteFirst(View view) {

    }

    public void startGame(View view) {

    }

    public void endGame(View view) {

    }
}
