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
import au.com.shawware.kenken.model.Grid;
import au.com.shawware.kenken.model.GridSpecification;
import au.com.shawware.kenken.service.IKenKenSolver;

/**
 * Solves a KenKen puzzle by iterating over a set of rules.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class RuleBasedSolver implements IKenKenSolver
{
    private final RuleEngine ruleEngine;

    public RuleBasedSolver()
    {
        ruleEngine = new RuleEngine(
                buildBaseSolvingRules(),
                buildExtraSolvingRules()
        );
    }

    @SuppressWarnings("static-method")
    private List<ISolvingRule> buildBaseSolvingRules()
    {
        List<ISolvingRule> solvingRules = new ArrayList<>();
        
        solvingRules.add(new FreebiesRule());
        solvingRules.add(new MinusRule());
        solvingRules.add(new DivideRule());
        solvingRules.add(new PlusRule());
        solvingRules.add(new TimesRule());
        solvingRules.add(new RowRule());
        solvingRules.add(new ColumnRule());
     
        return solvingRules;
    }

    @SuppressWarnings("static-method")
    private List<ISolvingRule> buildExtraSolvingRules()
    {
        List<ISolvingRule> solvingRules = new ArrayList<>();
        
        solvingRules.add(new PairsRule());
        solvingRules.add(new TriplesRule());
     
        return solvingRules;
    }

    @Override
    public Grid solve(GridSpecification specification)
    {
        final int gridSize = specification.getSize();
        final List<Cage> cages = specification.getCages();

        GridState gridState = new GridState(gridSize, cages);

        return ruleEngine.solve(gridSize, cages, gridState);
    }
}
