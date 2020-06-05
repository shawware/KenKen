/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.Square;
import au.com.shawware.util.StringUtil;

/**
 * Solves a rule that applies an operation to a cage of two or more squares.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
abstract class AbstractPermutationRule extends AbstractUnusedRule
{
    private final Function<Integer, BiFunction<Integer, Integer, Integer>> operationSupplier;

    AbstractPermutationRule(String name, List<Cage> cages, boolean cagesReady, String operation, Function<Integer, BiFunction<Integer, Integer, Integer>> operationSupplier)
    {
        super(name, cages, cagesReady, operation);
        this.operationSupplier = operationSupplier;
    }
    
    @Override
    @SuppressWarnings("boxing")
    List<Set<Integer>> findUnusedValues(Cage cage, GridState gridState)
    {
        final List<Square> squares = cage.getSquares();
        final List<Set<Integer>> unusedValues = new ArrayList<>(squares.size());
        for (int i = 0; i < squares.size(); i++)
        {
            unusedValues.add(new HashSet<>());
        }

        handleTwoOrMoreValues(cage.getValue(), squares, unusedValues, gridState, operationSupplier.apply(cage.getValue()));

        return unusedValues;
    }

    @SuppressWarnings("boxing")
    private void handleTwoOrMoreValues(int value, List<Square> squares, List<Set<Integer>> unusedValues, GridState gridState, BiFunction<Integer, Integer, Integer> operation)
    {
        int numberOfSquares = squares.size();

        RunningTotal[] runningTotal = new RunningTotal[numberOfSquares];
        for (int i = 0; i < numberOfSquares; i++)
        {
            runningTotal[i] = new RunningTotal();
        }

        for (int i = 0; i < numberOfSquares; i++)
        {
            List<Square> otherSquareStates = new ArrayList<>(numberOfSquares - 1);
            Set<Integer> unused = unusedValues.get(i);
            for (int j = 0; j < numberOfSquares; j++)
            {
                if (j != i)
                {
                    otherSquareStates.add(squares.get(j));
                }
            }
            Square thisSquare = squares.get(i);
            for (Integer initialValue : gridState.getValues(thisSquare))
            {
                runningTotal[0].initialise(thisSquare.getX(), thisSquare.getY(), initialValue);
                if (!findPermutation(value, 0, runningTotal, otherSquareStates, gridState, operation))
                {
                    unused.add(initialValue);
                }
            }
        }
    }

    // TODO: should current index be split into RT index and other values index?
    @SuppressWarnings("boxing")
    private boolean findPermutation(int total, int currentIndex, RunningTotal[] runningTotal, List<Square> otherSquares, GridState gridState, BiFunction<Integer, Integer, Integer> operation)
    {
        boolean found = false;
        Square nextSquare = otherSquares.get(currentIndex);
        List<Integer> values = gridState.getValues(nextSquare);
        for (Integer value : values)
        {
            if (!thisValueCanBeUsed(value, nextSquare, currentIndex, runningTotal))
            {
                continue;
            }

            int subTotal = operation.apply(runningTotal[currentIndex].runningTotal, value);
            if (subTotal > total)
            {
                return false;
            }
            runningTotal[currentIndex + 1].update(nextSquare.getX(), nextSquare.getY(), value, subTotal);
            // TODO: last list
            if (currentIndex == (otherSquares.size() - 1))
            {
                if (runningTotal[currentIndex + 1].runningTotal == total)
                {
                    return true;
                }
            }
            else
            {
                found = findPermutation(total, currentIndex + 1, runningTotal, otherSquares, gridState, operation);
                if (found)
                {
                    break;
                }
            }
        }
        return found;
    }

    @SuppressWarnings("static-method")
    private boolean thisValueCanBeUsed(int value, Square nextSquare, int currentIndex, RunningTotal[] runningTotal)
    {
        boolean canBeUsed = true;
        for (int i = 0; i <= currentIndex; i++)
        {
            if (value == runningTotal[i].value)
            {
                if ((nextSquare.getX() == runningTotal[i].x) ||
                    (nextSquare.getY() == runningTotal[i].y))
                {
                    // We're in line with that value so we can't use it.
                    canBeUsed = false;
                    break;
                }
            }
        }
        return canBeUsed;
    }
}

@SuppressWarnings("hiding")
class RunningTotal
{
    int x;
    int y;
    int value;
    int runningTotal;
    
    void initialise(int x, int y, int value)
    {
        update(x, y, value, value);
    }

    void update(int x, int y, int value, int runningTotal)
    {
        this.x = x;
        this.y = y;
        this.value = value;
        this.runningTotal = runningTotal;
    }

    @Override
    @SuppressWarnings("boxing")
    public String toString()
    {
        return StringUtil.toString(x, y, value, runningTotal);
    }
}