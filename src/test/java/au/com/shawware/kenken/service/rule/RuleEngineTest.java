/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.Square;

import static au.com.shawware.kenken.model.Cage.EQUALS;
import static au.com.shawware.kenken.model.Cage.PLUS;
import static au.com.shawware.kenken.model.Cage.TIMES;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Verifies the {@link RuleEngine}.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@SuppressWarnings("nls")
public class RuleEngineTest extends AbstractBaseTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule(); 

    @Mock
    private GridState gridState;

    private TestRule rule1;
    private TestRule rule2;
    private TestRule rule3;

    private List<ISolvingRule> baseRules;
    private List<ISolvingRule> extraRules;

    private RuleEngine ruleEngine;

    @Before
    public void setUp()
    {
        List<Cage> cages = Arrays.asList(
                buildCage(EQUALS, 1, new int[][] {{ 0, 0 }}),
                buildCage(PLUS,   2, new int[][] {{ 1, 0 }, {1, 1}}),
                buildCage(TIMES,  3, new int[][] {{ 0, 1 }, {0, 2}})
        );

        rule1 = new TestRule("Test1", cages, false, EQUALS);
        rule2 = new TestRule("Test2", cages, false, PLUS);
        rule3 = new TestRule("Test3", cages, false, TIMES);

        baseRules = new ArrayList<>();
        baseRules.add(rule1);
        baseRules.add(rule2);

        extraRules = new ArrayList<>();
        extraRules.add(rule3);

        ruleEngine = new RuleEngine(baseRules, extraRules);
    }

    @Test
    public void testEngineWhenUnsolvable()
    {
        ruleEngine.solve(gridState);

        assertEquals(2, rule1.getExecutionCount());
        assertEquals(2, rule2.getExecutionCount());
        assertEquals(1, rule3.getExecutionCount());
        assertEquals(3, baseRules.size());
        assertEquals(0, extraRules.size());
    }

    @Test
    @SuppressWarnings("boxing")
    public void testEngineWhenBaseRulesSolve()
    {
        when(gridState.isChanged()).thenReturn(true, true, false);
        when(gridState.isSolved()).thenReturn(true);
        when(gridState.isSolved(any())).thenReturn(false, true, false, true);

        ruleEngine.solve(gridState);

        assertEquals(1, rule1.getExecutionCount());
        assertEquals(1, rule2.getExecutionCount());
        assertEquals(0, rule3.getExecutionCount());
        assertEquals(2, baseRules.size());
        assertEquals(1, extraRules.size());
    }

    @Test
    @SuppressWarnings("boxing")
    public void testEngineWhenExtraRulesSolve()
    {
        when(gridState.isChanged()).thenReturn(true, true, false, false, true, false, false);
        when(gridState.isSolved()).thenReturn(false, true);
        when(gridState.isSolved(new Square(0,0))).thenReturn(false, true);
        when(gridState.isSolved(new Square(1,0))).thenReturn(false, true);
        when(gridState.isSolved(new Square(0,1))).thenReturn(false, true);
        when(gridState.isSolved(new Square(0,2))).thenReturn(false, true);

        ruleEngine.solve(gridState);

        assertEquals(1, rule1.getExecutionCount());
        assertEquals(4, rule2.getExecutionCount());
        assertEquals(2, rule3.getExecutionCount());
        assertEquals(3, baseRules.size());
        assertEquals(0, extraRules.size());
    }
}
