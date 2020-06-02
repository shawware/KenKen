/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import au.com.shawware.kenken.model.Cage;

import static au.com.shawware.kenken.model.Cage.DIVIDE;

/**
 * Solves cages with the minus operation.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
class DivideRule extends AbstractPairRule
{
    @SuppressWarnings("boxing")
    private static final Function<Integer, BiFunction<Integer, Integer, Boolean>> DIVISION = value -> (i1, i2) -> (i1 / i2 == value && i1 % i2 == 0) || (i2 / i1 == value && i2 % i1 == 0);

    DivideRule(int gridSize, List<Cage> cages)
    {
        super("Division", gridSize, DIVIDE, cages, DIVISION); //$NON-NLS-1$
    }
}
