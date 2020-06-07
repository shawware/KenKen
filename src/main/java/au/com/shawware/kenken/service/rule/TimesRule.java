/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.function.BiFunction;
import java.util.function.Function;

import static au.com.shawware.kenken.model.Cage.TIMES;

/**
 * Solves cages with the multiplication operation.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
class TimesRule extends AbstractPermutationRule
{
    @SuppressWarnings("boxing")
    private static final Function<Integer, BiFunction<Integer, Integer, Integer>> MULTIPLICATION = value -> (i1, i2) -> i1 * i2;

    TimesRule()
    {
        super("Times", TIMES, true, true, MULTIPLICATION); //$NON-NLS-1$
    }
}
