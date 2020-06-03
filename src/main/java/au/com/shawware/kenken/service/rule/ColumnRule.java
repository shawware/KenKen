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
    ColumnRule(int gridSize, @SuppressWarnings("unused") List<Cage> cages)
    {
        super("Column", buildCages(gridSize)); //$NON-NLS-1$
    }

    private static List<Cage> buildCages(int gridSize)
    {
        int sum = IntStream.rangeClosed(1, gridSize).sum();
    
        List<Cage> cages = new ArrayList<>(gridSize);
        for (int x = 0; x < gridSize; x++)
        {
            List<Square> squares = new ArrayList<>(gridSize);
            for (int y = 0; y < gridSize; y++)
            {
                // TODO: find a way to re-use the squares from the grid
                squares.add(new Square(x, y));
            }
            cages.add(new Cage(PLUS, sum, squares));
        }
        
        return cages;
    }
}
