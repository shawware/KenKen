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

/**
 * Base class and template for rules that work on finding un-used values.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
abstract class AbstractUnusedRule extends AbstractRule
{
    AbstractUnusedRule(String name, String operation, boolean filterCages, boolean sortCages)
    {
        super(name, operation, filterCages, sortCages);
    }

    @Override
    protected boolean applyRuleTo(Cage cage, GridState gridState)
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
                }
            }
        }
        return change;
    }
    
    // Package visibility for testing
    abstract List<Set<Integer>> findUnusedValues(Cage cage, GridState gridState);
}
