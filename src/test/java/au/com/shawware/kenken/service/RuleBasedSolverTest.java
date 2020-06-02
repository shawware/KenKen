/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import au.com.shawware.kenken.model.Grid;
import au.com.shawware.kenken.model.GridSpecification;
import au.com.shawware.kenken.model.IGridVisitor;
import au.com.shawware.kenken.service.rule.RuleBasedSolver;
import au.com.shawware.util.issues.IssueHolder;

import static org.junit.Assert.assertEquals;

/**
 * Verify the operation of {@link RuleBasedSolver}.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@SuppressWarnings("nls")
public class RuleBasedSolverTest extends AbstractBaseTest
{
    private GridSpecificationVerifier verifier;

    @Before
    public void setUp()
    {
        verifier = new GridSpecificationVerifier();
    }

    @Test
    public void testSolvable2x2() throws IOException
    {
        testProblem("kk-2x2-p1.json", "21,12");
    }

    @Test
    public void testUnsolvable2x2() throws IOException
    {
        testProblem("kk-2x2-p2.json", "--,--");
    }

    @Test
    public void testProblem1() throws IOException
    {
        testProblem("kk-3x3-p1.json", "213,132,321");
    }

    @Test
    public void testProblem2() throws IOException
    {
        testProblem("kk-3x3-p2.json", "231,123,312");
    }

    @Test
    public void testProblem3() throws IOException
    {
        testProblem("kk-3x3-p3.json", "231,312,123");
    }

    @Test
    public void testProblem4() throws IOException
    {
        testProblem("kk-4x4-p1.json", "3142,1234,4321,2413");
    }

    @Test
    public void testProblem5() throws IOException
    {
        testProblem("kk-4x4-p2.json", "3241,2413,4132,1324");
    }

    @Test
    public void testProblem6() throws IOException
    {
        testProblem("kk-4x4-p3.json", "1324,3412,4231,2143");
    }

    @Test
    public void testProblem7() throws IOException
    {
        testProblem("kk-6x6-p1.json", "143526,352641,461352,536214,624135,215463");
    }

    @Test
    public void testProblem8() throws IOException
    {
        testProblem("kk-7x7-p1.json", "7432561,2761345,4317652,5243716,3654127,1576234,6125473");
    }

    private void testProblem(String filename, String expectedSolution) throws IOException
    {
        GridSpecification specification = loadAndVerifyGridSpecification(filename);
        
        IKenKenSolver solver = new RuleBasedSolver(specification);

        solver.solve();
        
        Grid grid = new Grid(solver.solution());

        IGridVisitor visitor = new StringGridVisitor(',');
        grid.accept(visitor);

        assertEquals(expectedSolution, visitor.toString());
    }

    private GridSpecification loadAndVerifyGridSpecification(String filename) throws IOException
    {
        GridSpecification specification = loadGridSpecification(filename);
        IssueHolder issues = verifier.verifyGridSpecification(specification);
        assertEquals(issues.toString(), 0,  issues.numberOfErrors());
        return specification;
    }
}
