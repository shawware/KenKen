/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.List;

import au.com.shawware.kenken.model.Cage;

/**
 * Solves cages with the addition operation.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
class PlusRule extends AbstractPlusRule
{
    PlusRule(int gridSize, List<Cage> cages)
    {
        super("Plus", gridSize, cages); //$NON-NLS-1$
    }
}
