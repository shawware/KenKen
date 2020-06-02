/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.model;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.shawware.util.StringUtil;

/**
 * A KenKen grid specification. A grid is always square, ie. NxN.
 *
 * The square co-ordinates are such that:
 *
 *   [0, 0] is at the top left
 *   [N-1, N-1] is at the bottom right
 *   the x-axis moves right-to-left
 *   the y-axis moves up-and-down
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class GridSpecification
{
    private final int size;
    private final List<Cage> cages;

    @JsonCreator
    public GridSpecification(
            @JsonProperty("size") int size,
            @JsonProperty("cages") List<Cage> cages
        )
    {
        this.size = size;
        this.cages = (cages == null) ? Collections.emptyList() : Collections.unmodifiableList(cages);
    }

    public final int getSize()
    {
        return size;
    }

    public final List<Cage> getCages()
    {
        return cages;
    }

    @Override
    @SuppressWarnings("boxing")
    public String toString()
    {
        return StringUtil.toString(size, cages);
    }
}
