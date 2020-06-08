/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.model;

import java.util.List;

/**
 * Represent a Row as a {@link Cage}.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 *
 */
public class Row extends Cage
{
    public Row(int id, int value, List<Square> squares)
    {
        super(id, TYPE_ROW, PLUS, value, squares);
    }
}
