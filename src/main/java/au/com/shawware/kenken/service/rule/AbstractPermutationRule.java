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
import java.util.stream.Collectors;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.util.StringUtil;

/**
 * Solves a rule that applies an operation to a cage of two or more squares.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
abstract class AbstractPermutationRule extends AbstractUnusedRule
{
    private final int gridSize;
    private final Function<Integer, BiFunction<Integer, Integer, Integer>> operationSupplier;
    private final List<CageState> cageStates;

    private boolean initialised;

    AbstractPermutationRule(String name, int gridSize, String operation, List<Cage> cages, Function<Integer, BiFunction<Integer, Integer, Integer>> operationSupplier)
    {
        super(name);
        this.gridSize = gridSize;
        this.operationSupplier = operationSupplier;
        this.exhausted = cages.isEmpty();
        this.initialised = false;
        this.cageStates = buildCageStates(operation, cages);
    }
    
    @SuppressWarnings("static-method")
    private List<CageState> buildCageStates(String operation, List<Cage> cages)
    {
        return cages.stream()
                .filter(cage -> cage.getOperation().equals(operation))
                .map(cage -> new CageState(cage))
                .collect(Collectors.toList());
    }

    @Override
    public boolean applyTo(SquareState[][] gridState)
    {
        if (exhausted)
        {
            return false;
        }

        if (!initialised)
        {
            cageStates.forEach(cageState -> cageState.initialise(gridState));
            initialised = true;
        }

        boolean change = false;
        for (CageState cageState : cageStates)
        {
            if (solveCage(cageState))
            {
                change = true;
            }
        }

        exhausted = cageStates.stream().allMatch(CageState::isSolved);

        return change;
    }

    private boolean solveCage(CageState cageState)
    {
        // Our grid may have been solved by other work, so we check for that first.
        // TODO: is this true?
        if (cageState.isSolved())
        {
            System.out.format("Called on a pre-solved cage\n");
            return false;
        }

        List<SquareState> squareStates = cageState.getSquareStates();

        if (couldBeSolved(squareStates))
        {
            System.out.format("Found a pre-solved cage\n");
            return false;
        }

        List<Set<Integer>> unusedValues = findUnusedValues(cageState.getCage(), squareStates);
        boolean change = false;
        for (int i = 0; i < unusedValues.size(); i++)
        {
            Set<Integer> unused = unusedValues.get(i);
            if (!unused.isEmpty())
            {
                change = true;
                System.out.format("Removing any unused values found: %s\n", unusedValues.get(i));
                SquareState squareState = squareStates.get(i);
                if (!squareState.isSolved())
                {
                    System.out.format("Removing from %s\n", squareState);
                    squareState.removeValues(unusedValues.get(i));
                    squareState.solve();
                }
            }
        }

        return change;
    }

    // Package visibility for testing
    @Override
    @SuppressWarnings("boxing")
    List<Set<Integer>> findUnusedValues(Cage cage, List<SquareState> squareStates)
    {
        final List<Set<Integer>> unusedValues = new ArrayList<>(squareStates.size());
        for (int i = 0; i < squareStates.size(); i++)
        {
            unusedValues.add(new HashSet<>());
        }

        handleTwoOrMoreValues(cage.getValue(), squareStates, unusedValues, operationSupplier.apply(cage.getValue()));

        return unusedValues;
    }

    @SuppressWarnings("boxing")
    private void handleTwoOrMoreValues(int value, List<SquareState> squareStates, List<Set<Integer>> unusedValues, BiFunction<Integer, Integer, Integer> operation)
    {
        int numberOfSquares = squareStates.size();

        RunningTotal[] runningTotal = new RunningTotal[numberOfSquares];
        for (int i = 0; i < numberOfSquares; i++)
        {
            runningTotal[i] = new RunningTotal();
        }

        for (int i = 0; i < numberOfSquares; i++)
        {
            List<SquareState> otherSquareStates = new ArrayList<>(numberOfSquares - 1);
            Set<Integer> unused = unusedValues.get(i);
            for (int j = 0; j < numberOfSquares; j++)
            {
                if (j != i)
                {
                    otherSquareStates.add(squareStates.get(j));
                }
            }
            SquareState thisSquareState = squareStates.get(i);
            for (Integer initialValue : thisSquareState.getValues())
            {
                runningTotal[0].initialise(thisSquareState.getSquare().getX(), thisSquareState.getSquare().getY(), initialValue);
                if (!findPermutation(value, 0, runningTotal, otherSquareStates, operation))
                {
                    unused.add(initialValue);
                }
            }
        }
    }

    // TODO: should current index be split into RT index and other values index?
    @SuppressWarnings("boxing")
    private boolean findPermutation(int total, int currentIndex, RunningTotal[] runningTotal, List<SquareState> otherSquareStates, BiFunction<Integer, Integer, Integer> operation)
    {
        // TODO: more advanced check is available than just linear
        boolean found = false;
        SquareState nextSquareState = otherSquareStates.get(currentIndex);
        List<Integer> values = nextSquareState.getValues();
        for (Integer value : values)
        {
            if (!thisValueCanBeUsed(value, nextSquareState, currentIndex, runningTotal))
            {
                continue;
            }

            int subTotal = operation.apply(runningTotal[currentIndex].runningTotal, value);
            if (subTotal > total)
            {
                return false;
            }
            runningTotal[currentIndex + 1].update(nextSquareState.getSquare().getX(), nextSquareState.getSquare().getY(), value, subTotal);
            // TODO: last list
            if (currentIndex == (otherSquareStates.size() - 1))
            {
                if (runningTotal[currentIndex + 1].runningTotal == total)
                {
                    return true;
                }
            }
            else
            {
                found = findPermutation(total, currentIndex + 1, runningTotal, otherSquareStates, operation);
                if (found)
                {
                    break;
                }
            }
        }
        return found;
    }

    @SuppressWarnings("static-method")
    private boolean thisValueCanBeUsed(int value, SquareState nextSquareState, int currentIndex, RunningTotal[] runningTotal)
    {
        boolean canBeUsed = true;
        for (int i = 0; i <= currentIndex; i++)
        {
            if (value == runningTotal[i].value)
            {
                if ((nextSquareState.getSquare().getX() == runningTotal[i].x) ||
                    (nextSquareState.getSquare().getY() == runningTotal[i].y))
                {
                    // We're in line with that value so we can't use it.
                    canBeUsed = false;
                    break;
                }
            }
        }
        return canBeUsed;
    }

    @SuppressWarnings("static-method")
    private boolean couldBeSolved(List<SquareState> squareStates)
    {
        boolean couldBeSolved = true;
        
        for (SquareState squareState : squareStates)
        {
            if (!squareState.isSolved() && !squareState.couldBeSolved())
            {
                couldBeSolved = false;
                break;
            }
        }

        return couldBeSolved;
    }

    @Override
    @SuppressWarnings("boxing")
    public String toString()
    {
        return StringUtil.toString(gridSize, cageStates);
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