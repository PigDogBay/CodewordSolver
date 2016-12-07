package com.pigdogbay.codewordsolver.usercontrols;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.pigdogbay.codewordsolver.R;
import com.pigdogbay.codewordsolver.model.Square;

import java.util.List;

import static android.R.attr.x;

/**
 * Created by Mark on 05/12/2016.
 * Contains a row of squares for the keyboard
 */
public class RowView extends LinearLayout {


    public RowView(Context context, List<Square> squares, onSquareClickListener squareClickListener) {
        super(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1.0f;
        layoutParams.setMargins(0, 0, 2, 0);

        for (Square s : squares) {
            SquareView squareView = new SquareView(context, squareClickListener);
            squareView.setSquare(s);
            squareView.setLayoutParams(layoutParams);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Drawable background = getContext().getDrawable(R.drawable.keyboard_selector);
                squareView.setBackground(background);
            } else {
                squareView.setBackgroundColor(Color.WHITE);
            }
            addView(squareView);
        }
    }
    @Override
    public void invalidate() {
        super.invalidate();
        for (int i = 0 ; i<getChildCount();i++){
            getChildAt(i).invalidate();
        }
    }

}
