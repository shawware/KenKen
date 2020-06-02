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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.Square;

import static au.com.shawware.kenken.model.Cage.DIVIDE;
import static au.com.shawware.kenken.model.Cage.EQUALS;
import static au.com.shawware.kenken.model.Cage.MINUS;
import static au.com.shawware.kenken.model.Cage.PLUS;
import static org.junit.Assert.assertEquals;
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

    private List<Cage> cages;
    private SquareState[][] gridState;

    @Before
    public void setUp()
    {
        createGridState(GRID_SIZE);

        cages = new ArrayList<>();

        cages.add(buildCage(GRID_SIZE, PLUS,   5, new int[][] {{0,0},{0,1}}));
        cages.add(buildCage(GRID_SIZE, DIVIDE, 2, new int[][] {{1,0},{1,1}}));
        cages.add(buildCage(GRID_SIZE, MINUS,  5, new int[][] {{2,0},{2,1}}));
    }

    @Test
    public void testNoFreebies()
    {
        ISolvingRule rule = new FreebiesRule(GRID_SIZE, cages);

        assertTrue(rule.exhausted());
        assertFalse(rule.applyTo(gridState));
    }

    @Test
    public void testFreebies()
    {
        cages.add(buildCage(GRID_SIZE, EQUALS, 1, new int[][] {{2,2}}));
        cages.add(buildCage(GRID_SIZE, EQUALS, 3, new int[][] {{1,2}}));

        ISolvingRule rule = new FreebiesRule(GRID_SIZE, cages);

        assertFalse(rule.exhausted());
        assertTrue(rule.applyTo(gridState));
        assertTrue(rule.exhausted());
        assertFalse(rule.applyTo(gridState));

        assertListsEqual(Collections.singletonList(1), gridState[2][2].getValues());
        assertListsEqual(Collections.singletonList(3), gridState[1][2].getValues());
    }

    
    private void createGridState(int gridSize)
    {
        gridState = new SquareState[gridSize][gridSize];
    }
    
    private Cage buildCage(int gridSize, String operation, int value, int[][] coords)
    {
        List<Square> squares = buildSquares(coords);
        // Fake the initial grid state for the cage's squares.
        squares.forEach(square -> gridState[square.getX()][square.getY()] = new SquareState(gridSize, square));
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

    @SafeVarargs
    private final void verifyResult(List<Set<Integer>> actualValues, Set<Integer>... expectedValues)
    {
        assertEquals(expectedValues.length, actualValues.size());
        for (int i = 0; i < expectedValues.length; i++)
        {
            Set<Integer> expected = expectedValues[i];
            Set<Integer> actual = actualValues.get(i);
            assertEquals(expected.size(), actual.size());
            expected.forEach(v -> assertTrue(actual.contains(v)));
        }
    }

    private void assertListsEqual(List<Integer> expected, List<Integer> actual)
    {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++)
        {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    private void assertSetsEqual(Set<Integer> expected, Set<Integer> actual)
    {
        assertEquals(expected.size(), actual.size());
        expected.forEach(v -> assertTrue(actual.contains(v)));
    }

    private Set<Integer> setOf(int... values)
    {
        return IntStream.of(values).boxed().collect(Collectors.toSet());
    }
}
