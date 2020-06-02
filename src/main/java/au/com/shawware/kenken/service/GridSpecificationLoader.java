/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service;

import java.io.IOException;
import java.io.Reader;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.shawware.kenken.model.GridSpecification;

/**
 * Loads a {@link GridSpecification} specification.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class GridSpecificationLoader
{
    private final ObjectMapper mapper;

    public GridSpecificationLoader()
    {
        mapper = new ObjectMapper();
    }

    public GridSpecification loadGridSpecification(Reader reader)
        throws IOException
    {
        try
        {
            return mapper.readValue(reader, GridSpecification.class);           
        }
        catch (IOException e)
        {
            throw new IOException("Unable to load grid specification", e); //$NON-NLS-1$
        }
    }
}
