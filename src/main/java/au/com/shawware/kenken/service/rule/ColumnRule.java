/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.Square;

import static au.com.shawware.kenken.model.Cage.PLUS;

/**
 * A rule that solves a grid's columns.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
class ColumnRule extends AbstractPlusRule
{
    // TODO: should rules receives cages if they don't need them?
    // Solve in the rule factory
    ColumnRule(int gridSize, List<Cage> cages)
    {
        super("Column", buildCages(gridSize, cages), false); //$NON-NLS-1$
    }

    private static List<Cage> buildCages(int gridSize, List<Cage> cages)
    {
        int sum = IntStream.rangeClosed(1, gridSize).sum();
        Square[][] grid = extractGrid(gridSize, cages);

        List<Cage> columns = new ArrayList<>(gridSize);
        for (int x = 0; x < gridSize; x++)
        {
            List<Square> squares = new ArrayList<>(gridSize);
            for (int y = 0; y < gridSize; y++)
            {
                squares.add(grid[x][y]);
            }
            columns.add(new Cage(PLUS, sum, squares));
        }
        
        return columns;
    }

    private static Square[][] extractGrid(int gridSize, List<Cage> cages)
    {
        Square[][] grid = new Square[gridSize][gridSize];
        cages.forEach(cage ->
            cage.getSquares().forEach(square -> grid[square.getX()][square.getY()] = square)
        );
        return grid;
    }
}
