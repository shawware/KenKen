/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.util.StringUtil;

import static au.com.shawware.kenken.model.Cage.EQUALS;
import static au.com.shawware.kenken.model.Cage.PLUS;
import static au.com.shawware.kenken.model.Cage.TIMES;
import static org.junit.Assert.assertEquals;

/**
 * Verifies the {@link RuleEngine}.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@SuppressWarnings("nls")
public class RuleEngineTest extends AbstractBaseTest
{
    // Re-use the same rules across all tests;
    private static ISolvingRule timesRule;
    private static ISolvingRule freebiesRule;
    private static ISolvingRule plusRule;

    private TallyRuleCounts counts;

    @BeforeClass
    public static void staticSetUp()
    {
        freebiesRule = new FreebiesRule();
        plusRule = new PlusRule();
        timesRule = new TimesRule(); 
    }

    @Before
    public void setUp()
    {
        counts = new TallyRuleCounts();
    }

    private RuleEngine prepareRuleEngine(List<ISolvingRule> baseSolvingRules, List<ISolvingRule> extraSolvingRules)
    {
        RuleEngine ruleEngine = new RuleEngine(baseSolvingRules, extraSolvingRules);
        ruleEngine.attach(counts);
        return ruleEngine;
    }

    @Test
    public void testEngineWhenUnsolvable()
    {
        final int gridSize = 2;

        final List<Cage> cages = Arrays.asList(
              buildCage(PLUS, 3, new int[][] {{ 0, 0 }, { 0, 1 }}),
              buildCage(PLUS, 3, new int[][] {{ 1, 0 }, { 1, 1 }})
        );

        final GridState gridState = new GridState(gridSize, cages);

        final RuleEngine ruleEngine = prepareRuleEngine(
                asList(freebiesRule, plusRule),
                asList(timesRule)
        );

        ruleEngine.solve(gridSize, cages, gridState);

        assertEquals(2, counts.noChanges("Freebies"));
        assertEquals(2, counts.noChanges("Plus"));
        assertEquals(1, counts.noChanges("Times"));
        assertEquals(0, counts.noChanges(RuleEngine.NAKED_SINGLES));

        assertEquals(0, counts.changes("Freebies"));
        assertEquals(0, counts.changes("Plus"));
        assertEquals(0, counts.changes("Times"));
        assertEquals(0, counts.changes(RuleEngine.NAKED_SINGLES));
    }

    @Test
    public void testEngineWhenBaseRulesSolve()
    {
        final int gridSize = 2;

        final List<Cage> cages = Arrays.asList(
              buildCage(EQUALS, 1, new int[][] {{ 0, 0 }}),
              buildCage(TIMES,  4, new int[][] {{ 0, 1 }, { 1, 0 }, { 1, 1 }})
        );

        final GridState gridState = new GridState(gridSize, cages);

        final RuleEngine ruleEngine = prepareRuleEngine(
                asList(freebiesRule, plusRule, timesRule),
                Collections.emptyList()
        );

        ruleEngine.solve(gridSize, cages, gridState);

        assertEquals(1, counts.noChanges("Freebies"));
        assertEquals(2, counts.noChanges("Plus"));
        assertEquals(2, counts.noChanges("Times"));
        assertEquals(0, counts.noChanges(RuleEngine.NAKED_SINGLES));

        assertEquals(1, counts.changes("Freebies"));
        assertEquals(0, counts.changes("Plus"));
        assertEquals(0, counts.changes("Times"));
        assertEquals(1, counts.changes(RuleEngine.NAKED_SINGLES));
    }

    @Test
    public void testEngineWhenExtraRulesSolve()
    {
        final int gridSize = 3;

        final List<Cage> cages = Arrays.asList(
              buildCage(EQUALS, 3, new int[][] {{ 0, 2 }}),
              buildCage(PLUS,   4, new int[][] {{ 0, 1 }, { 1, 0 }, { 0, 0 }}),
              buildCage(PLUS,   6, new int[][] {{ 1, 1 }, { 1, 2 }, { 2, 2 }}),
              buildCage(TIMES,  6, new int[][] {{ 2, 0 }, { 2, 1 }})
        );

        final GridState gridState = new GridState(gridSize, cages);

        final RuleEngine ruleEngine = prepareRuleEngine(
                asList(freebiesRule, plusRule),
                asList(timesRule)
        );

        ruleEngine.solve(gridSize, cages, gridState);

        assertEquals(3, counts.noChanges("Freebies"));
        assertEquals(3, counts.noChanges("Plus"));
        assertEquals(1, counts.noChanges("Times"));
        assertEquals(2, counts.noChanges(RuleEngine.NAKED_SINGLES));

        assertEquals(1, counts.changes("Freebies"));
        assertEquals(1, counts.changes("Plus"));
        assertEquals(1, counts.changes("Times"));
        assertEquals(1, counts.changes(RuleEngine.NAKED_SINGLES));
    }
    
    @SuppressWarnings("static-method")
    private List<ISolvingRule> asList(ISolvingRule... rules)
    {
        List<ISolvingRule> list = new ArrayList<>();
        for (ISolvingRule rule : rules)
        {
            list.add(rule);
        }
        return list;
    }
}

class TallyRuleCounts implements Observer
{
    private final Map<String, Data> counts;

    public TallyRuleCounts()
    {
        counts = new HashMap<>();
    }

    @Override
    public void rule(String name, boolean changedState)
    {
        Data count = counts.computeIfAbsent(name, key -> new Data());
        if (changedState)
        {
            count.change.incrementAndGet();
        }
        else
        {
            count.noChange.incrementAndGet();
        }
    }
    
    int changes(String name)
    {
        return counts.getOrDefault(name, new Data()).change.intValue();
    }

    int noChanges(String name)
    {
        return counts.getOrDefault(name, new Data()).noChange.intValue();
    }

    @Override
    public String toString()
    {
        return StringUtil.toString(counts);
    }
    
    class Data
    {
        AtomicInteger change;
        AtomicInteger noChange;

        Data()
        {
            change = new AtomicInteger(0);
            noChange = new AtomicInteger(0);
        }
    }
}