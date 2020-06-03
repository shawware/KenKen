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
 * Solves pairs of cages as one.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
class PairsRule extends AbstractPlusRule
{
    PairsRule(@SuppressWarnings("unused") int gridSize, List<Cage> cages)
    {
        super("Pairs", buildCages(cages), true); //$NON-NLS-1$
    }

    private static List<Cage> buildCages(List<Cage> cages)
    {
        List<Cage> pairs = new ArrayList<>();

        for (int i = 0; i < cages.size(); i++)
        {
            Cage c1 = cages.get(i);
            for (int j = i + 1; j < cages.size(); j++)
            {
                Cage c2 = cages.get(j);
                if (c1.getOperation().equals(PLUS) && c2.getOperation().equals(PLUS))
                {
                    List<Square> squares = new ArrayList<>();
                    squares.addAll(c1.getSquares());
                    squares.addAll(c2.getSquares());
                    pairs.add(new Cage(PLUS, c1.getValue() + c2.getValue(), squares));
                }
            }
        }
        
        return pairs;
    }
}
