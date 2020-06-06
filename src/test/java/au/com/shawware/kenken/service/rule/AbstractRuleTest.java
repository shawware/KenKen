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

    @Test
    public void testName()
    {
        TestRule rule = new TestRule(RULE_NAME, Collections.emptyList(), true, EQUALS);
        
        assertEquals(RULE_NAME, rule.name());
    }

    @Test
    public void testConstructionWithReadyCages()
    {
        List<Cage> cages = Arrays.asList(
                buildCage(EQUALS, 1, new int[][] {{ 0, 0 }}),
                buildCage(PLUS,   2, new int[][] {{ 1, 0 }, {1, 1}}),
                buildCage(TIMES,  3, new int[][] {{ 0, 1 }, {0, 2}})
        );

        TestRule rule = new TestRule(RULE_NAME, cages, true, EQUALS);

        assertThat(rule.getCages(), equalTo(cages));
    }

    @Test
    public void testConstructionWithRawCages()
    {
        Cage p1 = buildCage(PLUS, 2, new int[][] {{ 1, 0 }, {1, 1}, {1, 2}});
        Cage p2 = buildCage(PLUS, 2, new int[][] {{ 1, 0 }, {1, 1}});

        List<Cage> cages = Arrays.asList(
                buildCage(EQUALS, 1, new int[][] {{ 0, 0 }}),
                p1,
                buildCage(TIMES,  3, new int[][] {{ 0, 1 }, {0, 2}}),
                p2
        );

        TestRule rule = new TestRule(RULE_NAME, cages, false, PLUS);

        assertThat(rule.getCages(), equalTo(Arrays.asList(p2, p1)));
    }

    @Test
    public void testExhaustionWithNoCages()
    {
        TestRule rule = new TestRule(RULE_NAME, Collections.emptyList(), true, EQUALS);

        GridState gridState = new GridState(1, Collections.emptyList());
        
        rule.applyTo(gridState);
        
        assertEquals(0, rule.getExecutionCount());
    }

    @Test
    public void testExhaustionWithCages()
    {
        Cage cage = buildCage(EQUALS, 1, new int[][] {{ 0, 0 }});
        List<Cage> cages = Collections.singletonList(cage);
        
        TestRule rule = new TestRule(RULE_NAME, cages, true, EQUALS);

        GridState gridState = new GridState(1, cages);
        
        rule.applyTo(gridState);
        assertEquals(1, rule.getExecutionCount());

        gridState.processNakedSingles();

        rule.applyTo(gridState);
        assertEquals(1, rule.getExecutionCount());
    }
}
