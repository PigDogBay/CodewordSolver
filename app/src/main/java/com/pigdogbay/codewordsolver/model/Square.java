package com.pigdogbay.codewordsolver.model;

/**
 * Created by Mark on 05/12/2016.
 * Data model representing a square on a codeword grid
 */
public class Square
{
    public static final int DELETE = 100;

    private int number;
    private String letter, numberString;

    public Square(int number, String letter){
        setNumber(number);
        setLetter(letter);
    }
    public Square(int number, char letter){
        setNumber(number);
        setLetter(String.valueOf(letter));
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public int getNumber() {
        return number;
    }

    private void setNumber(int number) {
        this.number = number;
        this.numberString = String.valueOf(number);
    }

    public String getNumberString() {
        return numberString;
    }
}
