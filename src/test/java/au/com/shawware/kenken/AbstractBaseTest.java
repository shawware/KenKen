/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken;

import java.util.ArrayList;
import java.util.List;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.Square;

/**
 * Base class with utility methods for some tests.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public abstract class AbstractBaseTest
{
    protected final Cage buildCage(String operation, int value, int[][] coords)
    {
        List<Square> squares = buildSquares(coords);
        return new Cage(operation, value, squares);
    }
    
    @SuppressWarnings("static-method")
    protected final List<Square> buildSquares(int[][] coords)
    {
        List<Square> squares = new ArrayList<>();
        for (int[] pair : coords)
        {
            squares.add(new Square(pair[0], pair[1]));
        }
        return squares;
    }
}
