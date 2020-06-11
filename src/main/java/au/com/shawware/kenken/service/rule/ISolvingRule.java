/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.List;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.service.IKenKenSolverObserver;

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
     * Initialise the rule from the given grid specification and state prior to solving.
     *
     * @param gridSize the grid's size
     * @param cages the grid's cages
     * @param gridState the current grid state
     * @param observer the observer to report progress to
     */
    void initialise(int gridSize, List<Cage> cages, GridState gridState, IKenKenSolverObserver observer);

    /**
     * Apply this rule to the given grid state - which is <em>updated</em> accordingly.
     *
     * @param gridState the current grid state
     */
    void applyTo(GridState gridState);
}
