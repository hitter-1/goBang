package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.zhongyu.gobang_ai.R;

import utils.DimenUtil;

/**
 * Created by zhongyu on 1/12/2018.
 */

public class GoBangBoard extends View {

    private static final int LINE_COUNT = 15;//棋盘行数和列数
    private static final int BOARD_MARGIN = 30;//棋盘距离手机边框距离
    private static final int HALF_CHESS_SIZE = 35;

    private static final int BOARD_SIZE = LINE_COUNT;
    private static final float BOARD_LINE_WIDTH_DP = 0.7f;//棋盘线宽度
    private static final float BOARD_FRAME_WIDTH_DP = 1;//棋盘框的线宽度
    private static final float BOARD_POINT_RADIUS_DP = 2;//棋盘五个圆点的半径宽度

    private int[][] mBoard = new int[BOARD_SIZE][BOARD_SIZE];

    private Paint mLinePaint;//画线
    private Paint mPointPaint;//画点

    private Context mContext;

    private float[] mBoardFramePoints;//棋盘边框
    private float[] mVerticalLinePoints;//棋盘竖线
    private float[] mHorizontalLinePoints;//棋盘横线
    private float[] mBlackPoints;//棋盘黑点

    private float mGridWidth;
    private float mGridHeight;

    private Bitmap mWhiteChessBitmap;//白色棋子
    private Bitmap mBlackChessBitmap;//黑色棋子


    private int mLineCount;

    public GoBangBoard(Context context) {
        super(context);
        init(context);
    }

    public GoBangBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GoBangBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        //线画笔初始化
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);//消除锯齿平滑处理
        mLinePaint.setColor(Color.BLACK);

        //点画笔初始化
        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);

        mLineCount = LINE_COUNT;

        mWhiteChessBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.chess_white);//白色棋子初始化
        mBlackChessBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.chess_black);//黑色棋子初始化
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //第一种
        int width = getMySize(100, widthMeasureSpec);
        int height = getMySize(100, heightMeasureSpec);

        if (width < height) {
            height = width;
        } else {
            width = height;
        }

        setMeasuredDimension(width, height);

        calcLinePoints();
//        第二种
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        setMeasuredDimension(width, width);
    }

    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED: {//如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.AT_MOST: {//如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mySize = size;
                break;
            }
            case MeasureSpec.EXACTLY: {//如果是固定的大小，那就不要去改变它
                mySize = size;
                break;
            }
        }
        return mySize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLines(canvas);
    }

    //计算线和点位置
    private void calcLinePoints() {
        mHorizontalLinePoints = new float[mLineCount * 4];//一条线需要4个坐标
        mVerticalLinePoints = new float[mLineCount * 4];

        float boardWidth = getMeasuredWidth() - BOARD_MARGIN * 2;
        float boardHeight = getMeasuredHeight() - BOARD_MARGIN * 2;

        mGridWidth = boardWidth / (mLineCount - 1);
        for (int i = 0; i < mLineCount * 4; i += 4) {
            mVerticalLinePoints[i] = i * mGridWidth / 4 + BOARD_MARGIN;
            mVerticalLinePoints[i + 1] = BOARD_MARGIN;
            mVerticalLinePoints[i + 2] = i * mGridWidth / 4 + BOARD_MARGIN;
            mVerticalLinePoints[i + 3] = boardHeight + BOARD_MARGIN;
        }

        mGridHeight = boardHeight / (mLineCount - 1);
        for (int i = 0; i < mLineCount * 4; i += 4) {
            mHorizontalLinePoints[i] = BOARD_MARGIN;
            mHorizontalLinePoints[i + 1] = i * mGridHeight / 4 + BOARD_MARGIN;
            mHorizontalLinePoints[i + 2] = boardWidth + BOARD_MARGIN;
            mHorizontalLinePoints[i + 3] = i * mGridHeight / 4 + BOARD_MARGIN;
        }

        float frameMargin = BOARD_MARGIN * 0.8f;
        mBoardFramePoints = new float[]{frameMargin, frameMargin, getMeasuredWidth() - frameMargin, frameMargin,//上横
                frameMargin, getMeasuredHeight() - frameMargin, getMeasuredWidth() - frameMargin, getMeasuredHeight() - frameMargin,//下横
                frameMargin, frameMargin, frameMargin, getMeasuredHeight() - frameMargin,//左竖
                getMeasuredWidth() - frameMargin, frameMargin, getMeasuredWidth() - frameMargin, getMeasuredHeight() - frameMargin};//右竖

        mBlackPoints = new float[]{3 * mGridWidth + BOARD_MARGIN, 3 * mGridHeight + BOARD_MARGIN,
                11 * mGridWidth + BOARD_MARGIN, 3 * mGridHeight + BOARD_MARGIN,
                7 * mGridWidth + BOARD_MARGIN, 7 * mGridHeight + BOARD_MARGIN,
                3 * mGridWidth + BOARD_MARGIN, 11 * mGridHeight + BOARD_MARGIN,
                11 * mGridWidth + BOARD_MARGIN, 11 * mGridHeight + BOARD_MARGIN};
    }

    private void drawLines(Canvas canvas) {
        mLinePaint.setStrokeWidth(DimenUtil.dp2px(mContext, BOARD_LINE_WIDTH_DP));//设置线宽，单位像素
        canvas.drawLines(mHorizontalLinePoints, mLinePaint);
        canvas.drawLines(mVerticalLinePoints, mLinePaint);
        mLinePaint.setStrokeWidth(DimenUtil.dp2px(mContext, BOARD_FRAME_WIDTH_DP));
        canvas.drawLines(mBoardFramePoints, mLinePaint);
    }

}






