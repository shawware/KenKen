/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.util.StringUtil;

/**
 * The base class for all rules.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
abstract class AbstractRule implements ISolvingRule
{
    private final String name;
    private final boolean filterCages;
    private final boolean sortCages;
    private final String operation;
    
    protected List<Cage> cages;
    private boolean exhausted;

    AbstractRule(String name, String operation, boolean filterCages, boolean sortCages)
    {
        this.name = name;
        this.operation = operation;
        this.filterCages = filterCages;
        this.sortCages = sortCages;
        this.cages = Collections.emptyList();
        this.exhausted = true;
    }

    @Override
    @SuppressWarnings("hiding")
    public final void initialise(int gridSize, List<Cage> cages, GridState gridState)
    {
        List<Cage> generatedCages = generateCages(gridSize, cages, gridState);
        if (filterCages)
        {
            generatedCages = cages.stream()
                    .filter(cage -> cage.getOperation().equals(operation))
                    .collect(Collectors.toList());
        }
        if (sortCages)
        {
            generatedCages.sort((c1, c2) -> Integer.compare(c1.getSize(), c2.getSize()));
        }
        this.cages = Collections.unmodifiableList(generatedCages);
        this.exhausted = this.cages.isEmpty();
    }

    /*
     * Subclasses should override if they want something other than the specification's cages.
     */
    @SuppressWarnings({ "static-method", "hiding", "unused" })
    protected List<Cage> generateCages(int gridSize, List<Cage> cages, GridState gridState)
    {
        return cages;
    }

    @Override
    public final void applyTo(GridState gridState)
    {
        if (exhausted)
        {
            return;
        }

        exhausted = cages.stream().allMatch(cage -> gridState.isSolved(cage));

        if (!exhausted)
        {
            applyRuleTo(gridState);
        }
    }

    protected abstract void applyRuleTo(GridState gridState);

    @Override
    public final String name()
    {
        return name;
    }

    @Override
    @SuppressWarnings("boxing")
    public String toString()
    {
        return StringUtil.toString(name, exhausted, cages);
    }
}
