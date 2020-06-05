/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.List;
import java.util.stream.Collectors;

import au.com.shawware.kenken.model.Cage;

/**
 * The base class for all rules.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
abstract class AbstractRule implements ISolvingRule
{
    private final String name;
    protected final List<Cage> cages;
    
    // TODO: this may be able to become private
    protected boolean exhausted;

    AbstractRule(String name, List<Cage> cages, boolean cagesReady, String operation)
    {
        this.name = name;
        this.cages = cagesReady ? cages : postProcessCages(cages, operation);
        this.exhausted = this.cages.isEmpty();
    }

    @SuppressWarnings({ "static-method", "hiding" })
    private List<Cage> postProcessCages(List<Cage> cages, String operation)
    {
        List<Cage> filteredCages = cages;
        if (cages.size() > 0)
        {
            filteredCages = cages.stream()
                .filter(cage -> cage.getOperation().equals(operation))
                .collect(Collectors.toList());
            filteredCages.sort((c1, c2) -> Integer.compare(c1.getSize(), c2.getSize()));
        }
        return filteredCages;
    }

    @Override
    public final boolean exhausted()
    {
        return exhausted;
    }

    @Override
    public final String name()
    {
        return name;
    }
}
