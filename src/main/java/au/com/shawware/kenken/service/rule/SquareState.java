/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import au.com.shawware.kenken.model.Square;
import au.com.shawware.util.StringUtil;

/**
 * Holds the state of a particular square during the solving process.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
class SquareState
{
    private final Square square;
    private final Set<Integer> possibleValues;
    private int solution;
    
    SquareState(int gridSize, Square square)
    {
        this.square = square;
        this.possibleValues = IntStream
                .rangeClosed(1, gridSize)
                .boxed()
                .collect(Collectors.toSet());
        this.solution = 0;
    }

    List<Integer> getValues()
    {
        return new ArrayList<>(possibleValues);
    }

    Square getSquare()
    {
        return square;
    }

    @SuppressWarnings("boxing")
    void removeValue(int value)
    {
        possibleValues.remove(value);
    }

    void removeValues(Set<Integer> values)
    {
        possibleValues.removeAll(values);
    }

    boolean isSolved()
    {
        return solution > 0;
    }
    
    @SuppressWarnings("boxing")
    public void solve()
    {
        if (possibleValues.size() == 1)
        {
            solution = possibleValues.iterator().next();
        }
    }

    @SuppressWarnings("boxing")
    public boolean couldBeSolved()
    {
        // TODO: check if solved?
        boolean couldBeSolved = false;
        if (possibleValues.size() == 1)
        {
            solution = possibleValues.iterator().next();
            couldBeSolved = true;
        }
        return couldBeSolved;
    }

    int value()
    {
        return solution;
    }

    @Override
    @SuppressWarnings("boxing")
    public String toString()
    {
        return StringUtil.toString(square, solution, possibleValues);
    }
}
