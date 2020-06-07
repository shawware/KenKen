/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.stream.IntStream;

import au.com.shawware.kenken.model.Square;

import static au.com.shawware.kenken.model.Cage.EQUALS;

/**
 * Solves freebie cages, ie. those with just one square.
 * As such, we only ever run this rule once as freebie squares always solve on the first attempt;
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
class FreebiesRule extends AbstractRule
{
    FreebiesRule()
    {
        super("Freebies", EQUALS, true, false); //$NON-NLS-1$
    }

    @Override
    protected void applyRuleTo(GridState gridState)
    {
        cages.forEach(cage -> {
            Square square = cage.getSquares().get(0); // There's only ever one square.
            IntStream.rangeClosed(1, gridState.getGridSize())
                .filter(value -> value != cage.getValue())
                .forEach(value -> gridState.removeValue(square, value));
        });
    }
}
