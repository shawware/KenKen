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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    
    private AbstractUnusedRule solver;
    private Cage cage;
    private List<SquareState> squareStates; // Keep a reference so we can manipulate the base test conditions.

    @SuppressWarnings("incomplete-switch")
    private void prepareSolver(int gridSize, int gridType, int numberOfSquares, String operation, int value)
    {
        final List<Square> squares = new ArrayList<>(numberOfSquares);
        squareStates = new ArrayList<>(numberOfSquares);
        int[][] coordinates = prepareCoordinates(gridType, numberOfSquares);
        
        for (int i = 0; i < numberOfSquares; i++)
        {
            Square square = new Square(coordinates[i][0], coordinates[i][1]);
            squares.add(square);
            squareStates.add(new SquareState(gridSize, square));
        }

        cage = new Cage(operation, value, squares);
        List<Cage> cages = Collections.singletonList(cage);

        switch(operation)
        {
            case MINUS  : solver = new MinusRule (gridSize, cages); break;
            case PLUS   : solver = new PlusRule  (gridSize, cages); break;
            case TIMES  : solver = new TimesRule (gridSize, cages); break;
            case DIVIDE : solver = new DivideRule(gridSize, cages); break;
        }
    }
    
    @SuppressWarnings("incomplete-switch")
    private int[][] prepareCoordinates(int gridType, int numberOfSquares)
    {
        // Check we don't pass in incorrect arguments.
        if ((numberOfSquares == 1) || (numberOfSquares == 2))
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

        final int[][] coordinates = new int[numberOfSquares][2];

        int x = 0, y = 0;
        for (int i = 0; i < numberOfSquares; i++)
        {
            switch (gridType)
            {
                case LINEAR :
    
                    coordinates[i][0] = 0;
                    coordinates[i][1] = i;
    
                    break;
                    
                case CORNER :
    
                    coordinates[i][0] = x;
                    coordinates[i][1] = y;
                        
                    if (i < numberOfSquares / 2) x++; else y++;
    
                    break;
                    
                case SQUARE :
    
                    coordinates[i][0] = i / 2;
                    coordinates[i][1] = i % 2;

                    break;
            }
        }
        return coordinates;
    }

    @Test
    public void testMinus()
    {
        final int gridSize = 3;

        List<Set<Integer>> unusedValues;

        prepareSolver(gridSize, LINEAR, PAIR, MINUS, 1);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, Collections.emptySet(), Collections.emptySet());

        prepareSolver(gridSize, LINEAR, PAIR, MINUS, 1);
        squareStates.get(0).removeValue(2);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, Collections.emptySet(), setOf(1, 3));

        prepareSolver(gridSize, LINEAR, PAIR, MINUS, 1);
        squareStates.get(1).removeValue(1);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, Collections.emptySet(), Collections.emptySet());

        prepareSolver(gridSize, LINEAR, PAIR, MINUS, 1);
        squareStates.get(0).removeValue(1);
        squareStates.get(0).removeValue(3);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, Collections.emptySet(), setOf(2));

        prepareSolver(gridSize, LINEAR, PAIR, MINUS, 2);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(2), setOf(2));

        prepareSolver(gridSize, LINEAR, PAIR, MINUS, 2);
        squareStates.get(0).removeValue(2);
        squareStates.get(1).removeValue(2);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, Collections.emptySet(), Collections.emptySet());

        prepareSolver(gridSize, LINEAR, PAIR, MINUS, 2);
        squareStates.get(0).removeValue(1);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(2), setOf(2, 3));

        prepareSolver(gridSize, LINEAR, PAIR, MINUS, 2);
        squareStates.get(0).removeValue(1);
        squareStates.get(1).removeValue(1);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(2, 3), setOf(2, 3));
    }

    @Test
    public void testDivideWithGridSize4()
    {
        final int gridSize = 4;

        List<Set<Integer>> unusedValues;

        prepareSolver(gridSize, LINEAR, PAIR, DIVIDE, 2);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(3), setOf(3));

        prepareSolver(gridSize, LINEAR, PAIR, DIVIDE, 3);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(2, 4), setOf(2, 4));

        prepareSolver(gridSize, LINEAR, PAIR, DIVIDE, 4);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(2, 3), setOf(2, 3));

        prepareSolver(gridSize, LINEAR, PAIR, DIVIDE, 2);
        squareStates.get(0).removeValue(1);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(3), setOf(3));

        prepareSolver(gridSize, LINEAR, PAIR, DIVIDE, 2);
        squareStates.get(1).removeValue(2);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(1, 3, 4), setOf(3));
    }

    @Test
    public void testDivideWithGridSize6()
    {
        final int gridSize = 6;

        List<Set<Integer>> unusedValues;

        prepareSolver(gridSize, LINEAR, PAIR, DIVIDE, 2);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(5), setOf(5));

        prepareSolver(gridSize, LINEAR, PAIR, DIVIDE, 3);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(4, 5), setOf(4, 5));

        prepareSolver(gridSize, LINEAR, PAIR, DIVIDE, 4);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(2, 3, 5, 6), setOf(2, 3, 5, 6));

        prepareSolver(gridSize, LINEAR, PAIR, DIVIDE, 2);
        squareStates.get(1).removeValue(2);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(1, 4, 5), setOf(5));

        prepareSolver(gridSize, LINEAR, PAIR, DIVIDE, 2);
        squareStates.get(0).removeValue(2);
        squareStates.get(1).removeValue(4);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(5), setOf(1, 5));

        prepareSolver(gridSize, LINEAR, PAIR, DIVIDE, 3);
        squareStates.get(0).removeValue(3);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(4, 5), setOf(1, 4, 5));
    }

    @Test
    public void testAddWithGridSize3()
    {
        final int gridSize = 3;

        List<Set<Integer>> unusedValues;

        prepareSolver(gridSize, LINEAR, PAIR, PLUS, 5);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(1), setOf(1));

        prepareSolver(gridSize, LINEAR, TRIPLE, PLUS, 6);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());

        prepareSolver(gridSize, LINEAR, TRIPLE, PLUS, 6);
        squareStates.get(0).removeValue(2);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());

        prepareSolver(gridSize, LINEAR, TRIPLE, PLUS, 6);
        squareStates.get(0).removeValue(3);
        squareStates.get(1).removeValue(3);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, Collections.emptySet(), Collections.emptySet(), setOf(1, 2));

        prepareSolver(gridSize, CORNER, TRIPLE, PLUS, 4);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(2, 3), setOf(1, 3), setOf(2, 3));
    }

    @Test
    public void testTimesWithGridSize3()
    {
        final int gridSize = 3;

        List<Set<Integer>> unusedValues;

        prepareSolver(gridSize, LINEAR, PAIR, TIMES, 2);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(3), setOf(3));

        prepareSolver(gridSize, LINEAR, PAIR, TIMES, 3);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(2), setOf(2));

        prepareSolver(gridSize, LINEAR, PAIR, TIMES, 6);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(1), setOf(1));

        prepareSolver(gridSize, LINEAR, TRIPLE, TIMES, 6);
        squareStates.get(0).removeValue(2);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());

        prepareSolver(gridSize, CORNER, TRIPLE, TIMES, 2);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(2, 3), setOf(1, 3), setOf(2, 3));

        prepareSolver(gridSize, CORNER, TRIPLE, TIMES, 4);
        unusedValues = solver.findUnusedValues(cage, squareStates);
        verifyResult(unusedValues, setOf(1, 3), setOf(2, 3), setOf(1, 3));
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

    private Set<Integer> setOf(int... values)
    {
        return IntStream.of(values).boxed().collect(Collectors.toSet());
    }
}
