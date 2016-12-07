package com.pigdogbay.codewordsolver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.pigdogbay.codewordsolver.controllers.ResultsFragment;
import com.pigdogbay.codewordsolver.controllers.SquareAdapter;
import com.pigdogbay.codewordsolver.model.BackgroundTasks;
import com.pigdogbay.codewordsolver.model.MainModel;
import com.pigdogbay.codewordsolver.model.Query;
import com.pigdogbay.codewordsolver.model.Square;
import com.pigdogbay.codewordsolver.model.SquareSet;
import com.pigdogbay.codewordsolver.usercontrols.KeyboardView;
import com.pigdogbay.codewordsolver.usercontrols.LetterPickerDialog;
import com.pigdogbay.codewordsolver.usercontrols.SquareView;
import com.pigdogbay.codewordsolver.usercontrols.onSquareClickListener;
import com.pigdogbay.lib.utils.CodewordSolver;
import com.pigdogbay.lib.utils.ObservableProperty;

import java.util.List;

public class MainActivity extends AppCompatActivity implements onSquareClickListener, ObservableProperty.PropertyChangedObserver<BackgroundTasks.States> {

    private SquareAdapter squareAdapter;
    private RecyclerView recyclerView;
    private KeyboardView keyboardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Square> squares = MainModel.get().getSquareSet().getSquares();
        ViewGroup container = (ViewGroup) findViewById(R.id.keyboard_container);
        keyboardView = new KeyboardView(this,squares, this);
        container.addView(keyboardView);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        squareAdapter = new SquareAdapter(MainModel.get().getQuery().getSquares());
        recyclerView.setAdapter(squareAdapter);

        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        showResults();

    }

    private void search() {
        MainModel main = MainModel.get();
        BackgroundTasks backgroundTasks = main.getBackgroundTasks();
        //To Do check user has entered letters (use toast to tell user to type in squares)
        if (backgroundTasks.isReady()) {
            CodewordSolver codewordSolver = main.getCodewordSolver();
            codewordSolver.parse(main.getQuery().getPattern());
            codewordSolver.setFoundLetters(main.getSquareSet().getFoundLetters() );
            backgroundTasks.search(codewordSolver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BackgroundTasks backgroundTasks = MainModel.get().getBackgroundTasks();
        backgroundTasks.stateObservable.addObserver(this);
        modelToView(backgroundTasks.stateObservable.getValue());
    }

    @Override
    protected void onPause() {
        super.onPause();
        BackgroundTasks backgroundTasks = MainModel.get().getBackgroundTasks();
        backgroundTasks.stateObservable.removeObserver(this);
    }

    @Override
    public void onSquareClicked(SquareView squareView) {
        Square square = squareView.getSquare();
        Query query = MainModel.get().getQuery();
        if (square.getNumber()==Square.DELETE){
            query.delete();
        }
        else {
            query.add(square);
        }
        squareAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(query.getSquares().size()-1);
    }

    @Override
    public void onSquareLongClicked(SquareView squareView) {
        if (squareView.getSquare().getNumber()==Square.DELETE)
        {
            //clear all
            MainModel mainModel = MainModel.get();
            mainModel.getQuery().clear();
            squareAdapter.notifyDataSetChanged();
        } else {
            LetterPickerDialog letterPickerDialog = new LetterPickerDialog();
            letterPickerDialog.show(this, squareView);
        }
        //TO DO - refresh any squares in the query where the letter changes
        redrawQuery();
    }

    private void analyzeResults(){
        MainModel mainModel = MainModel.get();
        mainModel.getQuery().analyze(mainModel.getBackgroundTasks().wordMatches.getMatches());
        keyboardView.invalidate();
    }

    @Override
    public void update(ObservableProperty<BackgroundTasks.States> sender, BackgroundTasks.States update) {
        modelToView(update);
    }
    private void modelToView(BackgroundTasks.States state ){
        Log.v("mpdb", state.name());
        switch (state){
            case uninitialized:
                MainModel.get().getBackgroundTasks().loadWordLists(this,new int[]{R.raw.standard,R.raw.pro});
                break;
            case loading:
                break;
            case ready:
                break;
            case searching:
                break;
            case finished:
                break;
            case loadError:
                break;
        }
    }

    private void redrawQuery(){

    }

    private void replaceMainFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment f = manager.findFragmentById(R.id.main_fragment_container);
        manager
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment, tag)
                .commit();
    }
    private void showResults()
    {
        if (getSupportFragmentManager().findFragmentByTag(ResultsFragment.TAG)==null)
        {
            replaceMainFragment(new ResultsFragment(), ResultsFragment.TAG);
        }
    }

}
