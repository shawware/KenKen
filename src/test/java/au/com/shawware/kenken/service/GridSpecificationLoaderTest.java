/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import au.com.shawware.kenken.model.Cage;
import au.com.shawware.kenken.model.GridSpecification;

/**
 * Verify the {@link GridSpecificationLoader}. 
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class GridSpecificationLoaderTest extends AbstractBaseTest
{
    @Test
    public void testSimpleLoad()
        throws IOException
    {
        final String filename = "kk-3x3.json"; //$NON-NLS-1$

        GridSpecification grid = loadGridSpecification(filename);

        assertNotNull(grid);
        assertEquals(3, grid.getSize());

        List<Cage> cages = grid.getCages();
        assertNotNull(cages);
        assertEquals(5, cages.size());
    }
}
