/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.List;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.util.StringUtil;

/**
 * Cleanup any singles found in the grid.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class SinglesRule extends AbstractRule
{
    @SuppressWarnings("unused")
    public SinglesRule(int gridSize, List<Cage> cages)
    {
        super("Singles"); //$NON-NLS-1$
        this.exhausted = false; // this rule never exhausts
    }

    @Override
    public boolean applyTo(GridState gridState)
    {
        int count = 0;
        while (processSingleValues(gridState))
        {
            // Nothing to do - keep going until all possible changes have been made.
            count++;
        }
        return (count > 0);
    }

    private boolean processSingleValues(GridState gridState)
    {
        boolean change = false;
        for (int x = 0; x < gridState.getGridSize(); x++)
        {
            for (int y = 0; y < gridState.getGridSize(); y++)
            {
                if (!gridState.isSolved(x, y) && gridState.couldBeSolved(x, y))
                {
                    change = true;
                    removeFromRow(gridState, x, y);
                    removeFromColumn(gridState, x, y);
                }
            }
        }
        return change;
    }

    @SuppressWarnings("static-method")
    private void removeFromRow(GridState gridState, int x, int y)
    {
        int value = gridState.value(x, y);
        for (int col = 0; col < gridState.getGridSize(); col++)
        {
            if ((col != x) && !gridState.isSolved(col, y))
            {
                gridState.removeValue(col, y, value);
            }
        }                
    }

    @SuppressWarnings("static-method")
    private void removeFromColumn(GridState gridState, int x, int y)
    {
        int value = gridState.value(x, y);
        for (int row = 0; row < gridState.getGridSize(); row++)
        {
            if ((row != y) && !gridState.isSolved(x, row))
            {
                gridState.removeValue(x, row, value);
            }
        }                
    }

    @Override
    @SuppressWarnings("boxing")
    public String toString()
    {
        return StringUtil.toString(exhausted);
    }
}
