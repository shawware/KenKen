/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

/**
 * Solves cages with the addition operation.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
class PlusRule extends AbstractPlusRule
{
    PlusRule()
    {
        super("Plus", true, true); //$NON-NLS-1$
    }
}
