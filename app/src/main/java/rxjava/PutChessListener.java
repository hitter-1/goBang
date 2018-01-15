package rxjava;

import io.reactivex.Observable;

/**
 * Created by zhongyu on 1/15/2018.
 */

public interface PutChessListener<E> {
    Observable<E> onPutChess(int[][] board, int x, int y);
}
