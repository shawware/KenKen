/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.function.BiFunction;
import java.util.function.Function;

import static au.com.shawware.kenken.model.Cage.MINUS;

/**
 * Solves cages with the minus operation.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
class MinusRule extends AbstractPairRule
{
    @SuppressWarnings("boxing")
    private static final Function<Integer, BiFunction<Integer, Integer, Boolean>> SUBTRACTION = value -> (i1, i2) -> (Math.abs(i1 - i2) == value);

    MinusRule()
    {
        super("Minus", MINUS, SUBTRACTION); //$NON-NLS-1$
    }
}
