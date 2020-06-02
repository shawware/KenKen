/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.util.StringUtil;

/**
 * Holds the state for a single cage.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
class CageState
{
    private final Cage cage;

    private List<SquareState> squareStates;
    private boolean initialised;
    private boolean solved;

    CageState(Cage cage)
    {
        this.cage = cage;
        this.squareStates = Collections.emptyList();
        this.initialised = false;
        this.solved = false;
    }

    boolean isSolved()
    {
        if (!solved)
        {
            solved = squareStates.stream().allMatch(SquareState::isSolved);
        }
        return solved;
    }

    // TODO: used
    boolean isInitialised()
    {
        return initialised;
    }

    @SuppressWarnings("hiding")
    void initialise(SquareState[][] gridState)
    {
        if (!initialised)
        {
            initialised = true;
            List<SquareState> squareStates = new ArrayList<>();
            cage.getSquares().forEach(square -> squareStates.add(gridState[square.getX()][square.getY()]));
            this.squareStates = Collections.unmodifiableList(squareStates);
        }
    }

    Cage getCage()
    {
        return cage;
    }

    List<SquareState> getSquareStates()
    {
        return squareStates;
    }

    @Override
    @SuppressWarnings("boxing")
    public String toString()
    {
        return StringUtil.toString(initialised, solved, cage, squareStates);
    }
}
