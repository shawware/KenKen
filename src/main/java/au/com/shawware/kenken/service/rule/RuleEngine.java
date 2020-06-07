/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.List;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.Grid;

/**
 * A simple engine for processing {@link ISolvingRule}s until completion.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class RuleEngine
{
    private final List<ISolvingRule> baseSolvingRules;
    private final List<ISolvingRule> extraSolvingRules;

    public RuleEngine(List<ISolvingRule> baseSolvingRules, List<ISolvingRule> extraSolvingRules)
    {
        this.baseSolvingRules = baseSolvingRules;
        this.extraSolvingRules = extraSolvingRules;
    }

    public Grid solve(int gridSize, List<Cage> cages, GridState gridState)
    {
        for (ISolvingRule rule : baseSolvingRules)
        {
            rule.initialise(gridSize, cages, gridState);
        }

        int maxAttempts = extraSolvingRules.size() + 1;
        while (maxAttempts > 0)
        {
            while (applyRules(gridState))
            {
                // Keep going until the grid is solved or the rules stop having an effect
            }
            if (gridState.isSolved())
            {
                break;
            }
            maxAttempts--;
            if (!extraSolvingRules.isEmpty())
            {
                ISolvingRule rule = extraSolvingRules.remove(0);
                rule.initialise(gridSize, cages, gridState);
                baseSolvingRules.add(rule);
            }
        }
        return new Grid(gridState.solution());
    }

    private boolean applyRules(GridState gridState)
    {
        boolean change = false;
        for (ISolvingRule rule : baseSolvingRules)
        {
            gridState.markUnchanged();
            rule.applyTo(gridState);
            if (gridState.isChanged())
            {
                gridState.processNakedSingles();
                change = true;
            }
        }
        return change;
    }
}
