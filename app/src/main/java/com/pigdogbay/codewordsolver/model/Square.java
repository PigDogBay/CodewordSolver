package com.pigdogbay.codewordsolver.model;

/**
 * Created by Mark on 05/12/2016.
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

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        this.numberString = String.valueOf(number);
    }

    public String getNumberString() {
        return numberString;
    }
}
