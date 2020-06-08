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
import au.com.shawware.kenken.model.Combo;

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

        int cageId = 300;
        for (int i = 0; i < cages.size(); i++)
        {
            Cage c1 = cages.get(i);
            if (c1.getOperation().equals(PLUS) && !gridState.isSolved(c1))
            {
                for (int j = i + 1; j < cages.size(); j++)
                {
                    Cage c2 = cages.get(j);
                    if (c2.getOperation().equals(PLUS) && !gridState.isSolved(c2))
                    {
                        for (int k = j + 1; k < cages.size(); k++)
                        {
                            Cage c3 = cages.get(k);
                            if (c3.getOperation().equals(PLUS) && !gridState.isSolved(c3))
                            {
                                triples.add(new Combo(cageId++, c1, c2, c3));
                            }
                        }
                    }
                }
            }
        }        

        return triples;
    }
}
