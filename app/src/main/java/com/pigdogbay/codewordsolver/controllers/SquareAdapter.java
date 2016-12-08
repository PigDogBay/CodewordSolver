package com.pigdogbay.codewordsolver.controllers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.pigdogbay.codewordsolver.model.Square;
import com.pigdogbay.codewordsolver.usercontrols.SquareView;

import java.util.List;

/**
 * Created by Mark on 06/12/2016.
 * Adapter for a recycleview to hold a horizontal list of squares as entered by the user
 *
 */
public class SquareAdapter extends RecyclerView.Adapter<SquareAdapter.ViewHolder> {

    private List<Square> squares;

    public SquareAdapter(List<Square> squares){
        this.squares = squares;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SquareView squareView = new SquareView(parent.getContext(),null);
        squareView.setShowQuestionMarkIfEmpty(true);
        return new ViewHolder(squareView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(squares.get(position));
    }

    @Override
    public int getItemCount() {
        return squares.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final SquareView squareView;

        ViewHolder(SquareView itemView) {
            super(itemView);
            this.squareView = itemView;
        }

        void bindItem(Square square){
            squareView.setSquare(square);
            //squareView.invalidate();
        }

        @Override
        public String toString() {
            return super.toString()+" '" +squareView.getSquare().getNumberString() + " "+ squareView.getSquare().getLetter()+"'";
        }
    }
}