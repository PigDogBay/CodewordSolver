package com.pigdogbay.codewordsolver.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Mark on 05/12/2016.
 * 26 Squares for each letter of the alphabet
 */
public class SquareSet
{
    private Square[] set = new Square[27];

    public SquareSet(){
        for (int i = 0; i<26; i++){
            set[i] = new Square(i+1,"");
        }
        //delete square
        set[26] = new Square(Square.DELETE,"");
    }

    public void reset(){
        for (Square s : set){
            s.setLetter("");
        }
    }

    public void setLetter(int number, String letter){
        set[number-1].setLetter(letter);
    }

    public String getLetter(int number){
        return set[number-1].getLetter();
    }

    public List<Square> getSquares(){
        return Arrays.asList(set);
    }

    public Square getSquare(int number){
        return set[number-1];
    }

    public String getFoundLetters(){
        String found = "";
        for (Square s : set){
            found = found+s.getLetter();
        }
        return found.toLowerCase();
    }

    boolean contains(String letter){
        for (Square s : set){
            if (s.getLetter().equals(letter)){
                return true;
            }
        }
        return false;
    }

   public void addNewSquares(List<Square> newSquares)
   {
       for (Square square : newSquares){
           getSquare(square.getNumber()).setLetter(square.getLetter());
       }
   }
}
