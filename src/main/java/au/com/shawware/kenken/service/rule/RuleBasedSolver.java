/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.GridSpecification;
import au.com.shawware.kenken.model.Square;
import au.com.shawware.kenken.service.IKenKenSolver;

import static au.com.shawware.kenken.model.Cage.PLUS;

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
        extraSolvingRules = buildExtraSolvingRules(cages);
        singlesRule = new SinglesRule(gridSize, cages);
    }

    private List<ISolvingRule> buildBaseSolvingRules(int gridSize, List<Cage> cages)
    {
        List<ISolvingRule> solvingRules = new ArrayList<>();
        
        solvingRules.add(new FreebiesRule(cages));
        solvingRules.add(new MinusRule(cages));
        solvingRules.add(new DivideRule(cages));
        solvingRules.add(new PlusRule(cages));
        solvingRules.add(new TimesRule(cages));

        int sum = IntStream.rangeClosed(1, gridSize).sum();
        Square[][] grid = extractGrid(gridSize, cages);
        List<Cage> rows = buildRows(gridSize, sum, grid);
        List<Cage> columns = buildColumns(gridSize, sum, grid);
        
        solvingRules.add(new RowRule(rows));
        solvingRules.add(new ColumnRule(columns));
     
        return solvingRules;
    }

    @SuppressWarnings("static-method")
    private List<ISolvingRule> buildExtraSolvingRules(List<Cage> cages)
    {
        List<ISolvingRule> solvingRules = new ArrayList<>();
        
        solvingRules.add(new PairsRule(cages));
        solvingRules.add(new TriplesRule(cages));
     
        return solvingRules;
    }

    @SuppressWarnings("static-method")
    private List<Cage> buildRows(int gridSize, int sum, Square[][] grid)
    {
        List<Cage> rows = new ArrayList<>(gridSize);
        for (int y = 0; y < gridSize; y++)
        {
            List<Square> squares = new ArrayList<>(gridSize);
            for (int x = 0; x < gridSize; x++)
            {
                squares.add(grid[x][y]);
            }
            rows.add(new Cage(PLUS, sum, squares));
        }
        return rows;
    }

    @SuppressWarnings("static-method")
    private List<Cage> buildColumns(int gridSize, int sum, Square[][] grid)
    {
        List<Cage> columns = new ArrayList<>(gridSize);
        for (int x = 0; x < gridSize; x++)
        {
            List<Square> squares = new ArrayList<>(gridSize);
            for (int y = 0; y < gridSize; y++)
            {
                squares.add(grid[x][y]);
            }
            columns.add(new Cage(PLUS, sum, squares));
        }
        return columns;
    }

    @SuppressWarnings("static-method")
    private Square[][] extractGrid(int gridSize, List<Cage> cages)
    {
        Square[][] grid = new Square[gridSize][gridSize];
        cages.forEach(cage ->
            cage.getSquares().forEach(square -> grid[square.getX()][square.getY()] = square)
        );
        return grid;
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
