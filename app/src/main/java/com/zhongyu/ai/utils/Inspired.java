package com.zhongyu.ai.utils;

import com.zhongyu.ai.bean.Point;
import com.zhongyu.ai.view.GoBangBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongyu on 1/29/2018.
 */

public class Inspired {


    void init() {
        int evaluateCache = 0;
        int steps = 0;
        int zobrist = 0;
        int size = 0;
        
    }

    void inspired(GoBangBoard goBangBoard) {
        List fives = new ArrayList();
        List fours = new ArrayList();
        List blockedfours = new ArrayList();
        List twothrees = new ArrayList();
        List threes = new ArrayList();
        List twos = new ArrayList();
        List neighbors = new ArrayList();

        for (int i = 0; i < goBangBoard.LINE_COUNT; i++) {
            for(int j = 0; j < goBangBoard.LINE_COUNT; j++) {
                if(goBangBoard.getRole(i, j) == Constants.CHESS_NONE) {
                    if(Neighbor.hasNeighbor(goBangBoard, new Point(i, j), 2, 2)) {

                    }
                }
            }
        }
    }
}
