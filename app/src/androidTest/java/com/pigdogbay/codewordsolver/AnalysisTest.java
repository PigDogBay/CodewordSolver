package com.pigdogbay.codewordsolver;

import android.support.test.runner.AndroidJUnit4;

import com.pigdogbay.codewordsolver.model.Analysis;
import com.pigdogbay.codewordsolver.model.Query;
import com.pigdogbay.codewordsolver.model.Square;
import com.pigdogbay.codewordsolver.model.SquareSet;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class AnalysisTest {

    @Test
    public void findCommonLetters1(){

        String[] list = {"marky","matty","davey"};
        Analysis analysis = new Analysis();
        List<Integer> common = analysis.findCommonLetters(Arrays.asList(list));
        assertThat(common.get(0), is(1));
        assertThat(common.get(1), is(4));
    }
    @Test
    public void findCommonLetters2(){

        String[] list = {"mark","matt","leon"};
        Analysis analysis = new Analysis();
        List<Integer> common = analysis.findCommonLetters(Arrays.asList(list));
        assertThat(common.size(), is(0));
    }

    /**
     * 1 result
     */
    @Test
    public void analyzeResults1(){
        SquareSet squareSet = new SquareSet();
        squareSet.setLetter(23,"A");
        squareSet.setLetter(7,"B");
        Query query = new Query();
        query.add(squareSet.getSquare(12));
        query.add(squareSet.getSquare(5));
        query.add(squareSet.getSquare(13));
        query.add(squareSet.getSquare(15));
        query.add(squareSet.getSquare(5));
        query.add(squareSet.getSquare(13));
        query.add(squareSet.getSquare(23));
        query.add(squareSet.getSquare(12));
        query.add(squareSet.getSquare(15));
        Analysis analysis = new Analysis();
        analysis.setSquareSet(squareSet);
        analysis.setQuery(query);
        String[] results = {"nurturant"};
        List<Square> newSquares = analysis.analyzeResults(Arrays.asList(results));
        assertThat(newSquares.size(),is(4));
        assertThat(newSquares.get(0).getLetter(),is("N"));
        assertThat(newSquares.get(1).getLetter(),is("U"));
        assertThat(newSquares.get(2).getLetter(),is("R"));
        assertThat(newSquares.get(3).getLetter(),is("T"));
        assertThat(newSquares.get(0).getNumber(),is(12));
        assertThat(newSquares.get(1).getNumber(),is(5));
        assertThat(newSquares.get(2).getNumber(),is(13));
        assertThat(newSquares.get(3).getNumber(),is(15));
    }
    /**
     * Multiple result
     */
    @Test
    public void analyzeResults2() {
        SquareSet squareSet = new SquareSet();
        squareSet.setLetter(23,"A");
        squareSet.setLetter(7,"B");
        Query query = new Query();
        query.add(squareSet.getSquare(11));
        query.add(squareSet.getSquare(14));
        query.add(squareSet.getSquare(14));
        query.add(squareSet.getSquare(24));
        query.add(squareSet.getSquare(9));
        query.add(squareSet.getSquare(14));
        query.add(squareSet.getSquare(19));
        query.add(squareSet.getSquare(19));
        Analysis analysis = new Analysis();
        analysis.setSquareSet(squareSet);
        analysis.setQuery(query);
        String[] results = {"deepness","heedless","meekness","needless","peerless","weedless"};
        List<Square> newSquares = analysis.analyzeResults(Arrays.asList(results));
        assertThat(newSquares.size(),is(2));
        assertThat(newSquares.get(0).getLetter(),is("E"));
        assertThat(newSquares.get(1).getLetter(),is("S"));
        assertThat(newSquares.get(0).getNumber(),is(14));
        assertThat(newSquares.get(1).getNumber(),is(19));
    }

}
