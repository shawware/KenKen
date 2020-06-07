/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.List;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.util.StringUtil;

/**
 * An implementation of the {@link ISolvingRule} API for verifying rule algorithms.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class TestRule extends AbstractRule
{
    private int executionCount;
    
    public TestRule(String name, String operation, boolean filterCages, boolean sortCages)
    {
        super(name, operation, filterCages, sortCages);
        executionCount = 0;
    }

    @Override
    protected void applyRuleTo(GridState gridState)
    {
        executionCount++;
    }

    int getExecutionCount()
    {
        return executionCount;
    }

    List<Cage> getCages()
    {
        return cages;
    }

    @Override
    @SuppressWarnings("boxing")
    public String toString()
    {
        return StringUtil.toString(name(), executionCount, cages);
    }
}
