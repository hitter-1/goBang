package com.zhongyu.ai.utils;

import java.util.UUID;

/**
 * Created by zhongyu on 1/14/2018.
 */

public class Constants {

    public static final String APP_NAME = "gobang";

    public static final String CON_TAG = "game_connection";
    public static final String GAME_MODE = "game_mode";

    public static final String YOU_WIN = "你赢了";
    public static final String YOU_LOSE = "你输了";

    public static final int SCORE_MAX = Score.FIVE * 10;
    public static final int SCORE_MIN = -1 * SCORE_MAX;

    public static final String INVALID_MODE = "invalidmode";
    public static final String COUPE_MODE = "coupemode";
    public static final String WIFI_MODE = "wifimode";
    public static final String BLUE_TOOTH_MODE = "bluetoothemode";
    public static final String AI_MODE = "aimode";

    public static final int CHESS_NONE = 0;
    public static final int CHESS_WHITE = 1;
    public static final int CHESS_BLACK = 2;


    public static final int MY = 1;//
    public static final int AI = 2;//默认电脑黑色棋子

    public static int reverse(int role) {
        return role == 1 ? 2 : 1;
    }

    public static final UUID MY_UUID = UUID.fromString("b2a770c2-529e-4e80-933b-99dc372d3e65");

    public static final String BTN_WIFI_BLUE_CANCEL = "btnwifibluecancel";
    public static final String WAIT_CANCEL = "waitcancel";
    public static final String WAIT_BEGAN = "waitbegan";

}
