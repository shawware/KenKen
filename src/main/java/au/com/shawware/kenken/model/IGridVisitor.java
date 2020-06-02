/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.model;

/**
 * Visits the squares of a {@link Grid} in order.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public interface IGridVisitor
{
    public void startGrid();
    
    public void startRow();

    public void square(int value);

    public void endRow();

    public void endGrid();
}
