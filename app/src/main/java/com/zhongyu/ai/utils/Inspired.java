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

    List<Point> inspired(GoBangBoard goBangBoard) {
        List<Point> fives = new ArrayList();
        List<Point> fours = new ArrayList();
        List<Point> blockedfours = new ArrayList();
        List<Point> twothrees = new ArrayList();
        List<Point> threes = new ArrayList();
        List<Point> twos = new ArrayList();
        List<Point> neighbors = new ArrayList();
        List<Point> result = new ArrayList<>();

        for (int i = 0; i < goBangBoard.LINE_COUNT; i++) {
            for(int j = 0; j < goBangBoard.LINE_COUNT; j++) {
                if(goBangBoard.getRole(i, j) == Constants.CHESS_NONE) {
                    if(Neighbor.hasNeighbor(goBangBoard, new Point(i, j), 2, 2)) {
                        int scoreMy = goBangBoard.getMyScore()[i][j];
                        int scoreAi = goBangBoard.getAiScore()[i][j];

                        Point point = new Point(i, j);

                        if(scoreMy >= Score.FIVE) {//先看电脑能不能连成5
                            result.add(point);
                            return result;
                        } else if(scoreAi >= Score.FIVE) {//再看玩家能不能连成5
                            //别急着返回，因为遍历还没完成，说不定电脑自己能成五。
                            fives.add(point);
                        } else if(scoreMy >= Score.FOUR) {
                            fours.add(0, point);
                        } else if(scoreAi >= Score.FOUR) {
                            fours.add(point);
                        } else if(scoreMy >= Score.BLOCKED_FOUR) {
                            blockedfours.add(0, point);
                        } else if(scoreAi >= Score.BLOCKED_FOUR) {
                            blockedfours.add(point);
                        } else if(scoreMy >= 2*Score.THREE) {
                            //能成双三也行
                            twothrees.add(0, point);
                        } else if(scoreAi >= 2*Score.THREE) {
                            twothrees.add(point);
                        } else if(scoreMy >= Score.THREE) {
                            threes.add(0, point);
                        } else if(scoreAi >= Score.THREE) {
                            threes.add(point);
                        } else if(scoreMy >= Score.TWO) {
                            twos.add(0, point);
                        } else if(scoreAi >= Score.TWO) {
                            twos.add(point);
                        } else {
                             neighbors.add(point);
                        }
                    }
                }
            }
        }

        if(fives.size() > 0) {
            result.add(fives.get(0));
            return result;
        }
        if(fours.size() > 0) return fours;
        if(blockedfours.size() > 0) {
            result.add(blockedfours.get(0));
            return result;
        }
        if(twothrees.size() > 0) {
            twothrees.addAll(threes);
            return twothrees;
        }
        twos.addAll(neighbors);
        threes.addAll(twos);
        if(threes.size() > Config.countLimit) {
            for (int i = 0; i < Config.countLimit; i++) {
                result.add(threes.get(i));
            }
        }
        return result;
    }
}
