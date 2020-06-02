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
    private final int gridSize;

    public SinglesRule(int gridSize, @SuppressWarnings("unused") List<Cage> cages)
    {
        super("Singles"); //$NON-NLS-1$
        this.gridSize = gridSize;
        this.exhausted = false; // this rule never exhausts
    }

    @Override
    public boolean applyTo(SquareState[][] gridState)
    {
        int count = 0;
        while (processSingleValues(gridState))
        {
            // Nothing to do - keep going until all possible changes have been made.
            count++;
        }
        return (count > 0);
    }

    private boolean processSingleValues(SquareState[][] gridState)
    {
        boolean change = false;
        for (int x = 0; x < gridSize; x++)
        {
            for (int y = 0; y < gridSize; y++)
            {
                SquareState squareState = gridState[x][y];
                if (!squareState.isSolved() && squareState.couldBeSolved())
                {
                    change = true;
                    System.out.format("Found a single value at [%d, %d]: %d\n", x, y, squareState.value());
                    removeFromRow(gridState, x, y);
                    removeFromColumn(gridState, x, y);
                }
            }
        }
        return change;
    }

    @SuppressWarnings("static-method")
    private void removeFromRow(SquareState[][] gridState, int x, int y)
    {
        int value = gridState[x][y].value();
        for (int col = 0; col < gridState.length; col++)
        {
            if ((col != x) && !gridState[col][y].isSolved())
            {
                gridState[col][y].removeValue(value);
            }
        }                
    }

    @SuppressWarnings("static-method")
    private void removeFromColumn(SquareState[][] gridState, int x, int y)
    {
        int value = gridState[x][y].value();
        for (int row = 0; row < gridState.length; row++)
        {
            if ((row != y) && !gridState[x][row].isSolved())
            {
                gridState[x][row].removeValue(value);
            }
        }                
    }

    @Override
    @SuppressWarnings("boxing")
    public String toString()
    {
        return StringUtil.toString(gridSize, exhausted);
    }
}
