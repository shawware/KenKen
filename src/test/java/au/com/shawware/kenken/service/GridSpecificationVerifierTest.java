/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import au.com.shawware.kenken.model.GridSpecification;
import au.com.shawware.kenken.model.Square;
import au.com.shawware.util.issues.IssueHolder;

/**
 * Verify the operation of {@link GridSpecificationVerifier}.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@SuppressWarnings("nls")
public class GridSpecificationVerifierTest extends AbstractBaseTest
{
    private GridSpecificationVerifier verifier;

    @Before
    public void setUp()
    {
        verifier = new GridSpecificationVerifier();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullGrid()
    {
        verifier.verifyGridSpecification(null);
    }

    @Test
    public void testInvalidSize() throws IOException
    {
        final String specification = "{ \"size\": 1 }";
        
        GridSpecification grid = readGridSpecification(specification);

        IssueHolder issues = verifier.verifyGridSpecification(grid);
        
        assertFalse(issues.isOkay());
        assertEquals(1, issues.numberOfErrors());
        assertEquals(0, issues.numberOfWarnings());
    }

    @Test
    public void testInvalidSquareCoordinate() throws IOException
    {
        final String filename = "kk-2x2-invalid-coord.json";
        
        GridSpecification grid = loadGridSpecification(filename);

        IssueHolder issues = verifier.verifyGridSpecification(grid);
        
        assertFalse(issues.isOkay());
        assertEquals(2, issues.numberOfErrors());
        assertEquals(0, issues.numberOfWarnings());
    }

    @Test
    public void testDuplicateCoverage() throws IOException
    {
        final String filename = "kk-2x2-duplicate.json";
        
        GridSpecification grid = loadGridSpecification(filename);

        IssueHolder issues = verifier.verifyGridSpecification(grid);
        
        assertFalse(issues.isOkay());
        assertEquals(1, issues.numberOfErrors());
        assertEquals(0, issues.numberOfWarnings());
    }

    @Test
    public void testEmptyCage() throws IOException
    {
        final String filename = "kk-2x2-empty-cage.json";
        
        GridSpecification grid = loadGridSpecification(filename);

        IssueHolder issues = verifier.verifyGridSpecification(grid);
        
        assertFalse(issues.isOkay());
        assertEquals(1, issues.numberOfErrors());
        assertEquals(0, issues.numberOfWarnings());
    }

    @Test
    public void testIncompleteCoverage() throws IOException
    {
        final String filename = "kk-2x2-incomplete.json";
        
        GridSpecification grid = loadGridSpecification(filename);

        IssueHolder issues = verifier.verifyGridSpecification(grid);
        
        assertFalse(issues.isOkay());
        assertEquals(1, issues.numberOfErrors());
        assertEquals(0, issues.numberOfWarnings());
    }

    @Test
    public void testInvalidOperation() throws IOException
    {
        final String filename = "kk-2x2-invalid-op.json";
        
        GridSpecification grid = loadGridSpecification(filename);

        IssueHolder issues = verifier.verifyGridSpecification(grid);
        
        assertFalse(issues.isOkay());
        assertEquals(2, issues.numberOfErrors());
        assertEquals(0, issues.numberOfWarnings());
    }

    @Test
    public void testInvalidValue() throws IOException
    {
        final String filename = "kk-2x2-invalid-value.json";
        
        GridSpecification grid = loadGridSpecification(filename);

        IssueHolder issues = verifier.verifyGridSpecification(grid);
        
        assertFalse(issues.isOkay());
        assertEquals(2, issues.numberOfErrors());
        assertEquals(0, issues.numberOfWarnings());
    }
    
    @Test
    public void testInvalidNumberOfSquares() throws IOException
    {
        final String filename = "kk-3x3-invalid-squares.json";
        
        GridSpecification grid = loadGridSpecification(filename);

        IssueHolder issues = verifier.verifyGridSpecification(grid);
        
        assertFalse(issues.isOkay());
        assertEquals(6, issues.numberOfErrors());
        assertEquals(0, issues.numberOfWarnings());
    }

    @Test
    public void testVerifyContiguousityForValidCages()
    {
        final int[][][] fixture =
        {
            { { 0, 0 } },
            { { 0, 0 }, { 0, 1 } },
            { { 0, 0 }, { 1, 0 } },
            { { 0, 0 }, { 0, 1 }, { 1, 0 } },
            { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } },
            { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 } },

            { { 0, 3 } },
            { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 } },

            { { 1, 1 }, { 2, 2 }, { 2, 1 }, { 1, 2 } },
            { { 1, 1 }, { 2, 2 }, { 2, 1 }, { 3, 3 }, { 2, 3 } },
            { { 2, 0 }, { 1, 2 }, { 2, 2 }, { 2, 1 }, { 0, 2 } },
            { { 1, 1 }, { 1, 2 }, { 1, 3 }, { 2, 1 }, { 3, 1 }, { 3, 3 }, { 2, 3 }, { 3, 2 } },

            { { 3, 1 }, { 3, 3 }, { 3, 2 }, { 3, 0 } },
            { { 3, 0 } },

            { { 2, 3 }, { 1, 3 }, { 3, 3 }, { 0, 3 } },
            { { 3, 3 } },
        };
        
        for (int[][] cells : fixture)
        {
            List<Square> squares = createSquares(cells);
            IssueHolder issues = new IssueHolder();
            verifier.verifyContiguousity(4, squares, issues);
            assertEquals("Squares: " + squares, 0, issues.numberOfErrors());
            assertEquals(0, issues.numberOfWarnings());
        }
    }

    @Test
    public void testVerifyContiguousityForInvalidCages()
    {
        final int[][][] fixture =
        {
            { { 0, 0 }, { 0, 2 } },
            { { 0, 0 }, { 1, 1 } },
            { { 0, 0 }, { 2, 0 } },
            { { 0, 3 }, { 1, 2 } },
            { { 3, 0 }, { 2, 1 } },
            { { 3, 3 }, { 2, 2 } },
 
            { { 0, 0 }, { 0, 2 }, { 0, 3 } },
            { { 2, 1 }, { 0, 1 }, { 3, 1 } },

            { { 1, 1 }, { 1, 2 }, { 3, 1 }, { 2, 2 }, { 2, 3 } },
        };

        for (int[][] cells : fixture)
        {
            List<Square> squares = createSquares(cells);
            IssueHolder issues = new IssueHolder();
            verifier.verifyContiguousity(4, squares, issues);
            assertEquals(1, issues.numberOfErrors());
            assertEquals(0, issues.numberOfWarnings());
        }
    }

    @SuppressWarnings("static-method")
    private List<Square> createSquares(int[][] cells)
    {
        List<Square> squares = new ArrayList<>(cells.length);
        for (int[] cell: cells)
        {
            Square square = new Square(cell[0], cell[1]);
            squares.add(square);
        }
        return squares;
    }
}
