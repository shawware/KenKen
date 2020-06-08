/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.model;

import java.util.List;

/**
 * Represent a Column as a {@link Cage}.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 *
 */
public class Column extends Cage
{
    public Column(int id, int value, List<Square> squares)
    {
        super(id, TYPE_COLUMN, PLUS, value, squares);
    }
}