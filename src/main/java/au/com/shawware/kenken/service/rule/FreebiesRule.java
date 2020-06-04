/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.Square;
import au.com.shawware.util.StringUtil;

import static au.com.shawware.kenken.model.Cage.EQUALS;

/**
 * Solves freebie cages, ie. those with just one square.
 * As such, we only ever run this rule once as freebie squares always solve on the first attempt;
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
class FreebiesRule extends AbstractRule
{
    private final List<Cage> cages;

    FreebiesRule(List<Cage> cages)
    {
        super("Freebies"); //$NON-NLS-1$
        this.cages = cages.stream()
                .filter(cage -> cage.getOperation().equals(EQUALS))
                .collect(Collectors.toList());
        this.exhausted = this.cages.isEmpty();
    }

    @Override
    public boolean applyTo(GridState gridState)
    {
        if (exhausted)
        {
            return false;
        }

        cages.forEach(cage -> {
            Square square = cage.getSquares().get(0); // There's only ever one square.
            IntStream.rangeClosed(1, gridState.getGridSize())
                .filter(value -> value != cage.getValue())
                .forEach(value -> gridState.removeValue(square, value));
        });
        
        exhausted = true;
        return true;
    }

    @Override
    @SuppressWarnings("boxing")
    public String toString()
    {
        return StringUtil.toString(exhausted, cages);
    }
}
