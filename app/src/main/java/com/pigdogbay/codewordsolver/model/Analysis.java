package com.pigdogbay.codewordsolver.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 07/12/2016.
 * Checks over the results to see if any new letters have been
 */
public class Analysis {

    private SquareSet squareSet;
    private Query query;

    public SquareSet getSquareSet() {
        return squareSet;
    }
    public void setSquareSet(SquareSet squareSet) {
        this.squareSet = squareSet;
    }
    public Query getQuery() {
        return query;
    }
    public void setQuery(Query query) {
        this.query = query;
    }

    public Analysis(){

    }

    public List<Integer> findCommonLetters(List<String> results){
        List<Integer> commonIndices = new ArrayList<>();
        int len = results.get(0).length();
        for (int i=0; i<len; i++){
            if (sameChar(results,i)){
                commonIndices.add(i);
            }
        }
        return commonIndices;
    }

    private boolean sameChar(List<String> results, int index){
        char c = results.get(0).charAt(index);
        for (String s : results){
            if (s.charAt(index)!=c){
                return false;
            }
        }
        return true;
    }

    public List<Square> analyzeResults(List<String> results) {
        List<Square> newSquares = new ArrayList<>();
        if (results.size()==1)
        {
            String firstResult = results.get(0).toUpperCase();
            //add all new letters
            for (int i=0; i<query.getSquares().size();i++){
                Square square = query.getSquares().get(i);
                if (square.getLetter().equals("")){
                    //found a new letter
                    String newLetter = String.valueOf(firstResult.charAt(i));
                    //don't really need this check, but just incase!
                    //but makesure not already in new squares
                    if (!squareSet.contains(newLetter) && !contains(newSquares, newLetter)) {
                        newSquares.add(new Square(square.getNumber(), newLetter));
                    }
                }
            }
        } else if (results.size()<50){
            //look for any common letters
            List<Integer> commonLetters = findCommonLetters(results);
            String firstResult = results.get(0).toUpperCase();
            if (commonLetters.size()>0){
                for (int index : commonLetters){
                    Square square = query.getSquares().get(index);
                    if (square.getLetter().equals("")) {
                        String newLetter = String.valueOf(firstResult.charAt(index));
                        if (!squareSet.contains(newLetter) && !contains(newSquares, newLetter)) {
                            newSquares.add(new Square(square.getNumber(), newLetter));
                        }
                    }
                }
            }
        }
        return newSquares;
    }

    private boolean contains(List<Square> squares, String letter){
        for (Square s : squares){
            if (s.getLetter().equals(letter)){
                return true;
            }
        }
        return false;
    }

}
