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
import au.com.shawware.kenken.model.Square;

import static au.com.shawware.kenken.model.Cage.PLUS;

/**
 * Solves triples of cages as one.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
class TriplesRule extends AbstractPlusRule
{
    TriplesRule()
    {
        super("Triples", false, true); //$NON-NLS-1$
    }

    @Override
    @SuppressWarnings("hiding")
    protected List<Cage> generateCages(int gridSize, List<Cage> cages, GridState gridState)
    {
        List<Cage> triples = new ArrayList<>();

        for (int i = 0; i < cages.size(); i++)
        {
            Cage c1 = cages.get(i);
            if (!c1.getOperation().equals(PLUS) || gridState.isSolved(c1))
            {
                continue;
            }
            for (int j = i + 1; j < cages.size(); j++)
            {
                Cage c2 = cages.get(j);
                if (!c2.getOperation().equals(PLUS) || gridState.isSolved(c2))
                {
                    continue;
                }
                for (int k = j + 1; k < cages.size(); k++)
                {
                    Cage c3 = cages.get(k);
                    if (!c3.getOperation().equals(PLUS) || gridState.isSolved(c3))
                    {
                        continue;
                    }
                    List<Square> squares = new ArrayList<>();
                    squares.addAll(c1.getSquares());
                    squares.addAll(c2.getSquares());
                    squares.addAll(c3.getSquares());
                    triples.add(new Cage(PLUS, c1.getValue() + c2.getValue() + c3.getValue(), squares));
                }
            }
        }        

        return triples;
    }
}
