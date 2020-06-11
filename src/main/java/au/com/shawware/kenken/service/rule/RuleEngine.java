/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.Grid;
import au.com.shawware.kenken.service.IKenKenSolverObserver;

/**
 * A simple engine for processing {@link ISolvingRule}s until completion.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class RuleEngine
{
    static final String NAKED_SINGLES = "Naked Singles"; //$NON-NLS-1$

    private final List<ISolvingRule> baseSolvingRules;
    private final List<ISolvingRule> extraSolvingRules;
    private final Set<Observer> observers;

    public RuleEngine(List<ISolvingRule> baseSolvingRules, List<ISolvingRule> extraSolvingRules)
    {
        this.baseSolvingRules = baseSolvingRules;
        this.extraSolvingRules = extraSolvingRules;
        this.observers = new HashSet<>();
    }

    public void attach(Observer observer)
    {
        observers.add(observer);
    }

    private void notify(String ruleName, boolean change)
    {
        observers.forEach(observer -> observer.rule(ruleName, change));
    }

    public Grid solve(int gridSize, List<Cage> cages, GridState gridState, IKenKenSolverObserver observer)
    {
        for (ISolvingRule rule : baseSolvingRules)
        {
            rule.initialise(gridSize, cages, gridState, observer);
        }

        int maxAttempts = extraSolvingRules.size() + 1;
        while (maxAttempts > 0)
        {
            while (applyRules(gridState, observer))
            {
                // Keep going until the grid is solved or the rules stop having an effect
                if (gridState.isSolved())
                {
                    break;
                }
            }
            maxAttempts--;
            if (!extraSolvingRules.isEmpty())
            {
                ISolvingRule rule = extraSolvingRules.remove(0);
                rule.initialise(gridSize, cages, gridState, observer);
                baseSolvingRules.add(rule);
            }
        }
        return new Grid(gridState.solution());
    }

    private boolean applyRules(GridState gridState, IKenKenSolverObserver observer)
    {
        boolean change = false;
        for (ISolvingRule rule : baseSolvingRules)
        {
            gridState.markUnchanged();
            rule.applyTo(gridState);
            notify(rule.name(), gridState.isChanged());
            if (gridState.isChanged())
            {
                gridState.markUnchanged();
                observer.nakedSingles();
                gridState.processNakedSingles();
                notify(NAKED_SINGLES, gridState.isChanged());
                change = true;
            }
        }
        return change;
    }
}

interface Observer
{
    void rule(String name, boolean changedState);
}
