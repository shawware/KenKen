/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.ArrayList;
import java.util.List;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.Row;
import au.com.shawware.kenken.model.Square;

/**
 * A rule that solves a grid's rows.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
class RowRule extends AbstractLineRule
{
    RowRule()
    {
        super("Row"); //$NON-NLS-1$
    }

    @Override
    List<Cage> buildLines(int gridSize, int sum, Square[][] grid)
    {
        List<Cage> rows = new ArrayList<>(gridSize);
        for (int y = 0; y < gridSize; y++)
        {
            List<Square> squares = new ArrayList<>(gridSize);
            for (int x = 0; x < gridSize; x++)
            {
                squares.add(grid[x][y]);
            }
            rows.add(new Row(y, sum, squares));
        }
        return rows;
    }
}
