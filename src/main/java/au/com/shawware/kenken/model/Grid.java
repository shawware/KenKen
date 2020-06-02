/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.model;

import au.com.shawware.util.StringUtil;

/**
 * A KenKen grid - represents a solution.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class Grid
{
    private final int[][] solution;

    public Grid(int[][] solution)
    {
        this.solution = solution;
    }

    public void accept(IGridVisitor visitor)
    {
        visitor.startGrid();

        // Visit row-by-row, square-by-square.
        for (int y = 0; y < solution.length; y++)
        {
            visitor.startRow();

            for (int x = 0; x < solution.length; x++)
            {
                visitor.square(solution[x][y]);
            }

            visitor.endRow();
        }

        visitor.endGrid();
    }

    @Override
    public String toString()
    {
        return StringUtil.toString((Object[])solution);
    }
}
