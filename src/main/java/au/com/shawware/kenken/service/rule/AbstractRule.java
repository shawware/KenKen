/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

/**
 * The base class for all rules.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
abstract class AbstractRule implements ISolvingRule
{
    private final String name;
    
    protected boolean exhausted;

    AbstractRule(String name)
    {
        this.name = name;
        this.exhausted = false;
    }

    @Override
    public final boolean exhausted()
    {
        return exhausted;
    }

    @Override
    public String name()
    {
        return name;
    }
}
