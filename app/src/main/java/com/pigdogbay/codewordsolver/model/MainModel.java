package com.pigdogbay.codewordsolver.model;

import com.pigdogbay.lib.utils.CodewordSolver;

/**
 * Created by Mark on 05/12/2016.
 * Application wide main model
 */
public class MainModel
{
    private static MainModel mainModel;

    private SquareSet squareSet;
    private Query query;
    private BackgroundTasks backgroundTasks;
    private CodewordSolver codewordSolver;

    public static MainModel get(){
        if (mainModel==null){
            mainModel = new MainModel();
        }
        return mainModel;
    }

    public SquareSet getSquareSet() {
        if (squareSet==null){
            squareSet = new SquareSet();
        }
        return squareSet;
    }

    public Query getQuery() {
        if (query==null){
            query = new Query();
        }
        return query;
    }

    public BackgroundTasks getBackgroundTasks() {
        if (backgroundTasks==null){
            backgroundTasks = new BackgroundTasks();
        }
        return backgroundTasks;
    }

    public CodewordSolver getCodewordSolver() {
        if (codewordSolver==null){
            codewordSolver = new CodewordSolver();
        }
        return codewordSolver;
    }
}
