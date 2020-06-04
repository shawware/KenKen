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
import au.com.shawware.util.StringUtil;

/**
 * Solves a KenKen puzzle by iterating over a set of rules.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class RuleBasedSolver implements IKenKenSolver
{
    private final int gridSize;
    private final List<Cage> cages;
    private final SquareState[][] gridState;
    private final List<ISolvingRule> baseSolvingRules;
    private final List<ISolvingRule> extraSolvingRules;
    private final ISolvingRule singlesRule;

    public RuleBasedSolver(GridSpecification specification)
    {
        this.gridSize = specification.getSize();
        this.cages = specification.getCages();
        this.gridState = buildGridState();
        this.baseSolvingRules = buildBaseSolvingRules();
        this.extraSolvingRules = buildExtraSolvingRules();
        this.singlesRule = new SinglesRule(gridSize, cages);
    }

    @SuppressWarnings("hiding")
    private SquareState[][] buildGridState()
    {
        final SquareState[][] gridState = new SquareState[gridSize][gridSize];
        cages.forEach(cage ->
            cage.getSquares().forEach(square ->
                gridState[square.getX()][square.getY()] = new SquareState(gridSize, square)
            )
        );
        return gridState;
    }

    private List<ISolvingRule> buildBaseSolvingRules()
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

    private List<ISolvingRule> buildExtraSolvingRules()
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
            System.out.format("After rule %s: %s\n", rule.name(), StringUtil.arrayToString(gridState));
        }
        return change;
    }

    @Override
    public boolean gridIsSolved()
    {
        boolean isSolved = true;
        out:
        for (int x=0; x<gridSize; x++)
        {
            for (int y=0; y<gridSize; y++)
            {
                if (!gridState[x][y].isSolved())
                {
                    isSolved = false;
                    break out;
                }
            }
        }
        return isSolved;
    }

    @Override
    public int[][] solution()
    {
        int[][] solution = new int[gridSize][gridSize];
        for (int x = 0; x < gridSize; x++)
        {
            for (int y = 0; y < gridSize; y++)
            {
                solution[x][y] = gridState[x][y].value();
            }
        }
        return solution;
    }
}
