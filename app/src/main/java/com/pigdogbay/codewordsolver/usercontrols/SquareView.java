package com.pigdogbay.codewordsolver.usercontrols;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.HapticFeedbackConstants;
import android.view.View;

import com.pigdogbay.codewordsolver.R;
import com.pigdogbay.codewordsolver.model.Square;
import com.pigdogbay.lib.utils.DisplayUtils;

/**
 * Created by Mark on 05/12/2016.
 * Custom view of a square in the codeword grid
 */
public class SquareView extends View implements View.OnClickListener, View.OnLongClickListener {
    public static final int PREF_WIDTH = 40;
    public static final int PREF_HEIGHT = 40;

    private final onSquareClickListener squareClickListener;
    private Paint numberPaint, letterPaint;
    private float letterX, letterY, numberX, numberY;
    private int preferredWidth, preferredHeight;
    private Square square;
    private Drawable deleteImg;
    private boolean showQuestionMarkIfEmpty = false;

    public boolean isShowQuestionMarkIfEmpty() {
        return showQuestionMarkIfEmpty;
    }

    public void setShowQuestionMarkIfEmpty(boolean showQuestionMarkIfEmpty) {
        this.showQuestionMarkIfEmpty = showQuestionMarkIfEmpty;
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public SquareView(Context context, onSquareClickListener squareClickListener) {
        super(context);
        this.squareClickListener = squareClickListener;
        init();
    }

    private void init() {
        numberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        letterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        letterPaint.setColor(Color.BLACK);
        letterPaint.setTypeface(Typeface.SANS_SERIF);
        letterPaint.setTextAlign(Paint.Align.CENTER);
        preferredWidth = PREF_WIDTH;
        preferredHeight = PREF_HEIGHT;
        if (squareClickListener != null) {
            setOnClickListener(this);
            setOnLongClickListener(this);
        }
        //image is a vector
        deleteImg = AppCompatDrawableManager.get().getDrawable(getContext(), R.drawable.ic_backspace_black_24dp);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float width = w;
        float numSize = width * 0.3f;
        float letterSize = width * 0.7f;
        numberPaint.setTextSize(numSize);
        letterPaint.setTextSize(letterSize);


        numberX = width * 0.1f;
        numberY = numSize * 1.09f;
        //center letter horizontally
        letterX = width / 2.0f;
        letterY = width * 0.91f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //http://stackoverflow.com/questions/12266899/onmeasure-custom-view-explanation
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        int prefWidthPx = (int) DisplayUtils.convertDpToPixel(preferredWidth, getContext());
        int prefHeightPx = (int) DisplayUtils.convertDpToPixel(preferredHeight, getContext());

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(prefWidthPx, widthSize);
        } else {
            width = prefWidthPx;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(width, heightSize);
        } else {
            height = prefHeightPx;
        }

        deleteImg.setBounds((int) (width * 0.1f), (int) (height * 0.1f), (int) (width * 0.9f), (int) (height * 0.9f));
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (square == null) {
            //draw blank
        } else if (square.getNumber() == Square.DELETE) {
            deleteImg.draw(canvas);
        } else if (!square.getLetter().equals("")) {
            canvas.drawText(square.getNumberString(), numberX, numberY, numberPaint);
            canvas.drawText(square.getLetter(), letterX, letterY, letterPaint);
        } else if (showQuestionMarkIfEmpty) {
            canvas.drawText(square.getNumberString(), numberX, numberY, numberPaint);
            canvas.drawText("?", letterX, letterY, letterPaint);
        } else {
            canvas.drawText(square.getNumberString(), numberX, numberY, numberPaint);
        }
    }


    @Override
    public void onClick(View view) {
        this.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        squareClickListener.onSquareClicked(this);
        AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        if (am != null) {
            float vol = 0.5f; //This will be half of the default system sound
            am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD, vol);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        squareClickListener.onSquareLongClicked(this);
        return true;
    }
}
