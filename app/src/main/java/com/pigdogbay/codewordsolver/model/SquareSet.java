package com.pigdogbay.codewordsolver.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Mark on 05/12/2016.
 * 26 Squares for each letter of the alphabet
 */
public class SquareSet {
    private Square[] set = new Square[27];

    public static String[] ALPHABET = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public SquareSet() {
        for (int i = 0; i < 26; i++) {
            set[i] = new Square(i + 1, "");
        }
        //delete square
        set[26] = new Square(Square.DELETE, "");
    }

    public void reset() {
        for (Square s : set) {
            s.setLetter("");
        }
    }

    public void setLetter(int number, String letter) {
        set[number - 1].setLetter(letter);
    }

    public String getLetter(int number) {
        return set[number - 1].getLetter();
    }

    public List<Square> getSquares() {
        return Arrays.asList(set);
    }

    public Square getSquare(int number) {
        return set[number - 1];
    }

    public String getFoundLetters() {
        String found = "";
        for (Square s : set) {
            found = found + s.getLetter();
        }
        return found.toLowerCase();
    }

    public boolean contains(String letter) {
        for (Square s : set) {
            if (s.getLetter().equals(letter)) {
                return true;
            }
        }
        return false;
    }

    public String addNewSquares(List<Square> newSquares) {
        String added="";
        for (Square square : newSquares) {
            if (!contains(square.getLetter())) {
                added = added + square.getLetter();
                getSquare(square.getNumber()).setLetter(square.getLetter());
            }
        }
        return added;
    }

    public String flatten() {
        String flat = "";
        for (Square s : getSquares()) {
            flat = flat + s.getLetter() + ",";
        }
        return flat;
    }

    public void unflatten(String flat) {
        if (flat == null || "".equals(flat)) {
            return;
        }
        reset();
        String[] letters = flat.trim().split(",");
        for (int i = 0; i < letters.length; i++) {
            set[i].setLetter(letters[i]);
        }
    }
}
