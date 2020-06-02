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
 * Solves a rule that applies an operation to a pair.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
abstract class AbstractPairRule extends AbstractUnusedRule
{
    // TODO: extract common code from APair APerm
    private final int gridSize;
    private final Function<Integer, BiFunction<Integer, Integer, Boolean>> operationSupplier;
    private final List<CageState> cageStates;

    private boolean initialised;
    // TODO: do we want to track the solved state of each cage
//    private boolean solved; 

    AbstractPairRule(String name, int gridSize, String operation, List<Cage> cages, Function<Integer, BiFunction<Integer, Integer, Boolean>> operationSupplier)
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
            System.out.format("Found a pre-solved c\n");
            // TODO: marked as solved, exhausted 
            return false;
        }

        List<Set<Integer>> unusedValues = findUnusedValues(cageState.getCage(), squareStates);
        // TODO: remove assert
        assert (unusedValues.size() == squareStates.size());
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
    List<Set<Integer>> findUnusedValues(Cage cage, List<SquareState> cageState)
    {
        final List<Set<Integer>> unusedValues = new ArrayList<>(cageState.size());

        for (int i = 0; i < cageState.size(); i++)
        {
            unusedValues.add(new HashSet<>());
        }

        handleTwoValues(cageState, unusedValues, operationSupplier.apply(cage.getValue()));

        return unusedValues;
    }
 
    private void handleTwoValues(List<SquareState> cageState, List<Set<Integer>> unusedValues, BiFunction<Integer, Integer, Boolean> operation)
    {
        final List<Integer> s1Values = cageState.get(0).getValues();
        final List<Integer> s2Values = cageState.get(1).getValues();
        
        handlePermutations(s1Values, s2Values, operation, unusedValues.get(0));
        handlePermutations(s2Values, s1Values, operation, unusedValues.get(1));
    }

    @SuppressWarnings({ "boxing", "static-method" })
    private void handlePermutations(List<Integer> s1Values, List<Integer> s2Values, BiFunction<Integer, Integer, Boolean> operation, Set<Integer> unused)
    {
        for (Integer i1 : s1Values)
        {
            boolean permutationFound = false;
            for (Integer i2 : s2Values)
            {
                if (i1 == i2)
                {
                    continue;
                }
                if (operation.apply(i1, i2))
                {
                    permutationFound = true;
                    break;
                }
            }
            if (!permutationFound)
            {
                unused.add(i1);
            }
        }
    }

    @SuppressWarnings("static-method")
    private boolean couldBeSolved(List<SquareState> squareStates)
    {
        boolean couldBeSolved = true;
        
        // TODO: this loop can be better
        for (SquareState squareState : squareStates)
        {
            if (squareState.isSolved())
            {
                continue;
            }
            if (squareState.couldBeSolved())
            {
                continue;
            }
            couldBeSolved = false;
            break;
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
