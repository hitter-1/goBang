package com.zhongyu.ai.utils;

import com.zhongyu.ai.bean.Point;
import com.zhongyu.ai.view.GoBangBoard;

/**
 * Created by zhongyu on 1/26/2018.
 */

public class Neighbor {

    public static boolean hasNeighbor(GoBangBoard goBangBoard, Point point, int distance, int count) {
        int len = goBangBoard.LINE_COUNT;
        int startX = point.x-distance;
        int endX = point.x+distance;
        int startY = point.y-distance;
        int endY = point.y+distance;
        for(int i=startX;i<=endX;i++) {
            if(i<0||i>=len) continue;
            for(int j=startY;j<=endY;j++) {
                if(j<0||j>=len) continue;
                if(i==point.x && j==point.y) continue;
                if(goBangBoard.getRole(i, j) != Constants.CHESS_NONE) {
                    count--;
                    if(count <= 0) return true;
                }
            }
        }
        return false;
    }
}
