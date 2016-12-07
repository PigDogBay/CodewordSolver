package com.pigdogbay.codewordsolver.controllers;

import android.view.View;

/**
 * Created by Mark on 07/12/2016.
 * User can interact with matches
 */
public interface MatchListener {
    void onIconClicked(View v, String word);
    void onLongClick(View v, String word);
}
