package com.pigdogbay.codewordsolver.controllers;


import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pigdogbay.codewordsolver.R;
import com.pigdogbay.codewordsolver.model.BackgroundTasks;
import com.pigdogbay.codewordsolver.model.MainModel;
import com.pigdogbay.lib.utils.ActivityUtils;
import com.pigdogbay.lib.utils.ObservableProperty;
import com.pigdogbay.lib.utils.WordMatches;
import com.pigdogbay.lib.utils.WordSolver;

import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultsFragment extends Fragment implements MatchListener, PopupMenu.OnMenuItemClickListener {

    public static final String TAG = "results";

    private MatchesAdapter _ItemAdapter;
    private TextView _TextStatus;
    private ObservableProperty.PropertyChangedObserver<BackgroundTasks.States> stateChangeListener;
    private ObservableProperty.PropertyChangedObserver<String> matchFoundListener;
    private RecyclerView recyclerView;
    private String longPressWord = "";

    public ResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);

        WordMatches wordMatches = MainModel.get().getBackgroundTasks().wordMatches;

        _TextStatus = (TextView) rootView.findViewById(R.id.resultsTextStatus);

        _ItemAdapter = new MatchesAdapter(wordMatches,this);
        _ItemAdapter.setTextSize(MatchesAdapter.TextSize.LARGE);

        this.stateChangeListener = new ObservableProperty.PropertyChangedObserver<BackgroundTasks.States>() {
            @Override
            public void update(ObservableProperty<BackgroundTasks.States> sender, final BackgroundTasks.States update) {
                modelToView(update);
            }
        };
        this.matchFoundListener =  new ObservableProperty.PropertyChangedObserver<String>() {
            @Override
            public void update(ObservableProperty<String> sender, final String update) {
                //force table to update
                _ItemAdapter.notifyDataSetChanged();
            }
        };
        recyclerView = (RecyclerView) rootView.findViewById(R.id.results_matches_collection);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(_ItemAdapter);
        return rootView;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        BackgroundTasks backgroundTasks = MainModel.get().getBackgroundTasks();
        backgroundTasks.stateObservable.addObserver(this.stateChangeListener);
        backgroundTasks.matchObservable.addObserver(this.matchFoundListener);
        modelToView(backgroundTasks.stateObservable.getValue());
    }
    @Override
    public void onPause()
    {
        super.onPause();
        BackgroundTasks backgroundTasks = MainModel.get().getBackgroundTasks();
        backgroundTasks.stateObservable.removeObserver(this.stateChangeListener);
        backgroundTasks.matchObservable.removeObserver(this.matchFoundListener);
    }

    @Override
    public void onIconClicked(View v, String word) {

    }

    @Override
    public void onLongClick(View view, String word) {

        longPressWord = word;
        PopupMenu popupMenu = new PopupMenu(this.getContext(),view);
        popupMenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_result_popup,popupMenu.getMenu());
        popupMenu.show();
    }

    private void modelToView(BackgroundTasks.States state)
    {
        _ItemAdapter.notifyDataSetChanged();
        switch (state)
        {
            case finished:
                WordMatches wordMatches = MainModel.get().getBackgroundTasks().wordMatches;
                String matches = String.format(Locale.US,"%s %d",getString(R.string.results_status_matches_found),wordMatches.getCount());
                _TextStatus.setText(matches);
                break;
            case loadError:
                break;
            case loading:
                break;
            case ready:
                _TextStatus.setText("");
                break;
            case searching:
                _TextStatus.setText(getString(R.string.results_status_searching));
                break;
            case uninitialized:
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_result_copy:
                copyToClipboard(longPressWord);
                return true;
            case R.id.menu_result_dictionary_com:
                String url = WordSolver.getDictionaryCom(longPressWord);
                showWebpage(url);
                return true;
            case R.id.menu_result_google:
                url = WordSolver.getWordURL(longPressWord);
                showWebpage(url);
                return true;
            case R.id.menu_result_merriam_webster:
                url = WordSolver.getMerriamWebsterURL(longPressWord);
                showWebpage(url);
                return true;
            case R.id.menu_result_wikipedia:
                url = WordSolver.getWikipediaURL(longPressWord);
                showWebpage(url);
                return true;
            case R.id.menu_result_livio_app:
                sendToLivioApp(longPressWord);
                return true;
        }
        return false;
    }

    private void copyToClipboard(String text){
        ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(getString(R.string.app_name),text);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(getContext(),"Copied to clipboard",Toast.LENGTH_SHORT).show();
    }

    private void showWebpage(String url){
        try
        {
            ActivityUtils.ShowWebPage(getActivity(), url);
        } catch (Exception e){e.printStackTrace();}

    }
    private boolean isIntentAvailable(Context context, Intent intent) {
        List<ResolveInfo> lri = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return (lri != null) && (lri.size() > 0);
    }

    private void sendToLivioApp(String word){
        try {
            //Try https://play.google.com/store/apps/details?id=livio.pack.lang.en_US&hl=en_GB
            //See http://thesaurus.altervista.org/dictionary-android
            Intent intent = new Intent(Intent.ACTION_SEARCH);
            intent.setPackage("livio.pack.lang.en_US");
            intent.putExtra(SearchManager.QUERY, word);
            if (isIntentAvailable(getActivity(), intent))
                startActivity(intent);
            else {
                //Take user to the app on the playstore
                Toast.makeText(getContext(),"App not installed",Toast.LENGTH_LONG).show();
                ActivityUtils.ShowAppOnMarketPlace(getActivity(), R.string.market_livio_dictionary_app);
            }
        }catch(Exception e){e.printStackTrace();}

    }
}
