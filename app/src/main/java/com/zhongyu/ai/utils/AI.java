package com.zhongyu.ai.utils;

import android.util.Log;

import com.zhongyu.ai.bean.Point;
import com.zhongyu.ai.view.GoBangBoard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhongyu on 1/31/2018.
 */

public class AI {
    private static final String TAG = "AI";
    GoBangBoard goBangBoard;
    EvaluatePoint evaluatePoint = new EvaluatePoint();
    int MAX_SCORE = Score.THREE;
    int MIN_SCORE = Score.FOUR;
    int debugNodeCount = 0;

    int total=0, //总节点数
            steps=0,  //总步数
            count,  //每次思考的节点数
            PVcut,
            ABcut,  //AB剪枝次数
            cacheCount=0, //zobrist缓存节点数
            cacheGet=0; //zobrist缓存命中数量

    public AI(GoBangBoard goBangBoard) {
        this.goBangBoard = goBangBoard;
        init();
    }

    public void init() {
        goBangBoard.putChess(false, 7, 7);
    }

    public Point search(int deepSearch) {
        if(deepSearch == 0) {
            deepSearch = Config.searchDeep;
        }
        Point result = null;
        for (int i = 2; i <= deepSearch; i += 2) {
            result = maxmin(i, 0);
//            goBangBoard.updateScoreAll();
            if(Math.greatOrEqualThan(goBangBoard.getScore(result), Score.FOUR)) return result;
        }
        return result;
    }

    public Point maxmin(int deep, int _checkmateDeep) {
        int checkmateDeep = Config.checkmateDeep;
        double best = Constants.SCORE_MIN;
        List<Point> pointList = inspired();//Complete hear
        List<Point> bestPoints = new ArrayList<>();


        int count = 0;
        int ABcut = 0;
        int PVcut = 0;
        checkmateDeep = (_checkmateDeep == 0 ? checkmateDeep : _checkmateDeep);

        for(int i=0; i < pointList.size(); i++) {
            Point p = pointList.get(i);
            goBangBoard.putChessFalse(false, p.x, p.y);
            double v = -max(deep - 1, -Score.FIVE * 10, (int) -best, Constants.MY);

            //边缘棋子的话，要把分数打折，避免电脑总喜欢往边上走
            if(p.x<3 || p.x > 11 || p.y < 3 || p.y > 11) {
                v = 0.5 * v;
            }

            //如果跟之前的一个好，则把当前位子加入待选位子
            if(Math.equal( v, best)) {
                bestPoints.add(p);
            }
            //找到一个更好的分，就把以前存的位子全部清除
            if(Math.greatThan( v, best)) {
                best =  v;
                bestPoints.clear();
                bestPoints.add(p);
            }

            goBangBoard.remove(p);
        }
//        console.log("分数:"+best.toFixed(3)+", 待选节点:"+JSON.stringify(bestPoints));
        Point result = bestPoints.get((int) (java.lang.Math.floor(bestPoints.size()) * java.lang.Math.random()));
        goBangBoard.setScore(result, (int) best, Constants.AI);
        steps ++;
        total += count;
//        console.log('搜索节点数:'+ count+ ',AB剪枝次数:'+ABcut + ', PV剪枝次数:' + PVcut + ', 缓存命中:' + (cacheGet / cacheCount).toFixed(3) + ',' + cacheGet + '/' + cacheCount + ',算杀缓存命中:' + (debug.checkmate.cacheGet / debug.checkmate.cacheCount).toFixed(3) + ',' + debug.checkmate.cacheGet + '/'+debug.checkmate.cacheCount); //注意，减掉的节点数实际远远不止 ABcut 个，因为减掉的节点的子节点都没算进去。实际 4W个节点的时候，剪掉了大概 16W个节点
//        console.log('当前统计：总共'+ steps + '步, ' + total + '个节点, 平均每一步' + Math.round(total/steps) +'个节点');
//        console.log("================================");
        return result;
    }

    //启发函数
    List<Point> inspired() {
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

                        if(scoreAi >= Score.FIVE) {//先看电脑能不能连成5
                            result.add(point);
                            return result;
                        } else if(scoreMy >= Score.FIVE) {//再看玩家能不能连成5
                            //别急着返回，因为遍历还没完成，说不定电脑自己能成五。
                            fives.add(point);
                        } else if(scoreAi >= Score.FOUR) {
                            fours.add(0, point);
                        } else if(scoreMy >= Score.FOUR) {
                            fours.add(point);
                        } else if(scoreAi >= Score.BLOCKED_FOUR) {
                            blockedfours.add(0, point);
                        } else if(scoreMy >= Score.BLOCKED_FOUR) {
                            blockedfours.add(point);
                        } else if(scoreAi >= 2*Score.THREE) {
                            //能成双三也行
                            twothrees.add(0, point);
                        } else if(scoreMy >= 2*Score.THREE) {
                            twothrees.add(point);
                        } else if(scoreAi >= Score.THREE) {
                            threes.add(0, point);
                        } else if(scoreMy >= Score.THREE) {
                            threes.add(point);
                        } else if(scoreAi >= Score.TWO) {
                            twos.add(0, point);
                        } else if(scoreMy >= Score.TWO) {
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

    private double max(int deep, double alpha, double beta, int role) {
        Point mate = checkMateFast(goBangBoard, role, Config.checkmateDeep, 0);
        // TODO: 2/2/2018 置换表默认不用
        if(Config.cache) {
            
        }
        int v = evaluatePoint.evaluate(goBangBoard, role);
        count++;
        if(deep <= 0 || Math.greatOrEqualThan(v, Score.FIVE)) {
            return v;
        }
        double best = Constants.SCORE_MIN;
        List<Point> points = inspired();
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if(role == Constants.AI)
                goBangBoard.setBoardWhiteBlack(point, false);
            else
                goBangBoard.setBoardWhiteBlack(point, true);
            double v1 =  (- max(deep - 1, -beta, -1 * (best > alpha ? best : alpha),
                    Constants.reverse(role)) * Config.deepDecrease);
            goBangBoard.remove(point);

            if(Math.greatThan(v1, best))
                best = v1;
            if(Math.greatOrEqualThan(v1, beta)) {
                ABcut++;
                // TODO: 2/5/2018
//                cache(deep, v1);
                return v1;
            }
        }
        if((deep == 2 || deep == 3) && Math.littleThan(best, Score.THREE * 2) && Math.greatThan(best, Score.THREE * -1)) {
//            mate = checkMateFast(goBangBoard, role, Config.checkmateDeep, 0);
//            if(mate == null) {
//            }else {
//                double score = goBangBoard.getScore(mate) * java.lang.Math.pow(0.8, 2) * (role == Constants.AI ? 1 : -1);
//                return score;
//            }
        }
        return best;
    }

    private void cache(int deep, int v) {
        if(!Config.cache) return;
    }

    private Point checkMateFast(GoBangBoard goBangBoard, int role, int deep, int onlyFour) {
        if(deep == 0) deep = Config.checkmateDeep;
        if(deep <= 0) return null;

        //先计算冲四赢的
        MAX_SCORE = Score.FOUR;
        MIN_SCORE = Score.FIVE;
        Point point = deeping(goBangBoard, role, deep);
        if(point != null) {
            goBangBoard.setScore(point, role, Score.FOUR);
            return point;
        }

        if(onlyFour > 0) return null;

        MAX_SCORE = Score.THREE;
        MIN_SCORE = Score.FOUR;
        point = deeping(goBangBoard, role, deep);
        if(point != null) goBangBoard.setScore(point, role, Score.THREE * 2);
        return point;
    }

    //迭代加深
    private Point deeping(GoBangBoard goBangBoard, int role, int deep) {
        Date start = new Date();
        debugNodeCount = 0;
        List<Point> result = null;
        for (int i = 1; i <= deep; i++) {
            result = maxSearch(goBangBoard, role, i);
            if(result != null) break;//找到一个就行
        }
        long time = java.lang.Math.abs(new Date().getTime() - start.getTime());
        Log.d(TAG, "deeping() returned: " + "算杀成功("+time+"毫秒, "+ debugNodeCount + "个节点):");
        if(result != null) {
            return result.get(0);
        }else {
            return null;
        }
    }

    private List<Point> maxSearch(GoBangBoard goBangBoard, int role, int deep) {
        List<Point> pointList = new ArrayList<>();
        List<Point> result = new ArrayList<>();
        debugNodeCount++;
        if(deep <= 0) return null;
        if(Config.cache) {

        }
        List<Point> points = findMax(goBangBoard, role, MAX_SCORE);
        if(points.size() > 0 && goBangBoard.getScore(points.get(0)) > Score.FOUR) {
            pointList.add(points.get(0));
            return pointList;
        }
        if(points.size() == 0) return null;
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            goBangBoard.setBoardWhiteBlack(p, role);
            List<Point> minList = min(goBangBoard, role, deep - 1);
            goBangBoard.setBoardWhiteBlack(p, Constants.CHESS_NONE);
            if(minList != null) {
                minList.add(p);
                return minList;
            }else {
                result.clear();
                result.add(p);
                return result;
            }
        }
        return null;
    }

    // TODO: 2/11/2018  
    private List<Point> findMax(GoBangBoard goBangBoard, int role, int MAX_SCORE) {
        List<Point> result = new ArrayList<>();
        for (int i = 0; i < goBangBoard.LINE_COUNT; i++) {
            for (int j = 0; j < goBangBoard.LINE_COUNT; j++) {
                if(goBangBoard.getRole(i, j) == Constants.CHESS_NONE) {
                    Point p = new Point(i, j);
                    if(Neighbor.hasNeighbor(goBangBoard, p, 2, 1)) {
                        // TODO: 2/7/2018 有修改
//                        goBangBoard.update(p);
                        int score = evaluatePoint.calculateScore(goBangBoard, p, role);
                        if(score >= Score.FIVE) {
                            result.add(p);
                            return result;
                        }
                        if(score >= MAX_SCORE) {
                            result.add(p);
                        }
                    }
                }
            }
        }
        result = goBangBoard.sortPointList(result);
        return result;
    }

    private List<Point> findMin(GoBangBoard goBangBoard, int role, int MAX_SCORE) {
        List<Point> result = new ArrayList<>();
        List<Point> fives = new ArrayList<>();
        List<Point> fours = new ArrayList<>();
        for (int i = 0; i < goBangBoard.LINE_COUNT; i++) {
            for (int j = 0; j < goBangBoard.LINE_COUNT; j++) {
                if(goBangBoard.getRole(i, j) == Constants.CHESS_NONE) {
                    Point p = new Point(i, j);
                    if(Neighbor.hasNeighbor(goBangBoard, p, 2, 1)) {//必须有邻居的才行
                        int s1 = evaluatePoint.calculateScore(goBangBoard, p, role);
                        int s2 = evaluatePoint.calculateScore(goBangBoard, p, Constants.reverse(role));
                        if(s1 >= Score.FIVE) {
                            goBangBoard.setScore(p, role, -s1);
                            List<Point> pointList = new ArrayList<>();
                            pointList.add(p);
                            return pointList;
                        }

                        if(s1 >= Score.FOUR) {
                            goBangBoard.setScore(p, role, -s1);
                            fours.add(0, p);
                            continue;
                        }

                        if(s2 >= Score.FIVE) {
                            goBangBoard.setScore(p, role, s2);
                            fives.add(p);
                            continue;
                        }

                        if(s2 >= Score.FOUR) {
                            goBangBoard.setScore(p, role, s2);
                            fours.add(p);
                            continue;
                        }

                        if(s1 >= MAX_SCORE || s2 >= MAX_SCORE) {
                            goBangBoard.setScore(p, role, s1);
                            result.add(p);
                        }
                    }
                }
            }
        }
        if(fives.size() > 0) {
            Point p = fives.get(0);
            fives.clear();
            fives.add(p);
            return fives;
        }
        if(fours.size() > 0) {
            Point p = fours.get(0);
            fours.clear();
            fours.add(p);
            return fours;
        }

        return goBangBoard.sortPointList(result);
    }

    private List<Point> min(GoBangBoard goBangBoard, int role, int deep) {
        debugNodeCount++;
        int win = goBangBoard.getWinPoint();
        if(win == role) return null;
        if(win == Constants.reverse(role)) return null;
        if(deep <= 0) return null;
        if(Config.cache) {

        }
        List<Point> points = findMin(goBangBoard, Constants.reverse(role), MAX_SCORE);
        if(points.size() == 0) {
            return null;
        }
        int score = -1 * goBangBoard.getScore(points.get(0));
        if(points.size() > 0 && score >= Score.FOUR) return null;//为了减少一层搜索，活四就行了
        List <Point> cands = new ArrayList<>();
        int currentRole = Constants.reverse(role);
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            goBangBoard.setBoardWhiteBlack(p, currentRole);
            List<Point> pointList = maxSearch(goBangBoard, role, deep - 1);
            goBangBoard.setBoardWhiteBlack(p, Constants.CHESS_NONE);
            if(pointList.size() > 0) {
                pointList.add(0, p);
                cands.add(p);
                continue;
            }else {
                return null;
            }
        }
        Point result = cands.get((int) java.lang.Math.floor(cands.size() * java.lang.Math.random()));
        // TODO: 2/6/2018 置换表
        List<Point> minList = new ArrayList<>();
        minList.add(result);
        return minList;
    }
}





















