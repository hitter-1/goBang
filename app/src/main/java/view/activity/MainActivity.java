package view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.zhongyu.gobang_ai.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnWifi(View view) {
        GameActivity.startActivity(this, GameActivity.GAME_WIFI);
    }

    public void btnBluetooth(View view) {
        GameActivity.startActivity(this, GameActivity.GAME_BLUETOOTH);
    }

    public void btnAI(View view) {
        GameActivity.startActivity(this, GameActivity.GAME_AI);
    }

    public void btnDoubleAgain(View view) {
        GameActivity.startActivity(this, GameActivity.GAME_DOUBLE_AGAIN);
    }


}
