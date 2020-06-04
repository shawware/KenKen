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

import org.junit.Test;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.Square;

import static au.com.shawware.kenken.model.Cage.DIVIDE;
import static au.com.shawware.kenken.model.Cage.MINUS;
import static au.com.shawware.kenken.model.Cage.PLUS;
import static au.com.shawware.kenken.model.Cage.TIMES;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Validate the algorithm for finding un-used square values.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@SuppressWarnings("static-method")
public class FindUnusedTest
{
    private static final int PAIR = 2;
    private static final int TRIPLE = 3;

    private static final int LINEAR = 0;
    private static final int CORNER = 1;
    private static final int SQUARE = 2;
    
    private AbstractUnusedRule rule;
    private GridState gridState;
    private List<Square> squares;
    private Cage cage;

    @SuppressWarnings("incomplete-switch")
    private void prepareRule(int gridSize, int gridType, int numberOfSquares, String operation, int value)
    {
        squares = prepareSquares(gridType, numberOfSquares);
        cage = new Cage(operation, value, squares);
        List<Cage> cages = Collections.singletonList(cage);
        gridState = new GridState(gridSize, cages);

        switch(operation)
        {
            case MINUS  : rule = new MinusRule (cages); break;
            case PLUS   : rule = new PlusRule  (cages); break;
            case TIMES  : rule = new TimesRule (cages); break;
            case DIVIDE : rule = new DivideRule(cages); break;
        }
    }
    
    @SuppressWarnings({ "incomplete-switch", "hiding" })
    private List<Square> prepareSquares(int gridType, int numberOfSquares)
    {
        List<Square> squares = new ArrayList<>(numberOfSquares);
 
        // Check we don't pass in incorrect arguments.
        if (numberOfSquares == 2)
        {
            assertEquals(LINEAR, gridType);
        }
        if (gridType == CORNER)
        {
            assertEquals(1, numberOfSquares % 2);
        }
        else if (gridType == SQUARE)
        {
            assertEquals(4, numberOfSquares);
        }

        int x = 0, y = 0;
        for (int i = 0; i < numberOfSquares; i++)
        {
            Square square = null;
            switch (gridType)
            {
                case LINEAR :
    
                    square = new Square(0, i);
    
                    break;
                    
                case CORNER :
    
                    square = new Square(x, y);
                        
                    if (i < numberOfSquares / 2) x++; else y++;
    
                    break;
                    
                case SQUARE :
    
                    square = new Square(i / 2, i % 2);

                    break;
            }
            squares.add(square);
        }
        return squares;
    }

    @Test
    public void testMinus()
    {
        final int gridSize = 3;

        List<Set<Integer>> unusedValues;

        prepareRule(gridSize, LINEAR, PAIR, MINUS, 1);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, Collections.emptySet(), Collections.emptySet());

        prepareRule(gridSize, LINEAR, PAIR, MINUS, 1);
        gridState.removeValue(squares.get(0), 2);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, Collections.emptySet(), setOf(1, 3));

        prepareRule(gridSize, LINEAR, PAIR, MINUS, 1);
        gridState.removeValue(squares.get(1), 1);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, Collections.emptySet(), Collections.emptySet());

        prepareRule(gridSize, LINEAR, PAIR, MINUS, 1);
        gridState.removeValue(squares.get(0), 1);
        gridState.removeValue(squares.get(0), 3);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, Collections.emptySet(), setOf(2));

        prepareRule(gridSize, LINEAR, PAIR, MINUS, 2);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(2), setOf(2));

        prepareRule(gridSize, LINEAR, PAIR, MINUS, 2);
        gridState.removeValue(squares.get(0), 2);
        gridState.removeValue(squares.get(1), 2);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, Collections.emptySet(), Collections.emptySet());

        prepareRule(gridSize, LINEAR, PAIR, MINUS, 2);
        gridState.removeValue(squares.get(0), 1);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(2), setOf(2, 3));

        prepareRule(gridSize, LINEAR, PAIR, MINUS, 2);
        gridState.removeValue(squares.get(0), 1);
        gridState.removeValue(squares.get(1), 1);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(2, 3), setOf(2, 3));
    }

    @Test
    public void testDivideWithGridSize4()
    {
        final int gridSize = 4;

        List<Set<Integer>> unusedValues;

        prepareRule(gridSize, LINEAR, PAIR, DIVIDE, 2);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(3), setOf(3));

        prepareRule(gridSize, LINEAR, PAIR, DIVIDE, 3);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(2, 4), setOf(2, 4));

        prepareRule(gridSize, LINEAR, PAIR, DIVIDE, 4);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(2, 3), setOf(2, 3));

        prepareRule(gridSize, LINEAR, PAIR, DIVIDE, 2);
        gridState.removeValue(squares.get(0), 1);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(3), setOf(3));

        prepareRule(gridSize, LINEAR, PAIR, DIVIDE, 2);
        gridState.removeValue(squares.get(1), 2);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(1, 3, 4), setOf(3));
    }

    @Test
    public void testDivideWithGridSize6()
    {
        final int gridSize = 6;

        List<Set<Integer>> unusedValues;

        prepareRule(gridSize, LINEAR, PAIR, DIVIDE, 2);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(5), setOf(5));

        prepareRule(gridSize, LINEAR, PAIR, DIVIDE, 3);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(4, 5), setOf(4, 5));

        prepareRule(gridSize, LINEAR, PAIR, DIVIDE, 4);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(2, 3, 5, 6), setOf(2, 3, 5, 6));

        prepareRule(gridSize, LINEAR, PAIR, DIVIDE, 2);
        gridState.removeValue(squares.get(1), 2);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(1, 4, 5), setOf(5));

        prepareRule(gridSize, LINEAR, PAIR, DIVIDE, 2);
        gridState.removeValue(squares.get(0), 2);
        gridState.removeValue(squares.get(1), 4);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(5), setOf(1, 5));

        prepareRule(gridSize, LINEAR, PAIR, DIVIDE, 3);
        gridState.removeValue(squares.get(0), 3);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(4, 5), setOf(1, 4, 5));
    }

    @Test
    public void testAddWithGridSize3()
    {
        final int gridSize = 3;

        List<Set<Integer>> unusedValues;

        prepareRule(gridSize, LINEAR, PAIR, PLUS, 5);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(1), setOf(1));

        prepareRule(gridSize, LINEAR, TRIPLE, PLUS, 6);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());

        prepareRule(gridSize, LINEAR, TRIPLE, PLUS, 6);
        gridState.removeValue(squares.get(0), 2);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());

        prepareRule(gridSize, LINEAR, TRIPLE, PLUS, 6);
        gridState.removeValue(squares.get(0), 3);
        gridState.removeValue(squares.get(1), 3);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, Collections.emptySet(), Collections.emptySet(), setOf(1, 2));

        prepareRule(gridSize, CORNER, TRIPLE, PLUS, 4);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(2, 3), setOf(1, 3), setOf(2, 3));
    }

    @Test
    public void testTimesWithGridSize3()
    {
        final int gridSize = 3;

        List<Set<Integer>> unusedValues;

        prepareRule(gridSize, LINEAR, PAIR, TIMES, 2);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(3), setOf(3));

        prepareRule(gridSize, LINEAR, PAIR, TIMES, 3);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(2), setOf(2));

        prepareRule(gridSize, LINEAR, PAIR, TIMES, 6);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(1), setOf(1));

        prepareRule(gridSize, LINEAR, TRIPLE, TIMES, 6);
        gridState.removeValue(squares.get(0), 2);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());

        prepareRule(gridSize, CORNER, TRIPLE, TIMES, 2);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(2, 3), setOf(1, 3), setOf(2, 3));

        prepareRule(gridSize, CORNER, TRIPLE, TIMES, 4);
        unusedValues = rule.findUnusedValues(cage, gridState);
        verifyResult(unusedValues, setOf(1, 3), setOf(2, 3), setOf(1, 3));
    }

    @SafeVarargs
    private final void verifyResult(List<Set<Integer>> actualValues, Set<Integer>... expectedValues)
    {
        assertEquals(expectedValues.length, actualValues.size());
        for (int i = 0; i < expectedValues.length; i++)
        {
            assertThat(actualValues.get(i), equalTo(expectedValues[i]));
        }
    }

    private Set<Integer> setOf(int... values)
    {
        return IntStream.of(values).boxed().collect(Collectors.toSet());
    }
}
