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
 * A cage specifies a contiguous section of a grid and its value.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class Cage
{
    public static final String EQUALS  = "="; //$NON-NLS-1$
    public static final String PLUS    = "+"; //$NON-NLS-1$
    public static final String MINUS   = "-"; //$NON-NLS-1$
    public static final String TIMES   = "x"; //$NON-NLS-1$
    public static final String DIVIDE  = "/"; //$NON-NLS-1$

    public static final String VALID_OPERATIONS = "=+-x/"; //$NON-NLS-1$

    protected static final String TYPE_PLAIN  = "Plain";  //$NON-NLS-1$
    protected static final String TYPE_ROW    = "Row";    //$NON-NLS-1$
    protected static final String TYPE_COLUMN = "Column"; //$NON-NLS-1$
    protected static final String TYPE_COMBO  = "Combo";  //$NON-NLS-1$
    
    private final String operation;
    private final int value;
    private final List<Square> squares;
    private final int size;
    
    private int id;
    private String type;

    @JsonCreator
    public Cage(
            @JsonProperty("operation") String operation,
            @JsonProperty("value") int value,
            @JsonProperty("squares") List<Square> squares
        )
    {
        this(0, TYPE_PLAIN, operation, value, squares);
    }

    public Cage(int id, String type, String operation, int value, List<Square> squares)
    {
        this.id = id;
        this.type = type;
        this.operation = operation;
        this.value = value;
        this.squares = (squares == null) ? Collections.emptyList() : Collections.unmodifiableList(squares);
        this.size = this.squares.size();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public final String getOperation()
    {
        return operation;
    }

    public final int getValue()
    {
        return value;
    }

    public final List<Square> getSquares()
    {
        return squares;
    }

    public final int getSize()
    {
        return size;
    }

    @Override
    @SuppressWarnings("boxing")
    public String toString()
    {
        return StringUtil.toString(id, type, operation, value, size, squares);
    }
}