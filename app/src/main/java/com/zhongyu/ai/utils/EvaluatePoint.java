package com.zhongyu.ai.utils;

import android.util.Log;

import com.zhongyu.ai.bean.Point;
import com.zhongyu.ai.view.GoBangBoard;

import java.lang.*;
import java.lang.Math;

import static com.zhongyu.ai.view.dialog.CompositionDialog.TAG;

/**
 * Created by zhongyu on 1/24/2018.
 */

public class EvaluatePoint {
    int result = 0;
    int count = 0;
    int block = 0;
    int len = GoBangBoard.LINE_COUNT;
    int secondCount = 0;
    int empty = -1;

    public int calculateScore(GoBangBoard goBangBoard, Point point, int role) {
        result = 0;
        reset();

        for(int i=point.y+1;true;i++) {
            if(i>=len) {
                block ++;
                break;
            }
            int t = goBangBoard.getRole(point.x, i);//[p[0]][i];
            if(t == Constants.CHESS_NONE) {
                if(empty == -1 && i<len-1 && goBangBoard.getRole(point.x, i + 1) == role) { //[p[0]][i+1]
                    empty = count;
                    continue;
                } else {
                    break;
                }
            }
            if(t == role) {
                count ++;
                continue;
            } else {
                block ++;
                break;
            }
        }


        for(int i = point.y - 1;true;i--) {//p[1]-1
            if(i<0) {
                block ++;
                break;
            }
            int t = goBangBoard.getRole(point.x, i); //board[p[0]][i];
            if(t == Constants.CHESS_NONE) {
                if(empty == -1 && i>0 &&  goBangBoard.getRole(point.x, i - 1) == role) { //board[p[0]][i-1]
                    empty = 0;  //注意这里是0，因为是从右往左走的
                    continue;
                } else {
                    break;
                }
            }
            if(t == role) {
                secondCount ++;
//                empty !== -1 && empty ++; 
                if(empty != -1) {
                    empty++;
                }
                //注意这里，如果左边又多了己方棋子，那么empty的位置就变大了
                continue;
            } else {
                block ++;
                break;
            }
        }

        count+= secondCount;


        result += CountToType.count(count, block, empty);

        //纵向
        reset();

        for(int i=point.x+1;true;i++) {
            if(i>=len) {
                block ++;
                break;
            }
            int t = goBangBoard.getRole(i, point.y);//board[i][p[1]];
            if(t == Constants.CHESS_NONE) {
                if(empty == -1 && i<len-1 && goBangBoard.getRole(i + 1, point.y) == role) {
                    empty = count;
                    continue;
                } else {
                    break;
                }
            }
            if(t == role) {
                count ++;
                continue;
            } else {
                block ++;
                break;
            }
        }

        for(int i=point.x-1;true;i--) {
            if(i<0) {
                block ++;
                break;
            }
            int t = goBangBoard.getRole(i, point.y);//  board[i][p[1]];
            if(t == Constants.CHESS_NONE) {
                if(empty == -1 && i>0 &&  goBangBoard.getRole(i - 1, point.y)  == role) {
                    empty = 0;
                    continue;
                } else {
                    break;
                }
            }
            if(t == role) {
                secondCount++;
//                empty !== -1 && empty ++;//注意这里，如果左边又多了己方棋子，那么empty的位置就变大了
                if(empty != -1) {
                    empty++;
                }
                continue;
            } else {
                block ++;
                break;
            }
        }

        count+= secondCount;
        result += CountToType.count(count, block, empty);


        // TODO: 2/9/2018  next
        reset();

        for(int i=1;true;i++) {
            int x = point.x+i, y = point.y+i;
            if(x>=len || y>=len) {
                block ++;
                break;
            }
            int t = goBangBoard.getRole(x, y);//     board[x][y];
            if(t == Constants.CHESS_NONE) {
                if(empty == -1 && (x<len-1 && y < len-1) && goBangBoard.getRole(x + 1, y + 1) == role) {
                    empty = count;
                    continue;
                } else {
                    break;
                }
            }
            if(t == role) {
                count ++;
                continue;
            } else {
                block ++;
                break;
            }
        }

        for(int i=1;true;i++) {
            int x = point.x-i, y = point.y-i;
            if(x<0||y<0) {
                block ++;
                break;
            }
            int t =  goBangBoard.getRole(x, y);//   board[x][y];
            if(t == Constants.CHESS_NONE) {
                if(empty == -1 && (x>0 && y>0) && goBangBoard.getRole(x - 1, y - 1)  == role) {
                    empty = 0;
                    continue;
                } else {
                    break;
                }
            }
            if(t == role) {
                secondCount ++;
//                empty !== -1 && empty ++;//注意这里，如果左边又多了己方棋子，那么empty的位置就变大了
                if(empty != -1) {
                    empty++;
                }
                continue;
            } else {
                block ++;
                break;
            }
        }

        count+= secondCount;
        result += CountToType.count(count, block, empty);


        // TODO: 2/9/2018 bug
        reset();

        for(int i=1; true;i++) {
            int x = point.x+i, y = point.y-i;
            if(x<0||y<0||x>=len||y>=len) {
                block ++;
                break;
            }
            int t = goBangBoard.getRole(x, y);// board[x][y];
            if(t == Constants.CHESS_NONE) {
                if(empty == -1 && (x<len-1 && y<len-1) && goBangBoard.getRole(x + 1, y - 1) == role) {
                    empty = count;
                    continue;
                } else {
                    break;
                }
            }
            if(t == role) {
                count ++;
                continue;
            } else {
                block ++;
                break;
            }
        }

        for(int i=1;true;i++) {
            int x = point.x-i, y = point.y+i;
            if(x<0||y<0||x>=len||y>=len) {
                block ++;
                break;
            }
            int t = goBangBoard.getRole(x, y);//  board[x][y];
            if(t == Constants.CHESS_NONE) {
                if(empty == -1 && (x>0 && y>0) && goBangBoard.getRole(x - 1, y + 1) == role) {
                    empty = 0;
                    continue;
                } else {
                    break;
                }
            }
            if(t == role) {
                secondCount++;
//                empty !== -1 && empty ++;//注意这里，如果左边又多了己方棋子，那么empty的位置就变大了
                if(empty != -1) {
                    empty++;
                }
                continue;
            } else {
                block ++;
                break;
            }
        }

        count+= secondCount;
        result += CountToType.count(count, block, empty);
        return CountToType.typeToScore(result);
    }

    //棋面估分
    public int evaluate(GoBangBoard goBangBoard, int role) {
        // TODO: 2/11/2018 缓存省略
        int comMaxScore = -Score.FIVE;
        int humMaxScore = -Score.FIVE;
        for (int i = 0; i < goBangBoard.LINE_COUNT; i++) {
            for (int j = 0; j < goBangBoard.LINE_COUNT; j++) {
//                goBangBoard.update(new Point(i,j));// TODO: 2/14/2018 有变动
                if(goBangBoard.getRole(i, j) == Constants.CHESS_NONE) {
                    comMaxScore = Math.max(goBangBoard.getAiScore()[i][j], comMaxScore);
                    humMaxScore = Math.max(goBangBoard.getMyScore()[i][j], humMaxScore);
                }else {
                }
            }
        }

        int result = (role == Constants.AI ? 1 : -1) * (comMaxScore - humMaxScore);
        return result;
    }

    void reset() {
        count = 1;
        block = 0;
        empty = -1;
        secondCount = 0;
    }
}
