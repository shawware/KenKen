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

import static au.com.shawware.kenken.model.Cage.PLUS;

/**
 * Solves cages with the addition operation.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
abstract class AbstractPlusRule extends AbstractPermutationRule
{
    @SuppressWarnings("boxing")
    private static final Function<Integer, BiFunction<Integer, Integer, Integer>> ADDITION = value -> (i1, i2) -> i1 + i2;

    AbstractPlusRule(String name, List<Cage> cages, boolean sort)
    {
        super(name, PLUS, cages, sort, ADDITION);
    }
}
