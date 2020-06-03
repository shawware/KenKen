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

    AbstractUnusedRule(String name, String operation, List<Cage> cages, boolean sort)
    {
        super(name);

        this.exhausted = cages.isEmpty();
        this.cageStates = buildCageStates(operation, cages, sort);
        this.initialised = false;
    }

    @SuppressWarnings({ "static-method", "hiding" })
    private List<CageState> buildCageStates(String operation, List<Cage> cages, boolean sort)
    {
        List<CageState> cageStates = cages.stream()
                .filter(cage -> cage.getOperation().equals(operation))
                .map(cage -> new CageState(cage))
                .collect(Collectors.toList());
        if (sort)
        {
            cageStates.sort((cs1, cs2) -> Integer.compare(cs1.getCage().getSquares().size(), cs2.getCage().getSquares().size()));
        }
        return cageStates;
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
            if (!cageState.isSolved() && solveCage(cageState))
            {
                change = true;
            }
        }

        exhausted = cageStates.stream().allMatch(CageState::isSolved);

        return change;
    }

    private boolean solveCage(CageState cageState)
    {
        List<SquareState> squareStates = cageState.getSquareStates();
        List<Set<Integer>> unusedValues = findUnusedValues(cageState.getCage(), squareStates);
        boolean change = false;
        for (int i = 0; i < unusedValues.size(); i++)
        {
            Set<Integer> unused = unusedValues.get(i);
            if (!unused.isEmpty())
            {
                change = true;
                SquareState squareState = squareStates.get(i);
                if (!squareState.isSolved())
                {
                    squareState.removeValues(unusedValues.get(i));
                    squareState.solve();
                }
            }
        }

        return change;
    }
    
    // Package visibility for testing
    abstract List<Set<Integer>> findUnusedValues(Cage cage, List<SquareState> squareState);

    @Override
    public String toString()
    {
        return StringUtil.toString(cageStates);
    }
}
