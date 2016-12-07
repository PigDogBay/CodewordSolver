package com.pigdogbay.codewordsolver;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.pigdogbay.codewordsolver.model.Analysis;
import com.pigdogbay.codewordsolver.model.Query;
import com.pigdogbay.codewordsolver.model.Square;
import com.pigdogbay.codewordsolver.model.SquareSet;
import com.pigdogbay.lib.utils.CodewordSolver;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

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

    @Test
    public void analyzeResults2(){
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


    }

}
