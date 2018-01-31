package com.zhongyu.ai.utils;

/**
 * Created by zhongyu on 1/26/2018.
 */

public class Math {
    double threshold = 1.1;

    boolean greatThan(int a, int b) {
        return a >= b * threshold;
    }
    boolean greatOrEqualThan(int a, int b) {
        return a * threshold >= b;
    }
    boolean littleThan(int a, int b) {
        return a * threshold <= b;
    }
    boolean littleOrEqualThan(int a, int b) {
        return a <= b * threshold;
    }
    boolean equal(int a, int b) {
        return (a * threshold >= b) && (a <= b * threshold);
    }
}
