package com.zhongyu.ai.utils;

/**
 * Created by zhongyu on 1/25/2018.
 */

public class CountToType {

    public static int count(int count, int block, int empty) {
        //没有空位
        if(empty <= 0) {
            if(count >= 5) return Score.FIVE;
            if(block == 0) {
                switch(count) {
                    case 1: return Score.ONE;
                    case 2: return Score.TWO;
                    case 3: return Score.THREE;
                    case 4: return Score.FOUR;
                }
            }

            if(block == 1) {
                switch(count) {
                    case 1: return Score.BLOCKED_ONE;
                    case 2: return Score.BLOCKED_TWO;
                    case 3: return Score.BLOCKED_THREE;
                    case 4: return Score.BLOCKED_FOUR;
                }
            }

        } else if(empty == 1 || empty == count-1) {
            //第1个是空位
            if(count >= 6) {
                return Score.FIVE;
            }
            if(block == 0) {
                switch(count) {
                    case 2: return Score.TWO/2;
                    case 3: return Score.THREE;
                    case 4: return Score.BLOCKED_FOUR;
                    case 5: return Score.FOUR;
                }
            }

            if(block == 1) {
                switch(count) {
                    case 2: return Score.BLOCKED_TWO;
                    case 3: return Score.BLOCKED_THREE;
                    case 4: return Score.BLOCKED_FOUR;
                    case 5: return Score.BLOCKED_FOUR;
                }
            }
        } else if(empty == 2 || empty == count-2) {
            //第二个是空位
            if(count >= 7) {
                return Score.FIVE;
            }
            if(block == 0) {
                switch(count) {
                    case 3: return Score.THREE;
                    case 4:
                    case 5: return Score.BLOCKED_FOUR;
                    case 6: return Score.FOUR;
                }
            }

            if(block == 1) {
                switch(count) {
                    case 3: return Score.BLOCKED_THREE;
                    case 4: return Score.BLOCKED_FOUR;
                    case 5: return Score.BLOCKED_FOUR;
                    case 6: return Score.FOUR;
                }
            }

            if(block == 2) {
                switch(count) {
                    case 4:
                    case 5:
                    case 6: return Score.BLOCKED_FOUR;
                }
            }
        } else if(empty == 3 || empty == count-3) {
            if(count >= 8) {
                return Score.FIVE;
            }
            if(block == 0) {
                switch(count) {
                    case 4:
                    case 5: return Score.THREE;
                    case 6: return Score.BLOCKED_FOUR;
                    case 7: return Score.FOUR;
                }
            }

            if(block == 1) {
                switch(count) {
                    case 4:
                    case 5:
                    case 6: return Score.BLOCKED_FOUR;
                    case 7: return Score.FOUR;
                }
            }

            if(block == 2) {
                switch(count) {
                    case 4:
                    case 5:
                    case 6:
                    case 7: return Score.BLOCKED_FOUR;
                }
            }
        } else if(empty == 4 || empty == count-4) {
            if(count >= 9) {
                return Score.FIVE;
            }
            if(block == 0) {
                switch(count) {
                    case 5:
                    case 6:
                    case 7:
                    case 8: return Score.FOUR;
                }
            }

            if(block == 1) {
                switch(count) {
                    case 4:
                    case 5:
                    case 6:
                    case 7: return Score.BLOCKED_FOUR;
                    case 8: return Score.FOUR;
                }
            }

            if(block == 2) {
                switch(count) {
                    case 5:
                    case 6:
                    case 7:
                    case 8: return Score.BLOCKED_FOUR;
                }
            }
        } else if(empty == 5 || empty == count-5) {
            return Score.FIVE;
        }

        return 0;
    }

    public static int typeToScore(int type) {
        if(type < Score.FOUR && type >= Score.BLOCKED_FOUR) {

            if(type >= Score.BLOCKED_FOUR && type < (Score.BLOCKED_FOUR + Score.THREE)) {
                //单独冲四，意义不大
                return Score.THREE;
            } else if(type >= Score.BLOCKED_FOUR + Score.THREE && type < Score.BLOCKED_FOUR * 2) {
                return Score.FOUR;  //冲四活三，比双三分高，相当于自己形成活四
            } else {
                //双冲四 比活四分数也高
                return Score.FOUR * 2;
            }
        }
        return type;
    }
}
