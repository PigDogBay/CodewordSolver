package com.pigdogbay.codewordsolver.usercontrols;

/**
 * Created by Mark on 05/12/2016.
 * Callback for when user clicks on a square
 */
public interface onSquareClickListener {
    void onSquareClicked(SquareView squareView);
    void onSquareLongClicked(SquareView squareView);

}
