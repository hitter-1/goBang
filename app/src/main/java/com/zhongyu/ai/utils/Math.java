package com.zhongyu.ai.utils;

/**
 * Created by zhongyu on 1/26/2018.
 */

public class Math {
    static double threshold = 1.1;

    static boolean greatThan(double a, double b) {
        return a >= b * threshold;
    }
    static boolean greatOrEqualThan(double a, double b) {
        return a * threshold >= b;
    }
    static boolean littleThan(double a, int b) {
        return a * threshold <= b;
    }
    static boolean littleOrEqualThan(int a, int b) {
        return a <= b * threshold;
    }
    static boolean equal(double a, double b) {
        return (a * threshold >= b) && (a <= b * threshold);
    }
}
