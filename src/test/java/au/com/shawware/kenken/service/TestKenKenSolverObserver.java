/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service;

import java.io.PrintStream;
import java.util.Set;

import au.com.shawware.kenken.model.Cage;

/**
 * A simple observer that dumps to a stream.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 *
 */
@SuppressWarnings({ "nls", "boxing" })
public class TestKenKenSolverObserver implements IKenKenSolverObserver
{
    private final PrintStream stream;
    
    public TestKenKenSolverObserver(PrintStream stream)
    {
        this.stream = stream;
    }

    @Override
    public void start()
    {
        stream.println("Solving commenced");
        stream.println("=================");
    }

    @Override
    public void tryingCage(Cage cage)
    {
        stream.format("Trying cage: %s\n", cage);
    }

    @Override
    public void cage(Cage cage, boolean change)
    {
        stream.format("Cage: %s %d done: change: %b\n", cage.getType(), cage.getId(), change);
    }

    @Override
    public void nakedSingles()
    {
        stream.format("Clean up naked singles\n");
    }

    @Override
    public void square(int x, int y, Set<Integer> removedValues)
    {
        stream.format("[%d, %d]: remove %s\n", x, y, removedValues.toString());
    }

    @Override
    public void finish(boolean success)
    {
        stream.format("Solving complete, success: %b\n", success);
    }
}
