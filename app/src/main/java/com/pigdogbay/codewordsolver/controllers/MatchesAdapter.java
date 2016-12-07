package com.pigdogbay.codewordsolver.controllers;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pigdogbay.codewordsolver.R;
import com.pigdogbay.lib.utils.WordMatches;

/**
 * Created by Mark on 07/12/2016.
 * Shows word macthes
 */
public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder>
{
    public enum TextSize
    {
        SMALL,
        MEDIUM,
        LARGE,
        XLARGE
    }
    private final WordMatches wordMatches;
    private final MatchListener listener;
    private int layoutId;

    public void setTextSize(TextSize textSize) {
        switch (textSize){
            case SMALL:
                this.layoutId = R.layout.match_list_item_small;
                break;
            case MEDIUM:
                this.layoutId = R.layout.match_list_item_medium;
                break;
            case XLARGE:
                this.layoutId = R.layout.match_list_item_xlarge;
                break;
            default:
                this.layoutId = R.layout.match_list_item;
                break;
        }
    }

    public MatchesAdapter(WordMatches wordMatches, MatchListener listener) {
        this.wordMatches = wordMatches;
        this.listener = listener;
        setTextSize(TextSize.LARGE);
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        return new MatchViewHolder(view,listener) ;
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        final String word = wordMatches.getWord(position);
        final String formattedWord = wordMatches.getFormattedWord(word);
        holder.bindItem(word, formattedWord);

    }

    @Override
    public int getItemCount() {
        return wordMatches.getCount();
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView matchText;
        private final MatchListener listener;
        private String word="";

        public MatchViewHolder(View itemView, MatchListener listener) {
            super(itemView);
            this.listener = listener;
            itemView.setOnLongClickListener(this);
            matchText = (TextView) itemView.findViewById(R.id.match_list_item_text);
            itemView.findViewById(R.id.match_list_item_info_btn).setOnClickListener(this);
        }

        public void bindItem(String word, String formattedWord){
            this.word = word;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                matchText.setText(Html.fromHtml(formattedWord, Html.FROM_HTML_MODE_LEGACY),TextView.BufferType.SPANNABLE);
            } else {
                matchText.setText(Html.fromHtml(formattedWord),TextView.BufferType.SPANNABLE);
            }
        }

        @Override
        public void onClick(View view) {
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            listener.onIconClicked(view, word);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onLongClick(view, word);
            return true;
        }

        @Override
        public String toString() {
            return super.toString()+" '"+word+"'";
        }
    }

}
