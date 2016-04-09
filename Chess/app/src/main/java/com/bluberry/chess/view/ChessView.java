package com.bluberry.chess.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by blueberry on 2016/4/9.
 * 五子棋
 * gobang
 */
public class ChessView extends View {

    private static int chessLine = 16;
    private float spaceSize;
    private int width;
    private int heigth;
    private float ratioCicleSizeOfSpaceSize = (float) 3 / 4; //棋子大小占间隔的比例

    private Paint mPaint = new Paint();

    private Bitmap whiteBitmap; //白棋
    private Bitmap blackBitmap; //黑棋

    public ChessView(Context context) {
        super(context);

    }

    public ChessView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int withMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(width, height);
        if (withMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.UNSPECIFIED) {
            size = width;
        }
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0x44ff0000);
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        drawBorder(canvas);
        drawChessPieces(canvas);

    }

    private void drawChessPieces(Canvas canvas) {
        for (ChessPieces[] mChessPieces : ChessPieces.positions)
            for (ChessPieces pieces : mChessPieces) {
                if (pieces == null) continue;
                if (pieces.white) {
                    canvas.drawBitmap(whiteBitmap, pieces.col * spaceSize + spaceSize / 2 - whiteBitmap.getWidth() / 2,
                            pieces.row * spaceSize + spaceSize / 2 - whiteBitmap.getHeight() / 2, mPaint
                    );
                } else {
                    canvas.drawBitmap(blackBitmap, pieces.col * spaceSize + spaceSize / 2 - blackBitmap.getWidth() / 2,
                            pieces.row * spaceSize + spaceSize / 2 - blackBitmap.getHeight() / 2, mPaint
                    );
                }
            }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        spaceSize = (float) h / chessLine;
        width = w;
        heigth = h;
        getChessPieces();
    }

    /**
     * 画出白棋与黑棋的bitmap
     */
    private void getChessPieces() {
        whiteBitmap = Bitmap.createBitmap((int) (ratioCicleSizeOfSpaceSize * spaceSize),
                (int) (ratioCicleSizeOfSpaceSize * spaceSize), Bitmap.Config.ARGB_8888);
        Canvas cavas = new Canvas(whiteBitmap);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        cavas.drawCircle(whiteBitmap.getWidth() / 2, whiteBitmap.getHeight() / 2, Math.min(whiteBitmap.getWidth(),
                whiteBitmap.getHeight()) / 2, mPaint);

        blackBitmap = Bitmap.createBitmap((int) (ratioCicleSizeOfSpaceSize * spaceSize),
                (int) (ratioCicleSizeOfSpaceSize * spaceSize), Bitmap.Config.ARGB_8888);
        cavas = new Canvas(blackBitmap);
        mPaint.setColor(Color.BLACK);
        cavas.drawCircle(blackBitmap.getWidth() / 2, blackBitmap.getHeight() / 2, Math.min(blackBitmap.getWidth(),
                blackBitmap.getHeight()) / 2, mPaint);
    }

    private void drawBorder(Canvas canvas) {
        for (int i = 0; i < chessLine; i++) {
            float startX = spaceSize / 2;
            float endX = width - startX;
            float y = spaceSize / 2 + i * spaceSize;

            float startY = spaceSize / 2;
            float endY = heigth - startY;
            float x = spaceSize / 2 + i * spaceSize;
            canvas.drawLine(startX, y, endX, y, mPaint);
            canvas.drawLine(x, startY, x, endY, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            getChessPiecesWithPoint(x, y);
            //判断是否有五子相连
            checkGobang();
            invalidate();
        }
        return true;
    }

    /**
     * 检查是否有五子相连
     */
    private void checkGobang() {
        for (ChessPieces[] mChessPieces : ChessPieces.positions)
            for (ChessPieces pieces : mChessPieces) {
                if (pieces != null)
                    pieces.check();
            }
    }

    private ChessPieces getChessPiecesWithPoint(int x, int y) {
        ChessPieces chessPieces = null;
        if (ChessPieces.isWhiteAble) {
            chessPieces = new WhiteChessPieces((int) ((y - 2 / spaceSize) / spaceSize)
                    , (int) ((x - 2 / spaceSize) / spaceSize));
        } else {
            chessPieces = new BlackChessPieces((int) ((y - 2 / spaceSize) / spaceSize)
                    , (int) ((x - 2 / spaceSize) / spaceSize));
        }
        return chessPieces;
    }


    /**
     * 棋子
     */
    private static abstract class ChessPieces {
        public static boolean isWhiteAble = false;
        public static int MAX = 8;
        public static int MIN=5;
        public static ChessPieces[][] positions = new ChessPieces[chessLine + 1][chessLine + 1];
        protected int row, col;//位置
        protected boolean white;//是否为白棋

        public ChessPieces(int row, int col ,boolean white) {
            this.row = row;
            this.col = col;
            if (positions[row][col] == null) {
                positions[row][col] = this;
                isWhiteAble = !isWhiteAble;
            }
            this.white = white;
        }

        public void check() {
            checkRight();
            checkDown();
            checkObliqueUp();
            checkObliqueDown();
        }

        void checkRight() {
            int count = 0;
            for (int i = 0; i < MAX; i++) {
                if(col+i >chessLine)break ;
                if (positions[row][col + i] != null && positions[row][col + i].white == white) {
                    count++;
                }else{
                    break ;
                }
            }
            if (count >= MIN) {
                for (int i = 0; i < count; i++) {
                    if (positions[row][col + i] != null && positions[row][col + i].white == white) {
                        positions[row][col + i] = null;
                    }
                }
            }
        }

        void checkDown() {
            int count = 0;
            for (int i = 0; i < MAX; i++) {
                if(row+i>chessLine) break;
                if (positions[row + i][col] != null && positions[row + i][col].white == white) {
                    count++;
                }else{
                    break ;
                }
            }
            if (count >= MIN) {
                for (int i = 0; i < count; i++) {
                    if (positions[row + i][col] != null && positions[row + i][col].white == white) {
                        positions[row + i][col] = null;
                    }
                }
            }
        }

        void checkObliqueUp() {
            int count = 0;
            for (int i = 0; i < MAX; i++) {
                if(row-i<0||col+i>chessLine)break;
                if (positions[row - i][col + i] != null && positions[row - i][col + i].white == white) {
                    count++;
                }else{
                    break;
                }
            }
            if (count >= MIN) {
                for (int i = 0; i < count; i++) {
                    if (positions[row - i][col + i] != null && positions[row - i][col + i].white == white) {
                        positions[row - i][col + i] = null;
                    }
                }
            }

        }

        void checkObliqueDown() {
            int count = 0;
            for (int i = 0; i < MAX; i++) {
                if(row+i>chessLine||col+i>chessLine)break;
                if (positions[row + i][col + i] != null && positions[row + i][col + i].white == white) {
                    count++;
                }else{
                    break;
                }
            }
            if (count >= MIN) {
                for (int i = 0; i < count; i++) {
                    if (positions[row + i][col + i] != null && positions[row + i][col + i].white == white) {
                        positions[row + i][col + i] = null;
                    }
                }
            }
        }
    }

    /**
     * 白
     */
    private static class WhiteChessPieces extends ChessPieces {

        public WhiteChessPieces(int row, int col) {
            super(row, col,true);
        }
    }

    /**
     * 黑
     */
    private static class BlackChessPieces extends ChessPieces {

        public BlackChessPieces(int row, int col) {
            super(row, col,false);
        }
    }

}
