/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import au.com.shawware.util.StringUtil;

/**
 * Represent a combination (combo) of Cages as a {@link Cage}.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class Combo extends Cage
{
    @JsonSerialize
    private final Collection<Integer> cagesIds;

    public Combo(int id, Cage... cages)
    {
        super(id, TYPE_COMBO, PLUS, Stream.of(cages).mapToInt(Cage::getValue).sum(), buildSquares(cages));
        cagesIds = Stream.of(cages).map(Cage::getId).collect(Collectors.toSet());
    }
    
    private static List<Square> buildSquares(Cage[] cages)
    {
        List<Square> squares = new ArrayList<>();
        for (Cage cage : cages)
        {
            squares.addAll(cage.getSquares());
        }
        return squares;
    }

    @Override
    @SuppressWarnings("boxing")
    public String toString()
    {
        return StringUtil.toString(getId(), TYPE_COMBO, cagesIds, getOperation(), getValue(), getSize(), getSquares());
    }
}
