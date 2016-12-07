package com.pigdogbay.codewordsolver.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 06/12/2016.
 * Represents the squares entered by the user
 */
public class Query {

    private List<Square> squares;
    private int[] frequency;
    private String[] patternChar;

    public List<Square> getSquares() {
        return squares;
    }

    public Query(){
        squares = new ArrayList<>();
        frequency = new int[26];
        patternChar = new String[26];
    }

    public void add(Square square){
        squares.add(square);
    }
    public void delete(){
        if (squares.size()>0){
            //remove last
            squares.remove(squares.size()-1);
        }
    }
    public void clear()
    {
        squares.clear();
    }


    private void createPatternChars(){
        //clear
        for (int i=0; i<frequency.length;i++){
            frequency[i]=0;
            patternChar[i] = ".";
        }
        for (Square s : squares){
            if (s.getLetter().equals("")) {
                int index = s.getNumber() - 1;
                frequency[index]++;
            } else {
                patternChar[s.getNumber() - 1] = s.getLetter();
            }
        }
        int repeatedSquare = 1;
        for (int i=0; i<frequency.length;i++){
            if (frequency[i]>1){
                patternChar[i] = String.valueOf(repeatedSquare);
                repeatedSquare++;
            }
        }

    }

    public String getPattern(){
        createPatternChars();
        String pattern = "";
        for(Square s : squares){
            pattern = pattern + patternChar[s.getNumber()-1];
        }
        return pattern.toLowerCase();
    }

    public void analyze(List<String> results){
        if (results.size()==1){
            String word = results.get(0);
            for (int i=0; i<word.length();i++){
                squares.get(i).setLetter(String.valueOf(word.charAt(i)).toUpperCase());
            }
        }

    }

}
