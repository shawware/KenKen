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
 * A rule that solves a grid's columns.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
class ColumnRule extends AbstractPlusRule
{
    ColumnRule(List<Cage> cages)
    {
        super("Column", cages, true); //$NON-NLS-1$
    }
}
