/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service.rule;

import java.util.List;

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

    AbstractRule(String name, List<Cage> cages)
    {
        this.name = name;
        this.cages = cages;
        this.exhausted = cages.isEmpty();
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
