/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.Square;

/**
 * Solves a rule that applies an operation to a pair.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
abstract class AbstractPairRule extends AbstractUnusedRule
{
    private final Function<Integer, BiFunction<Integer, Integer, Boolean>> operationSupplier;

    AbstractPairRule(String name, List<Cage> cages, String operation, Function<Integer, BiFunction<Integer, Integer, Boolean>> operationSupplier)
    {
        super(name, cages, false, operation);
        this.operationSupplier = operationSupplier;
    }

    @Override
    @SuppressWarnings("boxing")
    List<Set<Integer>> findUnusedValues(Cage cage, GridState gridState)
    {
        final List<Square> squares = cage.getSquares();
        final List<Set<Integer>> unusedValues = new ArrayList<>(squares.size());

        for (int i = 0; i < squares.size(); i++)
        {
            unusedValues.add(new HashSet<>());
        }

        handleTwoValues(squares, unusedValues, gridState, operationSupplier.apply(cage.getValue()));

        return unusedValues;
    }
 
    private void handleTwoValues(List<Square> squares, List<Set<Integer>> unusedValues, GridState gridState, BiFunction<Integer, Integer, Boolean> operation)
    {
        final List<Integer> s1Values = gridState.getValues(squares.get(0));
        final List<Integer> s2Values = gridState.getValues(squares.get(1));
        
        handlePermutations(s1Values, s2Values, operation, unusedValues.get(0));
        handlePermutations(s2Values, s1Values, operation, unusedValues.get(1));
    }

    @SuppressWarnings({ "boxing", "static-method" })
    private void handlePermutations(List<Integer> s1Values, List<Integer> s2Values, BiFunction<Integer, Integer, Boolean> operation, Set<Integer> unused)
    {
        for (Integer i1 : s1Values)
        {
            boolean permutationFound = false;
            for (Integer i2 : s2Values)
            {
                if (i1 == i2)
                {
                    continue;
                }
                if (operation.apply(i1, i2))
                {
                    permutationFound = true;
                    break;
                }
            }
            if (!permutationFound)
            {
                unused.add(i1);
            }
        }
    }
}
