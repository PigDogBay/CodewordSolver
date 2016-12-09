package com.pigdogbay.codewordsolver;

import android.support.test.runner.AndroidJUnit4;

import com.pigdogbay.codewordsolver.model.SquareSet;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class SquareSetTest {

    @Test
    public void flatten1(){
        SquareSet squareSet = new SquareSet();
        String flat = squareSet.flatten();
        assertThat(flat,is(",,,,,,,,,,,,,,,,,,,,,,,,,,,"));
    }
    @Test
    public void flatten2(){
        SquareSet squareSet = new SquareSet();
        squareSet.setLetter(5,"A");
        squareSet.setLetter(1,"C");
        squareSet.setLetter(26,"E");

        String flat = squareSet.flatten();
        assertThat(flat,is("C,,,,A,,,,,,,,,,,,,,,,,,,,,E,,"));
    }
    @Test
    public void unflatten1(){
        SquareSet squareSet = new SquareSet();
        squareSet.unflatten("C,,,,A,,,,,,,,,,,,,,,,,,,,,E,,");
        assertThat(squareSet.getLetter(1),is("C"));
        assertThat(squareSet.getLetter(5),is("A"));
        assertThat(squareSet.getLetter(26),is("E"));

        squareSet.unflatten(",,,,,,,,,,,,,,,,,,,,,,,,,,");
        assertThat(squareSet.getLetter(1),is(""));
        assertThat(squareSet.getLetter(5),is(""));
        assertThat(squareSet.getLetter(26),is(""));

        assertThat(squareSet.getSquare(27).getLetter(),is(""));
    }
    /*
     Ignore empty strings
     */
    @Test
    public void unflatten2() {
        SquareSet squareSet = new SquareSet();
        squareSet.setLetter(5,"A");
        squareSet.setLetter(1,"C");
        squareSet.setLetter(26,"E");
        squareSet.unflatten("");
        assertThat(squareSet.getLetter(1),is("C"));
        assertThat(squareSet.getLetter(5),is("A"));
        assertThat(squareSet.getLetter(26),is("E"));
    }
    /*
     Ignore empty strings
     */
    @Test
    public void unflatten3() {
        SquareSet squareSet = new SquareSet();
        squareSet.setLetter(5,"A");
        squareSet.setLetter(1,"C");
        squareSet.setLetter(26,"E");
        squareSet.unflatten(null);
        assertThat(squareSet.getLetter(1),is("C"));
        assertThat(squareSet.getLetter(5),is("A"));
        assertThat(squareSet.getLetter(26),is("E"));
    }

}
