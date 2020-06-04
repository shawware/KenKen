/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.List;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.Square;
import au.com.shawware.util.StringUtil;

// TODO: is this still required
/**
 * Holds the state for a single cage.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
class CageState
{
    private final Cage cage;

    private boolean solved;

    CageState(Cage cage)
    {
        this.cage = cage;
        this.solved = false;
    }

    boolean isSolved(GridState gridState)
    {
        if (!solved)
        {
            solved = cage.getSquares().stream().allMatch(square -> gridState.isSolved(square));
        }
        return solved;
    }

    Cage getCage()
    {
        return cage;
    }

    List<Square> getSquares()
    {
        return cage.getSquares();
    }

    @Override
    @SuppressWarnings("boxing")
    public String toString()
    {
        return StringUtil.toString(solved, cage);
    }
}
