package com.pigdogbay.codewordsolver.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 06/12/2016.
 * Represents the squares entered by the user
 */
public class Query {

    private static final int MAX_QUERY_LENGTH = 15;
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

    public boolean containsLetters(){
        for(Square s : squares){
            if (!s.getLetter().equals("")){
                return true;
            }
        }
        return false;
    }

    public List<Square> createNewSquares(String word) {
        List<Square> newSquares = new ArrayList<>();
        for (int i=0; i<word.length();i++){
            newSquares.add(
                    new Square(
                            squares.get(i).getNumber(),
                            word.charAt(i)
                    ));
        }
        return newSquares;
    }

    public enum Valid{
        OK,
        EMPTY,
        TOO_LONG
    }

    public Valid validate(){
        int len = squares.size();
        if (len == 0) {return Valid.EMPTY;}
        if (len> MAX_QUERY_LENGTH) {return Valid.TOO_LONG;}
        return Valid.OK;
    }

}
