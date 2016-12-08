package com.pigdogbay.codewordsolver.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pigdogbay.lib.utils.CodewordSolver;
import com.pigdogbay.lib.utils.LineReader;
import com.pigdogbay.lib.utils.ObservableProperty;
import com.pigdogbay.lib.utils.StringUtils;
import com.pigdogbay.lib.utils.WordList;
import com.pigdogbay.lib.utils.WordListCallback;
import com.pigdogbay.lib.utils.WordMatches;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mark on 06/12/2016.
 * Handles the async tasks such as loading word lists and performing a search
 */
public class BackgroundTasks
{
    private static final int TABLE_MAX_COUNT_TO_RELOAD = 40;
    private static final int DEFAULT_RESULTS_LIMIT = 100;

    public enum States
    {
        uninitialized, loading, ready, searching, analyzing, finished, loadError
    }
    private List<WordList> wordLists;
    private String query;

    public ObservableProperty<States> stateObservable;
    public ObservableProperty<String> matchObservable;
    public WordMatches wordMatches;

    BackgroundTasks(){
        query = "";
        wordMatches = new WordMatches();

        stateObservable = new ObservableProperty<>(States.uninitialized);
        matchObservable = new ObservableProperty<>("");
        wordLists = new ArrayList<>();
    }

    public void loadWordLists(Context context,int[] resourceIds){
        wordLists.clear();
        for (int id : resourceIds){
            WordList wl = new WordList();
            wl.setResourceId(id);
            wl.SetResultLimit(DEFAULT_RESULTS_LIMIT);
            wordLists.add(wl);
        }
        new LoadTask(context).execute();
    }

    private class LoadTask extends AsyncTask<Void, Void, Void> {
        Context context;
        boolean isLoadError = false;

        private LoadTask(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            stateObservable.setValue(States.loading);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
                for (WordList wl : wordLists){
                    List<String> words = LineReader.Read(context, wl.getResourceId());
                    wl.SetWordList(words);
                }
            } catch (Exception e){isLoadError = true;}
            context=null;
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            stateObservable.setValue( isLoadError ? States.loadError : States.ready);
        }
    }
    public void analysisComplete(){
        if (States.analyzing == stateObservable.getValue())
        {
            stateObservable.setValue(States.finished);
        }
    }
    public boolean isReady()
    {
        if (States.finished == stateObservable.getValue())
        {
            stateObservable.setValueWithoutNotification(States.ready);
        }
        return States.ready == stateObservable.getValue();
    }

    public void search(CodewordSolver codewordSolver){
        new SearchTask().execute(codewordSolver);
    }
    private class SearchTask extends AsyncTask<CodewordSolver, String, Void> implements WordListCallback
    {
        @Override
        protected void onPreExecute() {
            //need to clear on UI thread otherwise recycler view will occassionally crash
            wordMatches.getMatches().clear();
            stateObservable.setValue(States.searching);
        }
        @Override
        protected Void doInBackground(CodewordSolver... codewordSolvers) {
            wordLists.get(0).reset();
            wordLists.get(0).findCodewords(codewordSolvers[0],this);
            if (wordMatches.getCount()==0){
                wordLists.get(1).reset();
                //no words found try the pro word list
                wordLists.get(1).findCodewords(codewordSolvers[0],this);
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(String... values) {
            //The array will be used in the views array adapter
            //this means the array must be updated only from the UI thread
            wordMatches.getMatches().add(values[0]);
            //only update the UI for the first page or so of results
            if (wordMatches.getMatches().size()<=TABLE_MAX_COUNT_TO_RELOAD)
            {
                matchObservable.setValue(values[0]);
            }
        }
        @Override
        protected void onPostExecute(Void result) {
            stateObservable.setValue(States.analyzing);
        }
        @Override
        public void Update(String result) {
            publishProgress(result);
        }
    }



}
