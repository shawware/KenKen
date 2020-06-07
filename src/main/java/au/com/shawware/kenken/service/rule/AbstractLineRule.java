/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.List;
import java.util.stream.IntStream;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.Square;

/**
 * Base class for row and column rules.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
abstract class AbstractLineRule extends AbstractPlusRule
{
    AbstractLineRule(String name)
    {
        super(name, false, false);
    }

    @Override
    @SuppressWarnings("hiding")
    protected final List<Cage> generateCages(int gridSize, List<Cage> cages, GridState gridState)
    {
        int sum = IntStream.rangeClosed(1, gridSize).sum();
        Square[][] grid = extractGrid(gridSize, cages);
        return buildLines(gridSize, sum, grid);
    }

    abstract List<Cage> buildLines(int gridSize, int sum, Square[][] grid);

    @SuppressWarnings({ "static-method", "hiding" })
    private Square[][] extractGrid(int gridSize, List<Cage> cages)
    {
        Square[][] grid = new Square[gridSize][gridSize];
        cages.forEach(cage ->
            cage.getSquares().forEach(square -> grid[square.getX()][square.getY()] = square)
        );
        return grid;
    }
}
