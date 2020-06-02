/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service;

import java.util.List;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.GridSpecification;
import au.com.shawware.kenken.model.Square;
import au.com.shawware.util.issues.IssueHolder;

import static au.com.shawware.kenken.model.Cage.DIVIDE;
import static au.com.shawware.kenken.model.Cage.EQUALS;
import static au.com.shawware.kenken.model.Cage.MINUS;
import static au.com.shawware.kenken.model.Cage.PLUS;
import static au.com.shawware.kenken.model.Cage.TIMES;
import static au.com.shawware.kenken.model.Cage.VALID_OPERATIONS;

/**
 * Verifies a grid specification is valid.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@SuppressWarnings("static-method")
public class GridSpecificationVerifier
{

    public IssueHolder verifyGridSpecification(GridSpecification specification)
        throws IllegalArgumentException
    {
        if (specification == null)
        {
            throw new IllegalArgumentException("Missing grid specification"); //$NON-NLS-1$
        }

        IssueHolder issues = new IssueHolder();

        verifyGridSize(specification.getSize(), issues);
        
        // No point continuing if the size is invalid
        if (!issues.isOkay())
        {
            return issues;
        }

        verifyCages(specification.getSize(), specification.getCages(), issues);

        return issues;
    }

    @SuppressWarnings("boxing")
    private void verifyGridSize(int gridSize, IssueHolder issues)
    {
        if (gridSize < 2)
        {
            issues.addError(String.format("Grid size is too small: %d", gridSize)); //$NON-NLS-1$
        }
    }

    private void verifyCages(int gridSize, List<Cage> cages, IssueHolder issues)
    {
        if ((cages == null) || cages.isEmpty())
        {
            issues.addError("Grid is missing cages"); //$NON-NLS-1$
            return;
        }

        int[][] coverage = new int[gridSize][gridSize];

        cages.forEach(cage -> verifyCage(gridSize, cage, coverage, issues));

        verifyCoverage(coverage, issues);
    }

    private void verifyCage(int gridSize, Cage cage, int[][] coverage, IssueHolder issues)
    {
        boolean validOperation = verifyOperation(cage, issues);
        verifyValue(cage, issues);

        List<Square> squares = cage.getSquares();

        if ((squares == null) || squares.isEmpty())
        {
            issues.addError("Cage does not have any squares"); //$NON-NLS-1$
        }
        else
        {
            boolean allSquaresValid = verifySquares(gridSize, squares, coverage, issues);
            if (allSquaresValid)
            {
                if (validOperation)
                {
                    verifyNumberOfSquares(cage.getOperation(), squares.size(), issues);
                }
                verifyContiguousity(gridSize, cage.getSquares(), issues);
            }
        }
    }

    private boolean verifyOperation(Cage cage, IssueHolder issues)
    {
        boolean validOperation = true;
        String operation = cage.getOperation();
        if ((operation == null) || (operation.length() != 1) || !VALID_OPERATIONS.contains(operation))
        {
            issues.addError(String.format("Invalid operation \"%s\" in cage %s", operation, cage)); //$NON-NLS-1$
            validOperation  = false;
        }
        return validOperation;
    }

    @SuppressWarnings("boxing")
    private void verifyValue(Cage cage, IssueHolder issues)
    {
        // TODO: more sophisticated value checking?
        // TODO: DIVIDE 1 should be prevented.
        int value = cage.getValue();
        if (value <= 0)
        {
            issues.addError(String.format("Invalid value \"%d\" in cage %s", value, cage)); //$NON-NLS-1$
        }
    }

    @SuppressWarnings("boxing")
    private boolean verifySquares(int size, List<Square> squares, int[][] coverage, IssueHolder issues)
    {
        boolean allSquaresValid = true;
        for (Square square : squares)
        {
            int x = square.getX();
            int y = square.getY();
            if ((x < 0) || (x >= size))
            {
                issues.addError(String.format("Square x-coordinate [%d] is out range in: %s", x, square)); //$NON-NLS-1$
                allSquaresValid = false;
            }
            else if ((y < 0) || (y >= size))
            {
                issues.addError(String.format("Square y-coordinate [%d] is out range in: %s", y, square)); //$NON-NLS-1$
                allSquaresValid = false;
            }
            else
            {
                coverage[x][y]++;
            }
        }
        return allSquaresValid;
    }

    @SuppressWarnings("incomplete-switch")
    private void verifyNumberOfSquares(String operation, int size, IssueHolder issues)
    {
        switch (operation)
        {
            case EQUALS :
                if (size != 1)
                {
                    issues.addError("= operation does not have exactly one square"); //$NON-NLS-1$
                }
                break;

            case MINUS :
            case DIVIDE :
                if (size != 2)
                {
                    issues.addError(String.format("%s operation does not have exactly two squares", operation)); //$NON-NLS-1$
                }
                break;

            case PLUS :
            case TIMES :
                if (size < 2)
                {
                    issues.addError(String.format("%s operation does not have two or more squares", operation)); //$NON-NLS-1$
                }
                break;
        }
    }

    @SuppressWarnings("boxing")
    private void verifyCoverage(int[][] coverage, IssueHolder issues)
    {
        for (int x=0; x<coverage.length; x++)
        {
            for (int y=0; y<coverage[x].length; y++)
            {
                if (coverage[x][y] == 0)
                {
                    issues.addError(String.format("The square at [%d][%d] is not included in a cage", x, y)); //$NON-NLS-1$
                }
                else if (coverage[x][y] > 1)
                {
                    issues.addError(String.format("The square at [%d][%d] is included %d times", x, y, coverage[x][y])); //$NON-NLS-1$
                }
            }
        }
    }

    // Package visible for testing only.
    final void verifyContiguousity(int gridSize, List<Square> squares, IssueHolder issues)
    {
        if (squares.size() == 1)
        {
            return;
        }

        boolean[] contiguous = new boolean[squares.size()];
        for (int i = 0; i < squares.size(); i++)
        {
            if (contiguous[i])
            {
                continue;
            }
            Square s1 = squares.get(i);
            for (int j = 0; j < squares.size(); j++)
            {
                if (j == i)
                {
                    continue;
                }
                Square s2 = squares.get(j);
                boolean s1s2Contiguous = false;
                if (s1.getX() == s2.getX())
                {
                    if (((s1.getY() > 0) && (s2.getY() == (s1.getY() - 1))) ||
                        ((s1.getY() < (gridSize - 1)) && (s2.getY() == (s1.getY() + 1))))
                    {
                        s1s2Contiguous = true;
                    }
                }
                else if (s1.getY() == s2.getY())
                {
                    if (((s1.getX() > 0) && (s2.getX() == (s1.getX() - 1))) ||
                        ((s1.getX() < (gridSize - 1)) && (s2.getX() == (s1.getX() + 1))))
                    {
                        s1s2Contiguous = true;
                    }
                }
                if (s1s2Contiguous)
                {
                    contiguous[i] = true;
                    contiguous[j] = true;
                }
            }
        }
        for (int i = 0; i < contiguous.length; i++)
        {
            if (!contiguous[i])
            {
                issues.addError(String.format("Squares are not contiguous: %s", squares)); //$NON-NLS-1$
                break;
            }
        }
    }
}
    