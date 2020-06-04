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
import au.com.shawware.kenken.model.Square;
import au.com.shawware.util.StringUtil;

/**
 * Base class and template for rules that work on finding un-used values.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
abstract class AbstractUnusedRule extends AbstractRule
{
    // TODO: can this be a list of cages
    private final List<CageState> cageStates;

    AbstractUnusedRule(String name, String operation, List<Cage> cages, boolean sort)
    {
        super(name);

        this.exhausted = cages.isEmpty();
        this.cageStates = buildCageStates(operation, cages, sort);
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
            cageStates.sort((cs1, cs2) -> Integer.compare(cs1.getCage().getSize(), cs2.getCage().getSize()));
        }
        return cageStates;
    }

    @Override
    public final boolean applyTo(GridState gridState)
    {
        if (exhausted)
        {
            return false;
        }

        boolean change = false;
        for (CageState cageState : cageStates)
        {
            if (!cageState.isSolved(gridState) && solveCage(cageState, gridState))
            {
                change = true;
            }
        }

        exhausted = cageStates.stream().allMatch(cageState -> cageState.isSolved(gridState));

        return change;
    }

    private boolean solveCage(CageState cageState, GridState gridState)
    {
        List<Square> squares = cageState.getSquares();
        List<Set<Integer>> unusedValues = findUnusedValues(cageState.getCage(), gridState);
        boolean change = false;
        for (int i = 0; i < unusedValues.size(); i++)
        {
            Set<Integer> unused = unusedValues.get(i);
            if (!unused.isEmpty())
            {
                change = true;
                Square square = squares.get(i);
                if (!gridState.isSolved(square))
                {
                    gridState.removeValues(square, unusedValues.get(i));
                    gridState.solve(square);
                }
            }
        }

        return change;
    }
    
    // Package visibility for testing
    abstract List<Set<Integer>> findUnusedValues(Cage cage, GridState gridState);

    @Override
    public String toString()
    {
        return StringUtil.toString(cageStates);
    }
}
