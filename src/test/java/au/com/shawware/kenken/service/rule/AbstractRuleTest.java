/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import au.com.shawware.kenken.model.Cage;

import static au.com.shawware.kenken.model.Cage.EQUALS;
import static au.com.shawware.kenken.model.Cage.PLUS;
import static au.com.shawware.kenken.model.Cage.TIMES;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Test the algorithms in {@link AbstractRule}.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@SuppressWarnings({ "nls", "static-method" })
public class AbstractRuleTest extends AbstractBaseTest
{
    private static final String RULE_NAME = "Test";
    private static final int GRID_SIZE = 3;

    private GridState gridState;

    private Cage c1;
    private Cage c2;
    private Cage c3;
    private Cage c4;

    private List<Cage> cages;

    @Before
    public void setUp()
    {
        c1 = buildCage(PLUS,   5, new int[][] {{ 1, 0 }, {1, 1}, {1, 2}});
        c2 = buildCage(EQUALS, 1, new int[][] {{ 0, 0 }});
        c3 = buildCage(PLUS,   3, new int[][] {{ 1, 0 }, {1, 1}});
        c4 = buildCage(TIMES,  3, new int[][] {{ 0, 1 }, {0, 2}});

        cages = Arrays.asList(c1, c2, c3, c4);
        
        gridState = new GridState(GRID_SIZE, cages);
    }
    
    @Test
    public void testName()
    {
        TestRule rule = new TestRule(RULE_NAME, EQUALS, false, false);

        rule.initialise(GRID_SIZE, Collections.emptyList(), gridState);

        assertEquals(RULE_NAME, rule.name());
    }

    @Test
    public void testInitialisationWithUnchangedCages()
    {
        TestRule rule = new TestRule(RULE_NAME, EQUALS, false, false);

        rule.initialise(GRID_SIZE, cages, gridState);

        assertThat(rule.getCages(), equalTo(cages));
    }

    @Test
    public void testInitialisationWithFilteredAndUnsortedCages()
    {
        TestRule rule = new TestRule(RULE_NAME, PLUS, true, false);

        rule.initialise(GRID_SIZE, cages, gridState);

        assertThat(rule.getCages(), equalTo(Arrays.asList(c1, c3)));
    }

    @Test
    public void testInitialisationWithUnfilteredAndSortedRawCages()
    {
        TestRule rule = new TestRule(RULE_NAME, PLUS, false, true);

        rule.initialise(GRID_SIZE, cages, gridState);

        assertThat(rule.getCages(), equalTo(Arrays.asList(c2, c3, c4, c1)));
    }
    
    @Test
    public void testInitialisationWithFilteredAndSortedRawCages()
    {
        TestRule rule = new TestRule(RULE_NAME, PLUS, true, true);

        rule.initialise(GRID_SIZE, cages, gridState);

        assertThat(rule.getCages(), equalTo(Arrays.asList(c3, c1)));
    }

    @Test
    public void testExhaustionWithNoCages()
    {
        TestRule rule = new TestRule(RULE_NAME, EQUALS, true, false);

        rule.applyTo(new GridState(GRID_SIZE, Collections.emptyList()));
        
        assertEquals(0, rule.getExecutionCount());
    }

    @SuppressWarnings("hiding")
    @Test
    public void testExhaustionWithCages()
    {
        TestRule rule = new TestRule(RULE_NAME, EQUALS, true, false);

        // We need a solvable cage for this test.
        List<Cage> cages = Arrays.asList(c2);
        GridState gridState = new GridState(1, Arrays.asList(c2));

        rule.initialise(gridState.getGridSize(), cages, gridState);

        rule.applyTo(gridState);

        assertEquals(1, rule.getExecutionCount());

        gridState.processNakedSingles();

        rule.applyTo(gridState);
        assertEquals(1, rule.getExecutionCount());
    }
}
