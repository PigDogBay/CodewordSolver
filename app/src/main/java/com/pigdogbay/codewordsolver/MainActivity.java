package com.pigdogbay.codewordsolver;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pigdogbay.codewordsolver.controllers.HelpFragment;
import com.pigdogbay.codewordsolver.controllers.ResultsFragment;
import com.pigdogbay.codewordsolver.controllers.SquareAdapter;
import com.pigdogbay.codewordsolver.model.Analysis;
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
import com.pigdogbay.lib.utils.PreferencesHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity implements onSquareClickListener, ObservableProperty.PropertyChangedObserver<BackgroundTasks.States>, HelpFragment.OnFragmentInteractionListener {

    private SquareAdapter squareAdapter;
    private RecyclerView recyclerView;
    private KeyboardView keyboardView;
    //Need to clone the query before a search, as user could edit the query afterwards and then press +
    private Query queryCopy;
    private TextView searchHintText;

    //Model getters
    private Query getQuery() {
        return MainModel.get().getQuery();
    }
    private SquareSet getSquareSet() {
        return MainModel.get().getSquareSet();
    }
    private BackgroundTasks getBackgroundTasks() {
        return MainModel.get().getBackgroundTasks();
    }
    private List<String> getResults() {
        return getBackgroundTasks().wordMatches.getMatches();
    }
    private Analysis getAnalysis() {
        return MainModel.get().getAnalysis();
    }
    private CodewordSolver getCodewordSolver() {
        return MainModel.get().getCodewordSolver();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Square> squares = getSquareSet().getSquares();
        ViewGroup container = (ViewGroup) findViewById(R.id.keyboard_container);
        keyboardView = new KeyboardView(this, squares, this);
        container.addView(keyboardView);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        squareAdapter = new SquareAdapter(getQuery().getSquares());
        recyclerView.setAdapter(squareAdapter);
        searchHintText = (TextView) findViewById(R.id.search_hint_text);
        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        Fragment fragment =getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);
        if (fragment==null){
            showTips();
        }
        checkAppRate();
    }
    private void checkAppRate() {
        try {
            new com.pigdogbay.lib.apprate.AppRate(this)
                    .setMinDaysUntilPrompt(7).setMinLaunchesUntilPrompt(5).init();
        }catch (Exception e){e.printStackTrace();}
    }

    private void search() {
        //To Do check user has entered letters (use toast to tell user to type in squares)
        switch (getQuery().validate()) {

            case OK:
                break;
            case EMPTY:
                Toast.makeText(this, "Use keyboard to enter squares", Toast.LENGTH_LONG).show();
                return;
            case TOO_LONG:
                Toast.makeText(this, "Too many squares, hold backspace to clear", Toast.LENGTH_LONG).show();
                return;
        }
        if (getBackgroundTasks().isReady()) {
            //clone the query incase user needs to add a word to the letters
            queryCopy = getQuery().copy();
            CodewordSolver codewordSolver = getCodewordSolver();
            codewordSolver.parse(getQuery().getPattern());
            codewordSolver.setFoundLetters(getSquareSet().getFoundLetters());
            showResults();
            getBackgroundTasks().search(codewordSolver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBackgroundTasks().stateObservable.addObserver(this);
        modelToView(getBackgroundTasks().stateObservable.getValue());

        //Restore letters
        PreferencesHelper preferencesHelper = new PreferencesHelper(this);
        String flat = preferencesHelper.getString(R.string.pref_key_found_letters,"");
        getSquareSet().unflatten(flat);

        int hintVisibility = getQuery().getCount()==0 ? View.VISIBLE : View.INVISIBLE;
        searchHintText.setVisibility(hintVisibility);

    }

    @Override
    protected void onPause() {
        super.onPause();
        getBackgroundTasks().stateObservable.removeObserver(this);

        //Store letters
        PreferencesHelper preferencesHelper = new PreferencesHelper(this);
        String flat = getSquareSet().flatten();
        preferencesHelper.setString(R.string.pref_key_found_letters,flat);
    }
    @Override
    public void onBackPressed() {
        Fragment f= getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);
        String tag="";
        if (f!=null){
            tag = f.getTag();
        }
        if (HelpFragment.TAG.equals(tag))
        {
            super.onBackPressed();
        }
        else
        {
            showTips();
        }

    }

    @Override
    public void onSquareClicked(SquareView squareView) {
        Square square = squareView.getSquare();
        Query query = getQuery();
        if (square.getNumber() == Square.DELETE) {
            query.delete();
        } else {
            query.add(square);
        }
        int hintVisibility = query.getCount()==0 ? View.VISIBLE : View.INVISIBLE;
        searchHintText.setVisibility(hintVisibility);
        squareAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(query.getSquares().size() - 1);
    }

    @Override
    public void onSquareLongClicked(SquareView squareView) {
        if (squareView.getSquare().getNumber() == Square.DELETE) {
            //clear all
            getQuery().clear();
            searchHintText.setVisibility(View.VISIBLE);
            squareAdapter.notifyDataSetChanged();
        } else {
            LetterPickerDialog letterPickerDialog = new LetterPickerDialog();
            letterPickerDialog.show(this, squareView,
                    new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {                            squareAdapter.notifyDataSetChanged();                        }
                    },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            reset();
                        }
                    });
        }
    }

    @Override
    public void update(ObservableProperty<BackgroundTasks.States> sender, BackgroundTasks.States update) {
        modelToView(update);
    }

    private void modelToView(BackgroundTasks.States state) {

        switch (state) {
            case uninitialized:
                getBackgroundTasks().loadWordLists(this, new int[]{R.raw.standard, R.raw.pro});
                break;
            case loading:
                break;
            case ready:
                break;
            case searching:
                break;
            case analyzing:
                analyzing();
                getBackgroundTasks().analysisComplete();
                break;
            case finished:
                break;
            case loadError:
                break;
        }
    }

    private void analyzing() {
        List<String> results = getBackgroundTasks().wordMatches.getMatches();
        if (results.size() == 0) {

            //issue warning to check query/check found letters
            if (getQuery().containsLetters()) {
                Toast.makeText(this, "Check your squares and letters", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Check your squares", Toast.LENGTH_LONG).show();
            }

            return;
        }
        analyzeResults(results);
    }

    private void analyzeResults(List<String> results) {
        Analysis analysis = getAnalysis();
        List<Square> newSquares = analysis.analyzeResults(results);
        if (newSquares.size() > 0) {
            String newLetters = "Adding ";
            for (Square s : newSquares) {
                newLetters = newLetters + s.getLetter();
            }
            Toast.makeText(this, newLetters, Toast.LENGTH_LONG).show();
            //ask user to add new squares
            //yes update query/squareset/keyboard
            getSquareSet().addNewSquares(newSquares);
            keyboardView.invalidate();
            squareAdapter.notifyDataSetChanged();
        }
    }

    private void replaceMainFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment f = manager.findFragmentById(R.id.main_fragment_container);
        manager
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment, tag)
                .commit();
    }

    private void showResults() {
        if (getSupportFragmentManager().findFragmentByTag(ResultsFragment.TAG) == null) {
            replaceMainFragment(new ResultsFragment(), ResultsFragment.TAG);
        }
    }
    private void showTips() {
        if (getSupportFragmentManager().findFragmentByTag(HelpFragment.TAG) == null) {
            replaceMainFragment(new HelpFragment(), HelpFragment.TAG);
        }
    }

    public void addResult(String word) {
        word = word.toUpperCase();
        //use cloned searchQuery as user may have altered the query
        List<Square> newSquares = queryCopy.createNewSquares(word);
        getSquareSet().addNewSquares(newSquares);
        keyboardView.invalidate();
        squareAdapter.notifyDataSetChanged();
    }

    private void reset() {
        getQuery().clear();
        getSquareSet().reset();
        getBackgroundTasks().reset();
        keyboardView.invalidate();
        squareAdapter.notifyDataSetChanged();
        searchHintText.setVisibility(View.VISIBLE);
    }

    /**
     * Help Fragment Listener
     */
    @Override
    public void onReset() {
        reset();
    }
}
