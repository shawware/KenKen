/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service;

import java.util.Set;

import au.com.shawware.kenken.model.Cage;

/**
 * An observer API for receiving progress updates for a {@link IKenKenSolver}.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public interface IKenKenSolverObserver
{
    void start();

    void tryingCage(Cage cage);

    void cage(Cage cage, boolean change);
    
    void nakedSingles();

    void square(int x, int y, Set<Integer> removedValues);

    void finish(boolean success);
}
