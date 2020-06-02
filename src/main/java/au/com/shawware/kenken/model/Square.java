/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.shawware.util.StringUtil;

/**
 * A square is one cell of a grid.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class Square
{
    private final int x;
    private final int y;

    @JsonCreator
    public Square(
            @JsonProperty("x") int x,
            @JsonProperty("y") int y
        )
    {
        this.x = x;
        this.y = y;
    }

    public final int getX()
    {
        return x;
    }

    public final int getY()
    {
        return y;
    }

    @Override
    @SuppressWarnings("boxing")
    public String toString()
    {
        return StringUtil.toString(x, y);
    }
}