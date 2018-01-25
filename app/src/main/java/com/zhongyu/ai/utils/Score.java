package com.zhongyu.ai.utils;

/**
 * Created by zhongyu on 1/25/2018.
 */

public class Score {
    /**
     * 棋型表示
     * 用一个6位数表示棋型，从高位到低位分别表示
     * 连五，活四，眠四，活三，活二/眠三，活一/眠二, 眠一
     */
    public static final int ONE = 10;
    public static final int TWO = 100;
    public static final int THREE = 1000;
    public static final int FOUR = 100000;
    public static final int FIVE = 1000000;
    public static final int BLOCKED_ONE = 1;
    public static final int BLOCKED_TWO = 10;
    public static final int BLOCKED_THREE = 100;
    public static final int BLOCKED_FOUR = 10000;
}
