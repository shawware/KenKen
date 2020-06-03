/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.util.StringUtil;

/**
 * Base class and template for rules that work on finding un-used values.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
abstract class AbstractUnusedRule extends AbstractRule
{
    private final List<CageState> cageStates;

    private boolean initialised;

    AbstractUnusedRule(String name, String operation, List<Cage> cages)
    {
        super(name);

        this.exhausted = cages.isEmpty();
        this.cageStates = buildCageStates(operation, cages);
        this.initialised = false;
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
    public final boolean applyTo(SquareState[][] gridState)
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
    abstract List<Set<Integer>> findUnusedValues(Cage cage, List<SquareState> squareState);

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
    public String toString()
    {
        return StringUtil.toString(cageStates);
    }
}
