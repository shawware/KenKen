/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.ArrayList;
import java.util.List;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.GridSpecification;
import au.com.shawware.kenken.service.IKenKenSolver;

/**
 * Solves a KenKen puzzle by iterating over a set of rules.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class RuleBasedSolver implements IKenKenSolver
{
    private final GridState gridState;
    private final List<ISolvingRule> baseSolvingRules;
    private final List<ISolvingRule> extraSolvingRules;
    private final ISolvingRule singlesRule;

    public RuleBasedSolver(GridSpecification specification)
    {
        final int gridSize = specification.getSize();
        final List<Cage> cages = specification.getCages();

        gridState = new GridState(gridSize, cages);
        baseSolvingRules = buildBaseSolvingRules(gridSize, cages);
        extraSolvingRules = buildExtraSolvingRules(gridSize, cages);
        singlesRule = new SinglesRule(gridSize, cages);
    }

    @SuppressWarnings("static-method")
    private List<ISolvingRule> buildBaseSolvingRules(int gridSize, List<Cage> cages)
    {
        List<ISolvingRule> solvingRules = new ArrayList<>();
        
        solvingRules.add(new FreebiesRule(gridSize, cages));
        solvingRules.add(new MinusRule(gridSize, cages));
        solvingRules.add(new DivideRule(gridSize, cages));
        solvingRules.add(new PlusRule(gridSize, cages));
        solvingRules.add(new TimesRule(gridSize, cages));
        solvingRules.add(new RowRule(gridSize, cages));
        solvingRules.add(new ColumnRule(gridSize, cages));
     
        return solvingRules;
    }

    @SuppressWarnings("static-method")
    private List<ISolvingRule> buildExtraSolvingRules(int gridSize, List<Cage> cages)
    {
        List<ISolvingRule> solvingRules = new ArrayList<>();
        
        solvingRules.add(new PairsRule(gridSize, cages));
        solvingRules.add(new TriplesRule(gridSize, cages));
     
        return solvingRules;
    }

    @Override
    public void solve()
    {
        int maxAttempts = extraSolvingRules.size() + 1;
        while (maxAttempts > 0)
        {
            while (applyRules())
            {
                // Keep going until the grid is solved or the rules stop having an effect
            }
            if (gridIsSolved())
            {
                break;
            }
            maxAttempts--;
            if (!extraSolvingRules.isEmpty())
            {
                baseSolvingRules.add(extraSolvingRules.remove(0));
            }
        }
    }

    private boolean applyRules()
    {
        boolean change = false;
        for (ISolvingRule rule : baseSolvingRules)
        {
            if (rule.exhausted())
            {
                System.out.format("Rule exhausted: %s\n", rule.name());
                continue;
            }
            System.out.format("Trying rule: %s\n", rule.name());
            if (rule.applyTo(gridState))
            {
                change = true;
                System.out.format("Rule changed the state: %s\n", rule.name());
                singlesRule.applyTo(gridState);
            }
            System.out.format("After rule %s: %s\n", rule.name(), gridState);
        }
        return change;
    }

    @Override
    public boolean gridIsSolved()
    {
        return gridState.isSolved();
    }

    @Override
    public int[][] solution()
    {
        return gridState.solution();
    }
}
