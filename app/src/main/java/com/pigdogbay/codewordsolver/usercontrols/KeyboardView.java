package com.pigdogbay.codewordsolver.usercontrols;

import android.content.Context;
import android.content.res.Configuration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pigdogbay.codewordsolver.model.Square;

import java.util.List;

/**
 * Created by Mark on 05/12/2016.
 * Keyboard made up of the squares of a codeword grid
 */
public class KeyboardView extends LinearLayout{

    public KeyboardView(Context context, List<Square> squares, onSquareClickListener squareClickListener) {
        super(context);

        setOrientation(VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        layoutParams.setMargins(2,1,2,1);
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            //Portrait
            RowView rowView = new RowView(context, squares.subList(0, 9), squareClickListener);
            rowView.setLayoutParams(layoutParams);
            addView(rowView);

            rowView = new RowView(context, squares.subList(9, 18),squareClickListener);
            rowView.setLayoutParams(layoutParams);
            addView(rowView);

            rowView = new RowView(context, squares.subList(18, 27),squareClickListener);
            rowView.setLayoutParams(layoutParams);
            addView(rowView);

        } else {
            //Landscape
            RowView rowView = new RowView(context, squares.subList(0, 13),squareClickListener);
            rowView.setLayoutParams(layoutParams);
            addView(rowView);
            rowView = new RowView(context, squares.subList(13, 26),squareClickListener);
            rowView.setLayoutParams(layoutParams);
            addView(rowView);
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
