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
 * A null (or no-op) observer.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class NullKenKenSolverObserver implements IKenKenSolverObserver
{
    @Override
    public void start()
    {
        // Do nothing
    }

    @Override
    public void tryingCage(Cage cage)
    {
        // Do nothing
    }

    @Override
    public void cage(Cage cage, boolean change)
    {
        // Do nothing
    }

    @Override
    public void nakedSingles()
    {
        // Do nothing
    }

    @Override
    public void square(int x, int y, Set<Integer> removedValues)
    {
        // Do nothing
    }

    @Override
    public void finish(boolean success)
    {
        // Do nothing
    }
}
