package com.pigdogbay.codewordsolver;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.pigdogbay.codewordsolver.model.Query;
import com.pigdogbay.codewordsolver.model.Square;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by Mark on 06/12/2016.
 */
@RunWith(AndroidJUnit4.class)
public class QueryTest
{
    @Test
    public void getPattern1(){
        Query query = new Query();
        query.add(new Square(12,""));
        query.add(new Square(5,""));
        query.add(new Square(13,""));
        query.add(new Square(15,""));
        query.add(new Square(5,""));
        query.add(new Square(13,""));
        query.add(new Square(23,"A"));
        query.add(new Square(12,""));
        query.add(new Square(15,""));
        String pattern = query.getPattern();
        assertThat(pattern,is("213413a24"));

    }



}
