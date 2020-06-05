/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.List;
import java.util.Set;

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
    AbstractUnusedRule(String name, List<Cage> cages, boolean cagesReady, String operation)
    {
        super(name, cages, cagesReady, operation);
    }

    @Override
    public final void applyTo(GridState gridState)
    {
        if (exhausted)
        {
            return;
        }

        boolean change = false;
        for (Cage cage : cages)
        {
            if (!isSolved(cage, gridState) && solveCage(cage, gridState))
            {
                change = true;
            }
        }

        if (change)
        {
            exhausted = cages.stream().allMatch(cage -> isSolved(cage, gridState));
        }
    }

    @SuppressWarnings("static-method")
    private boolean isSolved(Cage cage, GridState gridState)
    {
        return cage.getSquares().stream().allMatch(square -> gridState.isSolved(square));
    }

    private boolean solveCage(Cage cage, GridState gridState)
    {
        List<Square> squares = cage.getSquares();
        List<Set<Integer>> unusedValues = findUnusedValues(cage, gridState);
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
        return StringUtil.toString(cages);
    }
}
