/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.Square;

import static au.com.shawware.kenken.model.Cage.DIVIDE;
import static au.com.shawware.kenken.model.Cage.EQUALS;
import static au.com.shawware.kenken.model.Cage.MINUS;
import static au.com.shawware.kenken.model.Cage.PLUS;
import static au.com.shawware.kenken.model.Cage.TIMES;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test the {@link FreebiesRule}.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class FreebiesRuleTest
{
    private static final int GRID_SIZE = 3;

    @Test
    public void testNoFreebies()
    {
        final List<Cage> cages = new ArrayList<>();

        cages.add(buildCage(PLUS,   5, new int[][] {{0,0},{0,1},{0,2}}));
        cages.add(buildCage(DIVIDE, 2, new int[][] {{1,0},{1,1}}));
        cages.add(buildCage(MINUS,  5, new int[][] {{2,0},{2,1}}));
        cages.add(buildCage(TIMES,  8, new int[][] {{1,2},{2,2}}));

        final GridState gridState = new GridState(GRID_SIZE, cages);

        final ISolvingRule rule = new FreebiesRule(GRID_SIZE, cages);

        assertTrue(rule.exhausted());
        assertFalse(rule.applyTo(gridState));
    }

    @Test
    @SuppressWarnings("boxing")
    public void testFreebies()
    {
        final List<Cage> cages = new ArrayList<>();

        cages.add(buildCage(PLUS,   5, new int[][] {{0,0},{0,1},{0,2}}));
        cages.add(buildCage(DIVIDE, 2, new int[][] {{1,0},{1,1}}));
        cages.add(buildCage(MINUS,  5, new int[][] {{2,0},{2,1}}));
        cages.add(buildCage(EQUALS, 1, new int[][] {{2,2}}));
        cages.add(buildCage(EQUALS, 3, new int[][] {{1,2}}));

        final GridState gridState = new GridState(GRID_SIZE, cages);

        final ISolvingRule rule = new FreebiesRule(GRID_SIZE, cages);

        assertFalse(rule.exhausted());
        assertTrue(rule.applyTo(gridState));
        assertTrue(rule.exhausted());
        assertFalse(rule.applyTo(gridState));

        assertThat(gridState.getValues(new Square(2, 2)), equalTo(Collections.singletonList(1)));
        assertThat(gridState.getValues(new Square(1, 2)), equalTo(Collections.singletonList(3)));
    }

    
    private Cage buildCage(String operation, int value, int[][] coords)
    {
        List<Square> squares = buildSquares(coords);
        return new Cage(operation, value, squares);
    }
    
    @SuppressWarnings("static-method")
    private List<Square> buildSquares(int[][] coords)
    {
        List<Square> squares = new ArrayList<>();
        for (int[] pair : coords)
        {
            squares.add(new Square(pair[0], pair[1]));
        }
        return squares;
    }
}
