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
 * Solves pairs of cages as one.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
class PairsRule extends AbstractPlusRule
{
    PairsRule()
    {
        super("Pairs", false, true); //$NON-NLS-1$
    }

    @Override
    @SuppressWarnings("hiding")
    protected List<Cage> generateCages(int gridSize, List<Cage> cages, GridState gridState)
    {
        List<Cage> pairs = new ArrayList<>();

        int cageId = 200;
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
                        pairs.add(new Combo(cageId++, c1, c2));
                    }
                }
            }
        }
        
        return pairs;
    }
}
