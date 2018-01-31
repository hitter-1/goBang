package com.zhongyu.ai.utils;

/**
 * Created by zhongyu on 1/29/2018.
 */

public class Negamax {

    int MAX = Score.FIVE * 10;
    int MIN = - 1 * MAX;

    int total=0, //总节点数
            steps=0,  //总步数
            count,  //每次思考的节点数
            PVcut,
            ABcut,  //AB剪枝次数
            cacheCount=0, //zobrist缓存节点数
            cacheGet=0; //zobrist缓存命中数量

    int Cache = {};

    int checkmateDeep = Config.checkmateDeep;
    
    /**
     * max min search
     * white is max, black is min
     */
    int maxmin(int deep, int  _checkmateDeep) {
        int best = MIN;
        int points = board.gen();
        int bestPoints = [];

        count = 0;
        ABcut = 0;
        PVcut = 0;
        checkmateDeep = (_checkmateDeep == undefined ? checkmateDeep : _checkmateDeep);

        for(int i=0;i<points.length;i++) {
            int p = points[i];
            board.put(p, R.com);
            int v = - max(deep-1, -MAX, -best, R.hum);

            //边缘棋子的话，要把分数打折，避免电脑总喜欢往边上走
            if(p[0]<3 || p[0] > 11 || p[1] < 3 || p[1] > 11) {
                v = .5 * v;
            }

            //console.log(v, p);
            //如果跟之前的一个好，则把当前位子加入待选位子
            if(math.equal(v, best)) {
                bestPoints.push(p);
            }
            //找到一个更好的分，就把以前存的位子全部清除
            if(math.greatThan(v, best)) {
                best = v;
                bestPoints = [];
                bestPoints.push(p);
            }


            board.remove(p);
        }
        console.log("分数:"+best.toFixed(3)+", 待选节点:"+JSON.stringify(bestPoints));
        int result = bestPoints[Math.floor(bestPoints.length * Math.random())];
        result.score = best;
        steps ++;
        total += count;
        console.log('搜索节点数:'+ count+ ',AB剪枝次数:'+ABcut + ', PV剪枝次数:' + PVcut + ', 缓存命中:' + (cacheGet / cacheCount).toFixed(3) + ',' + cacheGet + '/' + cacheCount + ',算杀缓存命中:' + (debug.checkmate.cacheGet / debug.checkmate.cacheCount).toFixed(3) + ',' + debug.checkmate.cacheGet + '/'+debug.checkmate.cacheCount); //注意，减掉的节点数实际远远不止 ABcut 个，因为减掉的节点的子节点都没算进去。实际 4W个节点的时候，剪掉了大概 16W个节点
        console.log('当前统计：总共'+ steps + '步, ' + total + '个节点, 平均每一步' + Math.round(total/steps) +'个节点');
        console.log("================================");
        return result;
    }

    int max = function(deep, alpha, beta, role) {

        if(config.cache) {
            int c = Cache[board.zobrist.code];
            if(c) {
                if(c.deep >= deep) {
                    cacheGet ++;
                    return c.score;
                }
            }
        }

        int v = board.evaluate(role);
        count ++;
        if(deep <= 0 || math.greatOrEqualThan(v, T.FIVE)) {
            return v;
        }

        int best = MIN;
        int points = board.gen();

        for(int i=0;i<points.length;i++) {
            int p = points[i];
            board.put(p, role);

            int v = - max(deep-1, -beta, -1 *( best > alpha ? best : alpha), R.reverse(role)) * config.deepDecrease;
            board.remove(p);

            if(math.greatThan(v, best)) {
                best = v;
            }
            if(math.greatOrEqualThan(v, beta)) { //AB 剪枝
                ABcut ++;
                cache(deep, v);
                return v;
            }
        }
        if( (deep == 2 || deep == 3 ) && math.littleThan(best, SCORE.THREE*2) && math.greatThan(best, SCORE.THREE * -1)
                ) {
            //int mate = checkmate(role, checkmateDeep);
            int mate = checkmateFast(board.board, role, checkmateDeep);
            if(mate) {
                int score = mate.score * Math.pow(.8, mate.length) * (role === R.com ? 1 : -1);
                cache(deep, score);
                return score;
            }
        }
        cache(deep, best);

        return best;
    }
    
}
