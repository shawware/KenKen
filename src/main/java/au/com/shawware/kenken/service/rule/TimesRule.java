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

    TimesRule(int gridSize, List<Cage> cages)
    {
        super("Times", gridSize, TIMES, cages, MULTIPLICATION); //$NON-NLS-1$
    }
}
