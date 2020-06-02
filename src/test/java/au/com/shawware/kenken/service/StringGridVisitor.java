/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service;

import au.com.shawware.kenken.model.IGridVisitor;

/**
 * Builds up a simple text-based view of a grid.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class StringGridVisitor implements IGridVisitor
{
    private final char rowTerminator;
    private final StringBuilder builder;

    public StringGridVisitor(char rowTerminator)
    {
        this.rowTerminator = rowTerminator;
        this.builder = new StringBuilder();
    }

    @Override
    public void startGrid()
    {
        // Do nothing
    }

    @Override
    public void startRow()
    {
        // Do nothing
    }

    @Override
    public void square(int value)
    {
        String c = (value == 0) ? String.valueOf('-') : String.valueOf(value);
        builder.append(c);
    }

    @Override
    public void endRow()
    {
        builder.append(rowTerminator);
    }

    @Override
    public void endGrid()
    {
        builder.deleteCharAt(builder.length() - 1);
    }

    @Override
    public String toString()
    {
        return builder.toString();
    }
}
