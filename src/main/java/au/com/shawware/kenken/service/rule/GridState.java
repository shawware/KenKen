/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.Square;
import au.com.shawware.util.StringUtil;

/**
 * Holds the state of the entire grid during the solving process.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
class GridState
{
    private final int gridSize;
    private final SquareState[][] gridState;
    
    private boolean changed;

    GridState(int gridSize, List<Cage> cages)
    {
        this.gridSize = gridSize;
        this.gridState = new SquareState[gridSize][gridSize];
        
        final Set<Integer> initialValues = IntStream
                .rangeClosed(1, gridSize)
                .boxed()
                .collect(Collectors.toSet());
        
        cages.forEach(cage ->
            cage.getSquares().forEach(square ->
                gridState[square.getX()][square.getY()] = new SquareState(initialValues)
            )
        );
        
        this.changed = false;
    }

    int getGridSize()
    {
        return gridSize;
    }
    
    void markUnchanged()
    {
        changed = false;
    }
    
    boolean isChanged()
    {
        return changed;
    }

    List<Integer> getValues(Square square)
    {
        return gridState[square.getX()][square.getY()].getValues();
    }

    void removeValue(Square square, int value)
    {
        removeValue(square.getX(), square.getY(), value);
    }

    void removeValue(int x, int y, int value)
    {
        gridState[x][y].removeValue(value);
        changed = true;
    }

    void removeValues(Square square, Set<Integer> values)
    {
        gridState[square.getX()][square.getY()].removeValues(values);
        changed = true;
    }

    int value(int x, int y)
    {
        return gridState[x][y].value();
    }


    boolean isSolved(Cage cage)
    {
        return cage.getSquares().stream().allMatch(square -> isSolved(square));
    }

    boolean isSolved(Square square)
    {
        return isSolved(square.getX(), square.getY());
    }

    private boolean isSolved(int x, int y)
    {
        return gridState[x][y].isSolved();
    }

    boolean isSolved()
    {
        boolean isSolved = true;
        out:
        for (int x=0; x<gridSize; x++)
        {
            for (int y=0; y<gridSize; y++)
            {
                if (!gridState[x][y].isSolved())
                {
                    isSolved = false;
                    break out;
                }
            }
        }
        return isSolved;
    }

    int[][] solution()
    {
        int[][] solution = new int[gridSize][gridSize];
        for (int x = 0; x < gridSize; x++)
        {
            for (int y = 0; y < gridSize; y++)
            {
                solution[x][y] = gridState[x][y].value();
            }
        }
        return solution;
    }

    void processNakedSingles()
    {
        while (processSingleValues())
        {
            // Do nothing - keep going until done
        }
    }

    private boolean processSingleValues()
    {
        boolean change = false;
        for (int x = 0; x < gridSize; x++)
        {
            for (int y = 0; y < gridSize; y++)
            {
                if (!isSolved(x, y) && gridState[x][y].couldBeSolved())
                {
                    change = true;
                    removeFromRow(x, y);
                    removeFromColumn(x, y);
                }
            }
        }
        return change;
    }

    private void removeFromRow(int x, int y)
    {
        int value = value(x, y);
        for (int col = 0; col < gridSize; col++)
        {
            if ((col != x) && !isSolved(col, y))
            {
                removeValue(col, y, value);
            }
        }                
    }

    private void removeFromColumn(int x, int y)
    {
        int value = value(x, y);
        for (int row = 0; row < gridSize; row++)
        {
            if ((row != y) && !isSolved(x, row))
            {
                removeValue(x, row, value);
            }
        }                
    }
   
    @Override
    @SuppressWarnings("boxing")
    public String toString()
    {
        return StringUtil.toString(gridSize, changed, gridState);
    }
}
