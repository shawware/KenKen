/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

/**
 * The cage solving rule API.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public interface ISolvingRule
{
    /**
     * @return This rule's name.
     */
    String name();

    /**
     * Apply this rule to the given grid state - which is <em>updated</em> accordingly.
     *
     * @param gridState the current grid state
     * 
     * @return whether apply this rule changed the grid state
     */
    boolean applyTo(GridState gridState);

    /**
     * @return Whether this rule is exhausted, ie. can no longer help solve the grid.
     */
    boolean exhausted();
}
