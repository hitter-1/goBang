package view.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.zhongyu.gobang_ai.R;

import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import rxjava.bluetooth.BluetoothClient;
import utils.Constants;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnWifi(View view) {
        GameActivity.startActivity(this, Constants.WIFI_MODE);
    }

    public void btnBluetooth(View view) {
        GameActivity.startActivity(this, Constants.BLUE_TOOTH_MODE);
    }

    public void btnAI(View view) {
        GameActivity.startActivity(this, Constants.AI_MODE);
    }

    public void btnDoubleAgain(View view) {
        GameActivity.startActivity(this, Constants.COUPE_MODE);
    }





}
