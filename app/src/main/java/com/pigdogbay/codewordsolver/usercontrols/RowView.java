package com.pigdogbay.codewordsolver.usercontrols;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pigdogbay.codewordsolver.R;
import com.pigdogbay.codewordsolver.model.Square;

import java.util.List;

/**
 * Created by Mark on 05/12/2016.
 * Contains a row of squares for the keyboard
 */
public class RowView extends LinearLayout {

    LinearLayout.LayoutParams layoutParams;
    public RowView(Context context, List<Square> squares, onSquareClickListener squareClickListener) {
        super(context);
        layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1.0f;
        layoutParams.setMargins(0, 0, 2, 0);

        for (Square s : squares) {
            SquareView squareView = new SquareView(context, squareClickListener);
            squareView.setSquare(s);
            squareView.setLayoutParams(layoutParams);
            setBackground(squareView);
            addView(squareView);
        }
    }

    private void setBackground(SquareView squareView) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Drawable background = getContext().getDrawable(R.drawable.keyboard_selector);
            squareView.setBackground(background);
        } else {
            squareView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimary_50));
        }
    }

    public void addDummyKey(Context context){
        SquareView squareView =new SquareView(context, null);
        squareView.setLayoutParams(layoutParams);
        setBackground(squareView);
        addView(squareView);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        for (int i = 0 ; i<getChildCount();i++){
            getChildAt(i).invalidate();
        }
    }

}
