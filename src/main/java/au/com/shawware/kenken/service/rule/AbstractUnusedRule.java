/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.List;
import java.util.Set;

import au.com.shawware.kenken.model.Cage;

/**
 * Base class for rules that work on finding un-used values.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
abstract class AbstractUnusedRule extends AbstractRule
{
    AbstractUnusedRule(String name)
    {
        super(name);
    }

    abstract List<Set<Integer>> findUnusedValues(Cage cage, List<SquareState> squareState);
}
